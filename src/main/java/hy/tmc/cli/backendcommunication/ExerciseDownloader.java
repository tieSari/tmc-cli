package hy.tmc.cli.backendcommunication;


import static com.google.common.base.Preconditions.checkNotNull;

import net.lingala.zip4j.exception.ZipException;
import org.apache.http.client.HttpClient;

import hy.tmc.cli.configuration.ClientData;
import hy.tmc.cli.domain.Exercise;
import hy.tmc.cli.frontend.FrontendListener;
import hy.tmc.cli.zipping.DefaultUnzipDecider;
import hy.tmc.cli.zipping.UnzipDecider;
import hy.tmc.cli.zipping.Unzipper;

import java.io.File;
import java.io.IOException;
import java.util.List;




public class ExerciseDownloader {

    private FrontendListener front;

    /**
     * Constructor for ExerciseDownloader.
     *
     * @param front component which implements frontend interface
     */
    public ExerciseDownloader(FrontendListener front) {
        this.front = checkNotNull(front);
    }

    /**
     * Download exercises by course url.
     *
     * @param courseUrl course url
     */
    public void downloadExercises(String courseUrl) {
        List<Exercise> exercises = TmcJsonParser.getExercises(courseUrl);
        if (exercises.isEmpty()) {
            this.front.printLine("No exercises to download.");
            return;
        }
        downloadFiles(exercises);
    }

    /**
     * Method for downloading files if path is not defined.
     *
     * @param exercises list of exercises which will be downloaded, list is parsed from json.
     */
    public void downloadFiles(List<Exercise> exercises) {
        downloadFiles(exercises, "");
    }

    /**
     * Method for downloading files if path where to download is defined.
     *
     * @param exercises list of exercises which will be downloaded, list is parsed from json.
     * @param path server path to exercises.
     */
    public void downloadFiles(List<Exercise> exercises, String path) {
        int exCount = 0;
        path = getCorrectPath(path);
        for (Exercise exercise : exercises) {
            handleSingleExercise(exercise, exCount, exercises, path);
            exCount++;
        }
        if (this.front != null) {
            front.printLine(exercises.size() + " exercises downloaded.");
        }

    }

    /**
     * Handles downloading, unzipping & telling user information,
     * for single exercise.
     *
     * @param exercise Exercise which will be downloaded
     * @param exCount order number of exercise in downloading
     * @param exercises list of exercises which will be downloaded
     * @param path path where single exercise will be downloaded
     */
    private void handleSingleExercise(Exercise exercise, int exCount,
            List<Exercise> exercises, String path) {
        tellStateForUser(exercise, exCount, exercises);
        String filePath = path + exercise.getName() + ".zip";
        downloadFile(exercise.getZipUrl(), filePath);
        try {
            unzipFile(filePath, path);
        } catch (IOException | ZipException ex) {
            this.front.printLine("Unzipping exercise failed.");
        }
    }

    /**
     * Unzips single file after downloading. 
     * 
     * @param unzipPath path of file which will be unzipped
     * @param destinationPath destination path
     */

    public void unzipFile(String unzipPath,
                          String destinationPath) throws IOException, ZipException {
        UnzipDecider md = new DefaultUnzipDecider();
        Unzipper zipHandler = new Unzipper(unzipPath, destinationPath, md);

        zipHandler.unzip();
    }

    /**
     * Tells which exercise is currently being downloaded.
     * 
     * @param exercise exercise to be showed
     * @param exCount order number of which exercise is in downloading
     */
    private void tellStateForUser(Exercise exercise, int exCount,
            List<Exercise> exercises) {
        String output = "Downloading exercise " + exercise.getName()
                + " " + (getPercents(exCount, exercises.size())) + "%";
        this.front.printLine(output);
    }

    /**
     * Modify path to correct. Adds a trailing '/' if necessary.
     *
     * @param path the pathname to be corrected
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
     * Get advantage percent in downloading single exercise.
     * 
     * @param exCount order number of exercise in downloading
     * @param exercisesSize total amount of exercises that will be downloaded
     * @return percents
     */
    public double getPercents(int exCount, int exercisesSize) {
        return Math.round(1.0 * exCount / exercisesSize * 100);
    }

    /**
     * Downloads single .zip file by using URLCommunicator.
     *
     * @param zipUrl url which will be downloaded
     * @param path where to download
     */
    private static void downloadFile(String zipUrl, String path) {
        System.out.println(zipUrl);
        HttpClient client = UrlCommunicator.createClient();
        File file = new File(path);
        UrlCommunicator.downloadFile(client, zipUrl, file,
                ClientData.getFormattedUserData());
    }
}
