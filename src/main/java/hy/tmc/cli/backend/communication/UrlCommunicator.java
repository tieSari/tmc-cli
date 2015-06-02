package hy.tmc.cli.backend.communication;

import static hy.tmc.cli.backend.communication.authorization.Authorization.encode;
import static org.apache.http.HttpHeaders.USER_AGENT;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;

import hy.tmc.cli.configuration.ClientData;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class UrlCommunicator {

    public static final int BAD_REQUEST = 400;

    /**
     * Creates and executes post-request to specified URL.
     *
     * @param toBeUploaded File-object that gets attached to request.
     * @param destinationUrl destination of the url.
     * @return HttpResult that contains response from the server.
     * @throws java.io.IOException if file is invalid.
     */
    public static HttpResult makePostWithFile(File toBeUploaded, String destinationUrl)
            throws IOException {
        HttpPost httppost = new HttpPost(destinationUrl);
        FileBody fileBody = new FileBody(toBeUploaded);
        addFileToRequest(fileBody, httppost);
        return getResponseResult(httppost);
    }

    private static void addFileToRequest(FileBody fileBody, HttpPost httppost) {
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        builder.addPart("submission[file]", fileBody);
        addCredentials(httppost, ClientData.getFormattedUserData());
        HttpEntity entity = builder.build();
        httppost.setEntity(entity);
    }

    /**
     * Tries to make GET-request to specific url.
     *
     * @param url URL to make request to
     * @param params Any amount of parameters for the request. params[0] is
     always username:password
     * @return A Result-object with some data and a state of success or fail
     */
    public static HttpResult makeGetRequest(String url, String... params) {
        try {
            HttpGet httpGet = createGet(url, params);
            return getResponseResult(httpGet);
        } catch (IOException e) {
            return new HttpResult("", BAD_REQUEST, false);
        }
    }

    private static HttpGet createGet(String url, String[] params)
            throws IOException {
        HttpGet request = new HttpGet(url);
        addCredentials(request, params[0]);
        return request;
    }

    /**
     * Download a file from the internet.
     *
     * @param url url of the get request
     * @param file file to write the results into
     * @param params params of the get request
     * @return true if successful
     */
    public static boolean downloadFile(
            String url,
            File file,
            String... params) {
        try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {

            HttpGet httpget = createGet(url, params);
            HttpResponse response = executeRequest(httpget);
            fileOutputStream.write(EntityUtils.toByteArray(response.getEntity()));

            return true;
        } catch (IOException e) {
            return false;
        }
    }

    private static StringBuilder writeResponse(HttpResponse response)
            throws UnsupportedOperationException, IOException {
        BufferedReader rd = new BufferedReader(
                new InputStreamReader(response.getEntity().getContent()));
        StringBuilder result = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }
        return result;
    }

    private static HttpClient createClient() {
        return HttpClientBuilder.create().build();
    }

    private static HttpResponse executeRequest(HttpRequestBase request)
            throws IOException {
        return createClient().execute(request);
    }

    private static void addCredentials(HttpRequestBase httpRequest, String credentials) {
        httpRequest.setHeader("Authorization", "Basic " + encode(credentials));
        httpRequest.setHeader("User-Agent", USER_AGENT);
    }

    private static HttpResult getResponseResult(HttpRequestBase httpRequest) 
            throws UnsupportedOperationException, IOException {
        HttpResponse response = executeRequest(httpRequest);
        StringBuilder result = writeResponse(response);
        int status = response.getStatusLine().getStatusCode();
        return new HttpResult(result.toString(), status, true);
    }

}