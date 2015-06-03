package hy.tmc.cli.testhelpers;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class ExampleJson {

    public static String courseExample = courseExample();
    public static String allCoursesExample = allCoursesExample();
    public static String successfulSubmission = successfulSubmission();
    public static String failedSubmission = failedSubmission();
    public static String submitResponse = submitResponse();
<<<<<<< HEAD
    public static String pasteResponse = pasteResponse();
=======
>>>>>>> a3f2f9be92426cd89883a869c76a9f187e20a8b1
    
    private static String successfulSubmission() {
        return readFile("src/test/resources/successfulSubmission.json");
    }
<<<<<<< HEAD

=======
    
>>>>>>> a3f2f9be92426cd89883a869c76a9f187e20a8b1
    private static String failedSubmission() {
        return readFile("src/test/resources/failedSubmission.json");
    }

<<<<<<< HEAD
    private static String courseExample() {
        return readFile("src/test/resources/course.json");
    }

    private static String allCoursesExample() {
        return readFile("src/test/resources/courses.json");
    }

    private static String submitResponse() {
        return readFile("src/test/resources/submitResponse.json");
    }

    private static String pasteResponse() {
        return readFile("src/test/resources/pasteResponse.json");
    }

=======
    private static String courseExample(){
        return readFile("src/test/resources/course.json");
    }
    
    
    private static String allCoursesExample() {
        return readFile("src/test/resources/courses.json");
    }
    
    private static String submitResponse() {
        return readFile("src/test/resources/submitResponse.json");
    }
    
>>>>>>> a3f2f9be92426cd89883a869c76a9f187e20a8b1
    private static String readFile(final String path) {
        try {
            return FileUtils.readFileToString(new File(path));
        }
        catch (IOException ex) {
            return "";
        }
    }
<<<<<<< HEAD
}
=======
}
>>>>>>> a3f2f9be92426cd89883a869c76a9f187e20a8b1
