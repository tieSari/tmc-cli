package hy.tmc.cli.backendCommunication;

import static hy.tmc.cli.backendCommunication.Authorization.Authorization.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.codec.binary.Base64;
import static org.apache.http.HttpHeaders.USER_AGENT;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

public class URLCommunicator {

    /**
     *
     * @param url URL to make request to
     * @param params Any amount of parameters for the request. params[0] is
<<<<<<< HEAD
<<<<<<< HEAD
     * always username:password
     * @param client HttpClient to execute HttpRequests. It will come as parameter to 
     * enable dependency injection.
=======
     * always username password
>>>>>>> devmaster
=======
     * always username:password
     * @param client HttpClient to execute HttpRequests. It will come as parameter to 
     * enable dependency injection.
>>>>>>> 94548a7a1af4dcf90497c8b561b3eb1572ee425f
     * @return A Result-object with some data and a state of success or fail
     */
    public static HTTPResult makePostRequest(HttpClient client, String url, String... params) {
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
            return new HTTPResult(result.toString(), status, true);
        }
        catch (IOException e) {
            return new HTTPResult("", 400, false);
        }
    }

    /**
     *
     * Tries to make GET-request to specific url
     *
     * @param url URL to make request to
     * @param params Any amount of parameters for the request. params[0] is
     * always username:password
     * @param client HttpClient to execute HttpRequests. It will come as parameter to 
     * enable dependency injection.
     * @return A Result-object with some data and a state of success or fail
     */
    public static HTTPResult makeGetRequest(HttpClient client, String url, String... params) {
        try {
            HttpResponse response = createAndExecuteGet(url, params, client);
            StringBuilder result = writeResponse(response);

            return new HTTPResult(
                    result.toString(),
                    response.getStatusLine().getStatusCode(),
                    true);
        }
        catch (IOException e) {
            return new HTTPResult("", 400, false);
        }
    }

    public static boolean downloadFile(HttpClient client, String url, File file, String... params) {
        try {
            HttpResponse response = createAndExecuteGet(url,params,client);
            FileOutputStream fileOutputStream = new FileOutputStream(file);

            fileOutputStream.write(EntityUtils.toByteArray(response.getEntity()));
            fileOutputStream.close();

            return true;
        } catch (IOException e) {
            return false;
        }
    }

    private static StringBuilder writeResponse(HttpResponse response) throws UnsupportedOperationException, IOException {
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

    private static HttpResponse createAndExecuteGet(String url, String[] params, HttpClient client) throws IOException {
        HttpGet request = new HttpGet(url);
        request.setHeader("Authorization", "Basic " + encode(params[0]));
        request.addHeader("User-Agent", USER_AGENT);
        HttpResponse response = client.execute(request);
        return response;
    }

}
