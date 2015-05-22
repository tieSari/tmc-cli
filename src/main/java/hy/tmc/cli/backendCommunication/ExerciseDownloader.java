/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
import java.nio.file.Files;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.lingala.zip4j.exception.ZipException;
import org.apache.http.client.HttpClient;

public class ExerciseDownloader {

    private FrontendListener front;

    public ExerciseDownloader(FrontendListener front) {
        if (front == null) {
            return;
        }
        this.front = front;
    }

    /**
     *
     * @param courseUrl
     */
    public void downloadExercises(String courseUrl) {
        List<Exercise> exercises = JSONParser.getExercises(courseUrl);
        downloadFiles(exercises);
    }

    /**
     *
     * @param exercises
     */
    public void downloadFiles(List<Exercise> exercises) {
        downloadFiles(exercises, "");
    }

    public void downloadFiles(List<Exercise> exercises, String path) {
        int exCount = 0;
        path = getCorrectPath(path);
        for (Exercise e : exercises) {
            handleSingleExercise(e, exCount, exercises, path);
        }
        front.printLine(exercises.size() + " exercises downloaded.");
    }

    private void handleSingleExercise(Exercise e, int exCount, List<Exercise> exercises, String path) {
        tellStateForUser(e, exCount, exercises);
        String filePath = path + e.getName() + ".zip";
        downloadFile(e.getZip_url(), filePath);
        exCount++;
        try {
            unzipFile(filePath, path);
        } catch (IOException | ZipException ex) {
            this.front.printLine("Unzipping exercise failed.");
        }
    }
    

    public void unzipFile(String unzipPath, String destinationPath) throws IOException, ZipException {
        MoveDecider md = new DefaultMoveDecider(new DefaultRootDetector());
        ZipHandler zipHandler = new ZipHandler(unzipPath, destinationPath, md);
        zipHandler.unzip();
    }

    private void tellStateForUser(Exercise e, int exCount, List<Exercise> exercises) {
        this.front.printLine("Downloading exercise " + e.getName() + " " + (getPercents(exCount, exercises.size())) + "%");
    }

    public String getCorrectPath(String path) {
        if (path == null) {
            path = "";
        } else if (!path.isEmpty() && !path.endsWith("/")) {
            path += "/";
        }
        return path;
    }

    public double getPercents(int exCount, int exercisesSize) {
        return Math.round(1.0 * exCount / exercisesSize * 100);
    }

    private static void downloadFile(String zip_url, String path) {
        HttpClient client = URLCommunicator.createClient();
        File f = new File(path);
        URLCommunicator.downloadFile(client, zip_url, f, ClientData.getFormattedUserData());
    }
}
