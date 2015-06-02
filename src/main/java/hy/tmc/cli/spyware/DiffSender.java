
package hy.tmc.cli.spyware;

import hy.tmc.cli.backend.communication.HttpResult;
import hy.tmc.cli.backend.communication.UrlCommunicator;
import hy.tmc.cli.configuration.ClientData;
import hy.tmc.cli.domain.Course;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class DiffSender {

    /**
     * Sends given difffile to all URLs specified by course.
     */ 
    public void sendToSpyware(File diffFile, Course currentCourse) {
        List<String> spywareUrls = currentCourse.getSpywareUrls();
        
        for (String url : spywareUrls) {
            sendToUrl(diffFile, url);
        }
    }

    private void sendToUrl(File diffFile, String url) {
        Map<String, String> headers = new HashMap<>();
        headers.put("X-Tmc-Version", "1");
        headers.put("X-Tmc-Username", ClientData.getUsername());
        headers.put("X-Tmc-Password", ClientData.getPassword());
        HttpResult makePostWithFile = null;
        try {
            makePostWithFile = UrlCommunicator.makePostWithFile(diffFile, url, headers);
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
        System.out.println("DATA: " + makePostWithFile.getData());
        
    }
}
