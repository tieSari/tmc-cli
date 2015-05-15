/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hy.tmc.cli.backendCommunication;

import hy.tmc.cli.Configuration.ClientData;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
import org.apache.http.impl.client.DefaultHttpClient;

/**
 *
 * @author chang
 */
public class URLCommunicator {

    /**
     *
     * @param url URL to make request to
     * @param params Any amount of parameters for the request. params[0] is always username:password
     * @return A Result-object with some data and a state of success or fail
     */
    public static HTTPResult makePostRequest(String url, String... params) {
        try {

            
            
            
            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(url);

            String encoding = Base64.encodeBase64String((params[0]).getBytes());
            post.setHeader("Authorization", "Basic " + encoding);

            post.setHeader("User-Agent", USER_AGENT);

            List<NameValuePair> urlParameters = new ArrayList();

            post.setEntity(new UrlEncodedFormEntity(urlParameters));

            HttpResponse response = client.execute(post);
            BufferedReader rd = new BufferedReader(
                    new InputStreamReader(response.getEntity().getContent()));

            StringBuilder result = new StringBuilder();
            String line = "";
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
            int status = response.getStatusLine().getStatusCode();
            return new HTTPResult(result.toString(), status, true);
        } catch (IOException e) {
            return new HTTPResult("", 400, false);
        }
    }

    /**
     *
     * @param url URL to make request to
     * @param params Any amount of parameters for the request. params[0] is always username:password
     * @return A Result-object with some data and a state of success or fail
     */
    
    
    public static HTTPResult makeGetRequest(String url, String... params) {
        try {

            HttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet(url);


            String encoding = Base64.encodeBase64String((params[0]).getBytes());
            request.setHeader("Authorization", "Basic " + encoding);
            
            request.addHeader("User-Agent", USER_AGENT);

            HttpResponse response = client.execute(request);

            BufferedReader rd = new BufferedReader(
                    new InputStreamReader(response.getEntity().getContent()));

            StringBuilder result = new StringBuilder();
            String line = "";
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }

            return new HTTPResult(result.toString(),
                    response.getStatusLine().getStatusCode(),
                    true);
        } catch (IOException e) {
            return new HTTPResult("", 400, false);
        }
    }

}
