/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hy.tmc.cli.backendCommunication;

import hy.tmc.cli.Configuration.ClientData;
import hy.tmc.cli.domain.Exercise;
import java.io.File;
import java.nio.file.Files;
import java.util.List;

import hy.tmc.cli.frontend_communication.FrontendListener;
import org.apache.http.client.HttpClient;

public class ExerciseDownloader {

    private FrontendListener front;

    public ExerciseDownloader() {
        this.front = null;
    }

    public ExerciseDownloader(FrontendListener front) {
        this.front = front;
    }
    
    /**
     *
     * @param courseUrl
     */
    public void downloadExercises(String courseUrl){
        List<Exercise> exercises = JSONParser.getExercises(courseUrl);
        downloadFiles(exercises);
    }
    
    /**
     * 
     * @param exercises
     */
    public void downloadFiles(List<Exercise> exercises){
        downloadFiles(exercises,"");
    }

    public void downloadFiles(List<Exercise> exercises, String path) {
        int exCount = 0;
        if (path == null) {
            path = "";
        } else if (!path.isEmpty() && !path.endsWith("/")) {
            path += "/";
        }
        for(Exercise e : exercises) {
            if (this.front != null) {
                this.front.printLine("Downloading exercise " + e.getName() + " " + (Math.round(1.0*exCount/exercises.size()*100)) + "%");
            }
            String filePath = path + e.getName() + ".zip";
            downloadFile(e.getZip_url(), filePath);
            exCount++;
        }
        if (this.front != null)  {
            front.printLine(exercises.size() + " exercises downloaded.");
        }

    }

    private static void downloadFile(String zip_url, String path) {
        HttpClient client = URLCommunicator.createClient();
        File f = new File(path);
        URLCommunicator.downloadFile(client, zip_url, f, ClientData.getFormattedUserData());
    }
}
