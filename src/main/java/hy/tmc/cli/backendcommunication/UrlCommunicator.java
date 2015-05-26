package hy.tmc.cli.backendcommunication;

import static hy.tmc.cli.backendcommunication.authorization.Authorization.encode;
import static org.apache.http.HttpHeaders.USER_AGENT;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class UrlCommunicator {
    
    public static final int BAD_REQUEST = 400;

    /**
     * Make a post request.
     * 
     * @param client HttpClient to execute HttpRequests. It will come as parameter to enable 
    dependency injection.
     * @param url the url of the request
     * @param params parameters of the post request
     * @return A Result-object with some data and a state of success or fail
     */
    public static HttpResult makePostRequest(HttpClient client, String url,
            String... params) {
        try {
            HttpPost post = new HttpPost(url);

            String encoding = Base64.encodeBase64String((params[0]).getBytes());
            post.setHeader("Authorization", "Basic " + encoding);
            post.setHeader("User-Agent", USER_AGENT);

            List<NameValuePair> urlParameters = new ArrayList();
            post.setEntity(new UrlEncodedFormEntity(urlParameters));

            HttpResponse response = client.execute(post);
            StringBuilder result = writeResponse(response);
            int status = response.getStatusLine().getStatusCode();
            return new HttpResult(result.toString(), status, true);
        } catch (IOException e) {
            return new HttpResult("", BAD_REQUEST, false);
        }
    }

    /**
     * Tries to make GET-request to specific url.
     *
     * @param url URL to make request to
     * @param params Any amount of parameters for the request. params[0] is always username:password
     * @return A Result-object with some data and a state of success or fail
     */
    public static HttpResult makeGetRequest(String url, String... params) {
        try {
            HttpResponse response = createAndExecuteGet(url, params);
            StringBuilder result = writeResponse(response);
            return new HttpResult(
                    result.toString(),
                    response.getStatusLine().getStatusCode(),
                    true);
        } catch (IOException e) {
            return new HttpResult("", BAD_REQUEST, false);
        }
    }

    /**
     * Download a file from the internet.
     * @param client httpclient to be used
     * @param url url of the get request
     * @param file file to write the results into
     * @param params params of the get request
     * @return true if succesful
     */
    public static boolean downloadFile(HttpClient client, 
                                       String url, 
                                       File file, 
                                       String... params) {
        try {
            HttpResponse response = createAndExecuteGet(url, params);
            FileOutputStream fileOutputStream = new FileOutputStream(file);

            fileOutputStream.write(EntityUtils.toByteArray(response.getEntity()));
            fileOutputStream.close();

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
        String line = "";
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }
        return result;
    }

    public static HttpClient createClient() {
        return HttpClientBuilder.create().build();
    }

    private static HttpResponse createAndExecuteGet(String url, String[] params)
            throws IOException {
        HttpGet request = new HttpGet(url);
        request.setHeader("Authorization", "Basic " + encode(params[0]));
        request.addHeader("User-Agent", USER_AGENT);
        return executeGetRequest(request);
    }
    
    private static HttpResponse executeGetRequest(HttpGet request) 
            throws IOException {
        return createClient().execute(request);
    }

}
