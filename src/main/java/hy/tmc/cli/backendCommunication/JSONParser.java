package hy.tmc.cli.backendCommunication;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import hy.tmc.cli.Configuration.ClientData;
import hy.tmc.cli.Configuration.ServerData;
import org.apache.http.client.HttpClient;

public class JSONParser {

    
    private static JsonObject getJSONFrom(String url) {
        HTTPResult httpResult = URLCommunicator.makeGetRequest(
                URLCommunicator.createClient(),
                url, ClientData.getFormattedUserData());
        String data = httpResult.getData();
        
        return new JsonParser().parse(data).getAsJsonObject();
    }
    
    public static String parseCourseNames() {
        JsonObject jObject = getJSONFrom(ServerData.getCoursesUrl());
        JsonArray jarray = jObject.getAsJsonArray("courses");
        
        StringBuilder result = new StringBuilder();
        for (JsonElement element : jarray) {
            result.append(element.getAsJsonObject().get("name"));
            result.append("\n");
        }
        
        return result.toString();
    }

}
