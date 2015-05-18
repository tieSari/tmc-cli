package hy.tmc.cli.backendCommunication;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import hy.tmc.cli.domain.Course;
import hy.tmc.cli.Configuration.ClientData;
import hy.tmc.cli.Configuration.ServerData;
import hy.tmc.cli.domain.Exercise;
import java.util.Arrays;
import java.util.List;

public class JSONParser {

    
    private static JsonObject getJSONFrom(String url) {
        HTTPResult httpResult = URLCommunicator.makeGetRequest(url, ClientData.getFormattedUserData());
        String data = httpResult.getData();
        
        return new JsonParser().parse(data).getAsJsonObject();
    }
    
    public static String getCourseNames() {
        List<Course> courses = getCourses();
        
        StringBuilder result = new StringBuilder();
        for (Course course : courses) {
            result.append(course.getName()).append(", id:").append(course.getId());
            result.append("\n");
        }
        
        return result.toString();
    }
    
    public static List<Course> getCourses() {
        JsonObject jObject = getJSONFrom(ServerData.getCoursesUrl());
        Gson mapper = new Gson();
        Course[] courses = mapper.fromJson(jObject.getAsJsonArray("courses"), Course[].class);
        return Arrays.asList(courses);
    }
    
    public static String getExerciseNames(String courseUrl) {
        List<Exercise> exercises = getExercises(courseUrl);
        
        StringBuilder asString = new StringBuilder();
        for (Exercise exercise : exercises) {
            asString.append(exercise.getName());
            asString.append("\n");
        }
        return asString.toString();
    }
    
    public static List<Exercise> getExercises(String courseUrl) {
        
        JsonObject course = getJSONFrom(courseUrl);
        Gson mapper = new Gson();
        Exercise[] exercises = mapper.fromJson(course.getAsJsonObject("course").get("exercises"), Exercise[].class);
        return Arrays.asList(exercises);
        
    }
    

}
