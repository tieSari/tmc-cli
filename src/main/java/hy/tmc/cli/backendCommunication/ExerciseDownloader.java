
package hy.tmc.cli.backendCommunication;

import hy.tmc.cli.Configuration.ClientData;
import hy.tmc.cli.domain.Exercise;
import hy.tmc.cli.frontend_communication.FrontendListener;
import hy.tmc.cli.zipping.DefaultMoveDecider;
import hy.tmc.cli.zipping.DefaultRootDetector;
import hy.tmc.cli.zipping.MoveDecider;
import hy.tmc.cli.zipping.ZipHandler;
import java.io.File;
import java.io.IOException;
import java.util.List;
import net.lingala.zip4j.exception.ZipException;
import org.apache.http.client.HttpClient;

public class ExerciseDownloader {

    private FrontendListener front;

    /**
     * Constructor for ExerciseDownloader
     * @param front component which implements frontend interface
     */
    public ExerciseDownloader(FrontendListener front) {
        if (front == null) {
            return;
        }
        this.front = front;
    }

    /**
     * Download exercises by course url
     * @param courseUrl course url
     */
    public void downloadExercises(String courseUrl) {
        List<Exercise> exercises = JSONParser.getExercises(courseUrl);
        downloadFiles(exercises);
    }

    /**
     * Method for downloading files if path is not defined
     * @param exercises list of exercises which will be downloaded, list is parsed from json
     */
    public void downloadFiles(List<Exercise> exercises) {
        downloadFiles(exercises, "");
    }

    /**
     * Method for downloading files if path where to download is defined
     * @param exercises list of exercises which will be downloaded, list is parsed from json
     * @param path server path to exercises.
     */
    public void downloadFiles(List<Exercise> exercises, String path) {
        int exCount = 0;
        path = getCorrectPath(path);
        for (Exercise exercise : exercises) {
            handleSingleExercise(exercise, exCount, exercises, path);
            exCount++;
        }
        if (this.front != null)  {
            front.printLine(exercises.size() + " exercises downloaded.");
        }

    }

    /**
     * Handles downloading, unzipping & telling user information, for single exercise
     * @param exercise Exercise which will be downloaded
     * @param exCount order number of exercise in downloading
     * @param exercises list of exercises which will be downloaded
     * @param path path where single exercise will be downloaded
     */
    private void handleSingleExercise(Exercise exercise, int exCount, List<Exercise> exercises, String path) {
        tellStateForUser(exercise, exCount, exercises);
        String filePath = path + exercise.getName() + ".zip";
        downloadFile(exercise.getZip_url(), filePath);
        try {
            unzipFile(filePath, path);
        } catch (IOException | ZipException ex) {
            this.front.printLine("Unzipping exercise failed.");
        }
    }
    
    /**
     * Unzips a zip file
     * @param unzipPath path of file which will be unzipped
     * @param destinationPath destination path 
     * @throws IOException
     * @throws ZipException
     */
    public void unzipFile(String unzipPath, String destinationPath) throws IOException, ZipException {
        MoveDecider md = new DefaultMoveDecider(new DefaultRootDetector());
        ZipHandler zipHandler = new ZipHandler(unzipPath, destinationPath, md);
        zipHandler.unzip();
    }

    /**
     * Tells which exercise is currently being downloaded
     * @param exercise exercise to be showed
     * @param exCount order number of which exercise is in downloading
     */
    private void tellStateForUser(Exercise exercise, int exCount, List<Exercise> exercises) {
        this.front.printLine("Downloading exercise " + exercise.getName() + " " + (getPercents(exCount, exercises.size())) + "%");
    }

    /**
     * Modify path to correct
     * @return corrected path
     */
    public String getCorrectPath(String path) {
        if (path == null) {
            path = "";
        } else if (!path.isEmpty() && !path.endsWith("/")) {
            path += "/";
        }
        return path;
    }

    /**
     * Get advantage percent in downloading single exercise
     * @param exCount order number of exercise in downloading 
     * @param exercisesSize total amount of exercises that will be downloaded
     * @return percents
     */
    public double getPercents(int exCount, int exercisesSize) {
        return Math.round(1.0 * exCount / exercisesSize * 100);
    }

    /**
     * Downloads single .zip file by using URLCommunicator
     * @param zip_url url which will be downloaded
     * @param path where to download
     */
    private static void downloadFile(String zip_url, String path) {
        HttpClient client = URLCommunicator.createClient();
        File f = new File(path);
        URLCommunicator.downloadFile(client, zip_url, f, ClientData.getFormattedUserData());
    }
}
