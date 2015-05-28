package hy.tmc.cli.testhelpers;

import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;

public class ExampleJSON {

    public static String courseExample = courseExample();
    public static String allCoursesExample = allCoursesExample();
    public static String successfulSubmission = successfulSubmission();
    public static String failedSubmission = failedSubmission();
    
    private static String successfulSubmission() {
        try {
            return FileUtils.readFileToString(new File("src/test/resources/successfulSubmission.json"));
        }
        catch (IOException ex) {
            return "";
        }
    }
    
    private static String failedSubmission() {
        try {
            return FileUtils.readFileToString(new File("src/test/resources/failedSubmission.json"));
        }
        catch (IOException ex) {
            return "";
        }
    }
    
    private static String courseExample(){
        try {
            return FileUtils.readFileToString(new File("src/test/resources/course.json"));
        }
        catch (IOException ex) {
            return "";
        }
    }
    
    private static String allCoursesExample() {
        try {
            return FileUtils.readFileToString(new File("src/test/resources/courses.json"));
        }
        catch (IOException ex) {
            return "";
        }
    }
    
}