package hy.tmc.cli.backendcommunication;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import hy.tmc.cli.configuration.ClientData;
import hy.tmc.cli.configuration.ConfigHandler;
import hy.tmc.cli.domain.Course;
import hy.tmc.cli.domain.Exercise;

import java.util.Arrays;
import java.util.List;

/**
 * A Utility class for handling JSONs downloaded from the TMC-server.
 */
public class TmcJsonParser {

    /**
     * Get JSON-data from url.
     *
     * @param url url from which the object data is fetched
     * @return JSON-object containing JSON-data
     */
    private static JsonObject getJsomFrom(String url) {
        HttpResult httpResult = UrlCommunicator.makeGetRequest(
                url, ClientData.getFormattedUserData()
        );
        String data = httpResult.getData();
        return new JsonParser().parse(data).getAsJsonObject();
    }
    
    /**
     * Get the names of all courses on the server specified by ServerData.
     * @return String containing all course names separated by newlines
     */
    public static String getCourseNames() {
        List<Course> courses = getCourses();

        StringBuilder result = new StringBuilder();
        for (Course course : courses) {
            result.append(course.getName()).append(", id:")
                    .append(course.getId());
            result.append("\n");
        }

        return result.toString();
    }

    /**
     * Get list of all the courses on the server specified by ServerData.
     * @return List of Course-objects
     */
    public static List<Course> getCourses() {
        JsonObject jsonObject = getJsomFrom(new ConfigHandler()
                .readCoursesAddress());
        Gson mapper = new Gson();
        Course[] courses = mapper
                .fromJson(jsonObject.getAsJsonArray("courses"), Course[].class);
        return Arrays.asList(courses);
    }

    /**
     * Get all exercise names of a course specified by courseUrl.
     * @param courseUrl url of the course we are interested in
     * @return String of all exercise names separated by newlines
     */
    public static String getExerciseNames(String courseUrl) {
        List<Exercise> exercises = getExercises(courseUrl);
        StringBuilder asString = new StringBuilder();
        for (Exercise exercise : exercises) {
            asString.append(exercise.getName());
            asString.append("\n");
        }
        return asString.toString();
    }

    /**
     * Get all exercises of a course specified by Course.
     * @param course Course that we are interested in
     * @return List of all exercises as Exercise-objects
     */
    public static List<Exercise> getExercises(Course course) {
        return getExercises(course.getId());
    }

    /**
     * Get all exercises of a course specified by Course id.
     * @param id id of the course we are interested in
     * @return List of a all exercises as Exercise-objects
     */
    public static List<Exercise> getExercises(int id) {
        ConfigHandler confighandler = new ConfigHandler();
        return getExercises(confighandler.getCourseUrl(id));
    }

    /**
    /**
     * Get all exercises of a course specified by courseUrl.
     * @param courseUrl url of the course we are interested in
     * @return List of all exercises as Exercise-objects
     */
    public static List<Exercise> getExercises(String courseUrl) {
        JsonObject course = getJsomFrom(courseUrl);
        Gson mapper = new Gson();
        System.out.println(course);
        Exercise[] exercises = mapper
                .fromJson(course.getAsJsonObject("course").get("exercises"), 
                        Exercise[].class);
        return Arrays.asList(exercises);
    }

}