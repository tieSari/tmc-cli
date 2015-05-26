package hy.tmc.cli.testhelpers;

import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;

public class ExampleJSON {

    public static String courseExample = courseExample();
    public static String allCoursesExample = allCoursesExample();
    
    private static String courseExample(){
        System.out.println(System.getProperty("user.dir"));
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