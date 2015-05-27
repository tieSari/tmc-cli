package hy.tmc.cli.backendcommunication;

import hy.tmc.cli.domain.Course;
import hy.tmc.cli.domain.Exercise;

import hy.tmc.cli.zipping.ProjectRootFinder;
import hy.tmc.cli.zipping.Zipper;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import net.lingala.zip4j.exception.ZipException;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpEntity;
import static org.apache.http.HttpHeaders.USER_AGENT;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import static org.apache.http.client.methods.RequestBuilder.post;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.util.EntityUtils;

public class CourseSubmitter {

    private ProjectRootFinder rootFinder;

    public CourseSubmitter(ProjectRootFinder rootFinder) {
        this.rootFinder = rootFinder;
    }

    public void submit(String currentPath, String exerciseName) throws UnsupportedEncodingException, IOException {
        Course currentCourse = getCurrentCourse(currentPath);
        List<Exercise> exercisesForCurrentCourse = TmcJsonParser.getExercises(currentCourse.getId());
        Exercise currentExercise = findCurrentExercise(exercisesForCurrentCourse, exerciseName);

        String exerciseFolderToZip = currentPath + "/" + exerciseName;
        String destinationFolder = currentPath;
        String submissionZipPath = currentPath + "/submission.zip";
        String URL = currentExercise.getReturnUrl() + "?api_version=7";
        //String URL = "http://localhost:8080/gifs";

        zip(exerciseFolderToZip, submissionZipPath);
        HttpClient httpclient = HttpClientBuilder.create().build();
        File file = new File(submissionZipPath);
        Path path = Paths.get(submissionZipPath);
        byte[] data = Files.readAllBytes(path);

        HttpPost httppost = new HttpPost(URL);

        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

        FileBody fileBody = new FileBody(file); //image should be a String
        builder.addPart("submission[file]", fileBody);

        String encoding = Base64.encodeBase64String(("test:1234").getBytes());
        httppost.setHeader("Authorization", "Basic " + encoding);
        httppost.setHeader("User-Agent", USER_AGENT);
      //  httppost.setHeader("Content-Type", );
        
        
        HttpEntity entity = builder.build();
        httppost.setEntity(entity);
        System.out.println("executing request " + httppost.getRequestLine());
        HttpResponse response = httpclient.execute(httppost);
        HttpEntity resEntity = response.getEntity();

        System.out.println(response.getStatusLine());
        if (resEntity != null) {
            System.out.println(EntityUtils.toString(resEntity));
        }
        if (resEntity != null) {
            resEntity.consumeContent();
        }

        httpclient.getConnectionManager().shutdown();

        System.out.println(response.getStatusLine());
        System.out.println(response);

        new File(submissionZipPath).delete();
    }

    private void zip(String exerciseFolderToZip, String currentPath) {
        try {
            new Zipper().zip(exerciseFolderToZip, currentPath);
        }
        catch (ZipException ex) {
            System.err.println(ex.getMessage());
        }
    }

    private Exercise findCurrentExercise(List<Exercise> exercisesForCurrentCourse, String exerciseName) {
        Exercise currentExercise = null;
        for (Exercise exercise : exercisesForCurrentCourse) {
            if (exercise.getName().contains(exerciseName)) {
                currentExercise = exercise;
                break;
            }
        }
        return currentExercise;
    }

    public Course getCurrentCourse(String directoryPath) {
        String[] exerciseName = getExerciseName(directoryPath);
        return getCurrentCourseByName(exerciseName);
    }

    private Course getCurrentCourseByName(String[] foldersPath) {
        List<Course> courses = TmcJsonParser.getCourses();
        Course currentCourse = null;
        for (Course course : courses) {
            for (String folderName : foldersPath) {
                if (course.getName().equals(folderName)) {
                    currentCourse = course;
                    break;
                }
            }
        }
        assert currentCourse != null; // if currentCourse is null this fails
        return currentCourse;
    }

    public String[] getExerciseName(String directoryPath) {
        Path path = rootFinder.getRootDirectory(Paths.get(directoryPath));
        return path.toString().split("/");
    }
}
