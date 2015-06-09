package hy.tmc.cli.backend.communication;

import com.google.common.base.Optional;
import static com.google.common.base.Strings.isNullOrEmpty;

import hy.tmc.cli.configuration.ClientData;
import hy.tmc.cli.domain.Exercise;

import hy.tmc.cli.zipping.DefaultUnzipDecider;
import hy.tmc.cli.zipping.UnzipDecider;
import hy.tmc.cli.zipping.Unzipper;

import net.lingala.zip4j.exception.ZipException;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class ExerciseDownloader {

    /**
     * Download exercises by course url.
     *
     * @param courseUrl course url
     * @return info about downloading.
     */
    public Optional<String> downloadExercises(String courseUrl) {
        List<Exercise> exercises = TmcJsonParser.getExercises(courseUrl);
        if (exercises.isEmpty()) {
            return Optional.of("No exercises to download.");
        }
        return downloadFiles(exercises);
    }

    /**
     * Method for downloading files if path is not defined.
     *
     * @param exercises list of exercises which will be downloaded, list is parsed from json.
     * @return info about downloading.
     */
    public Optional<String> downloadFiles(List<Exercise> exercises) {
        return downloadFiles(exercises, "");
    }

    /**
     * Method for downloading files if path where to download is defined.
     * @return info about downloading.
     */
    public Optional<String> downloadFiles(List<Exercise> exercises, String path) {
        return downloadFiles(exercises, path, null);
    }

    /**
     * Method for downloading files if path where to download is defined. Also requires seperate
     * folder name that will be created to defined path.
     *
     * @param exercises list of exercises which will be downloaded, list is parsed from json.
     * @param path server path to exercises.
     * @param folderName folder name of where exercises will be extracted (for example course name)
     * @return
     */
    public Optional<String> downloadFiles(List<Exercise> exercises, String path, String folderName) {
        StringBuilder exercisesListed = new StringBuilder();
        int exCount = 0;
        path = getCorrectPath(path);
        if (!isNullOrEmpty(folderName)) {
            path += folderName + File.separator;
        }
        File coursePath = new File(path);
        if (!coursePath.exists()) {
            coursePath.mkdirs();
        }
        for (Exercise exercise : exercises) {
            exercisesListed.append(handleSingleExercise(exercise, exCount, exercises, path));
            exCount++;
        }
        exercisesListed.append(exercises.size())
                       .append(" exercises downloaded.");
        return Optional.of(exercisesListed.toString());
    }

    /**
     * Handles downloading, unzipping & telling user information, for single exercise.
     *
     * @param exercise Exercise which will be downloaded
     * @param exCount order number of exercise in downloading
     * @param exercises list of exercises which will be downloaded
     * @param path path where single exercise will be downloaded
     */
    private String handleSingleExercise(Exercise exercise, int exCount,
            List<Exercise> exercises, String path) {
        String exerciseInfo = tellStateForUser(exercise, exCount, exercises);
        String filePath = path + exercise.getName() + ".zip";
        downloadFile(exercise.getZipUrl(), filePath);
        try {
            unzipFile(filePath, path);
            deleteZip(filePath);
        }
        catch (IOException | ZipException ex) {
            exerciseInfo = "Unzipping exercise failed.";
        }
        return exerciseInfo;
    }

    /**
     * Delete .zip -file after unzipping.
     *
     * @param filePath path to delete
     */
    private void deleteZip(String filePath) {
        File file = new File(filePath);
        file.delete();
    }

    /**
     * Unzips single file after downloading.
     *
     * @param unzipPath path of file which will be unzipped
     * @param destinationPath destination path
     */
    public void unzipFile(String unzipPath,
            String destinationPath) throws IOException, ZipException {
        UnzipDecider decider = new DefaultUnzipDecider();
        Unzipper zipHandler = new Unzipper(unzipPath, destinationPath, decider);
        zipHandler.unzip();
    }

    /**
     * Tells which exercise is currently being downloaded.
     *
     * @param exercise exercise to be showed
     * @param exCount order number of which exercise is in downloading
     */
    private String tellStateForUser(Exercise exercise, int exCount,
            List<Exercise> exercises) {
        String output = "Downloading exercise " + exercise.getName()
                + " " + (getPercents(exCount, exercises.size())) + "%";
        return output;
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
        } else if (!path.isEmpty() && !path.endsWith(File.separator)) {
            path += File.separator + "";
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
        File file = new File(path);
        UrlCommunicator.downloadFile(zipUrl, file,
                ClientData.getFormattedUserData());
    }
}
