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
import org.apache.http.client.HttpClient;

public class ExerciseDownloader {
    
    /**
     *
     * @param courseUrl
     */
    public static void downloadExercises(String courseUrl){
        List<Exercise> exercises = JSONParser.getExercises(courseUrl);
        downloadFiles(exercises);
    }
    
    /**
     * 
     * @param exercises
     */
    public static void downloadFiles(List<Exercise> exercises){
        for(Exercise e : exercises) {
            downloadFile(e.getZip_url(), e.getName());
        }
    }

    private static void downloadFile(String zip_url, String path) {
        HttpClient client = URLCommunicator.createClient();
        File f = new File(path);
        URLCommunicator.downloadFile(client, zip_url, f, ClientData.getFormattedUserData());
    }
}
