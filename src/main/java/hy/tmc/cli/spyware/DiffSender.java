
package hy.tmc.cli.spyware;

import hy.tmc.cli.backend.communication.HttpResult;
import hy.tmc.cli.backend.communication.UrlCommunicator;
import hy.tmc.cli.configuration.ClientData;
import hy.tmc.cli.domain.Course;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class DiffSender {

    /**
     * Sends given file to all URLs specified by course.
     * @param diffFile includes diffs to be sended
     * @param currentCourse tell all spywareUrls
     * @return all results
     */ 
    public List<HttpResult> sendToSpyware(File diffFile, Course currentCourse) {
        List<String> spywareUrls = currentCourse.getSpywareUrls();
        List<HttpResult> results = new ArrayList<>();
        for (String url : spywareUrls) {
            results.add(sendToUrl(diffFile, url));
        }
        return results;
    }

    /**
     * Sends given file to all URLs specified by course.
     * @param diffFile includes diffs to be sended
     * @param url of destination 
     * @return  HttpResult from UrlCommunicator
     */
    public HttpResult sendToUrl(File diffFile, String url) {
        Map<String, String> headers = new HashMap<>();
        headers.put("X-Tmc-Version", "1");
        headers.put("X-Tmc-Username", ClientData.getUsername());
        headers.put("X-Tmc-Password", ClientData.getPassword());
        HttpResult result = makePostRequest(diffFile, url, headers);
        return result;
    }

    private HttpResult makePostRequest(File diffFile, String url, Map<String, String> headers) {
        HttpResult result = null;
        try {
            result = UrlCommunicator.makePostWithFile(diffFile, url, headers);
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
        return result;
    }
}
