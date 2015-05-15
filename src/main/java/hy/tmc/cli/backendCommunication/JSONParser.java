/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hy.tmc.cli.backendCommunication;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import hy.tmc.cli.Configuration.ClientData;

/**
 *
 * @author chang
 */
public class JSONParser {

    public static String parseCourses() {
        HTTPResult httpResult = URLCommunicator.makeGetRequest("https://tmc.mooc.fi/staging/courses.json?api_version=7", ClientData.getFormattedUserData());
        String data = httpResult.getData();
        
        JsonElement jelement = new JsonParser().parse(data);
        System.out.println(data);
        JsonObject jobject = jelement.getAsJsonObject();
        JsonArray jarray = jobject.getAsJsonArray("courses");
        System.out.println(jarray);
        StringBuilder result = new StringBuilder();
        for (JsonElement element : jarray) {
            result.append(element.getAsJsonObject().get("name"));
            result.append("\n");
        }
        
        //jobject = jarray.get(0).getAsJsonObject();
        //String result = jobject.get("name").toString();
        return result.toString();
    }

}
