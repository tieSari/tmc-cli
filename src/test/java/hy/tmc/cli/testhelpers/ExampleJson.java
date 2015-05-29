package hy.tmc.cli.testhelpers;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class ExampleJson {

    public static String courseExample = courseExample();
    public static String allCoursesExample = allCoursesExample();
    
    private static String courseExample() {
        try {
            return FileUtils.readFileToString(new File("src/test/resources/course.json"));
        } catch (IOException ex) {
            return "";
        }
    }
    
    private static String allCoursesExample() {
        try {
            return FileUtils.readFileToString(new File("src/test/resources/courses.json"));
        } catch (IOException ex) {
            return "";
        }
    }
    
}