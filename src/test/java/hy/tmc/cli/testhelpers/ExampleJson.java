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
    public static String pasteResponse = pasteResponse();
    public static String feedbackExample = feedbackExample();
    public static String noFeedbackExample = noFeedbackExample();
    public static String feedbackCourse = feedbackCourseExample();

    private static String successfulSubmission() {
        return readFile("src/test/resources/successfulSubmission.json");
    }

    private static String feedbackExample() {
        return readFile("src/test/resources/feedback.json");
    }

    private static String noFeedbackExample() {
        return readFile("src/test/resourses/noFeedback.json");
    }

    private static String failedSubmission() {
        return readFile("src/test/resources/failedSubmission.json");
    }

    private static String courseExample() {
        return readFile("src/test/resources/course.json");
    }

    private static String allCoursesExample() {
        return readFile("src/test/resources/courses.json");
    }

    private static String feedbackCourseExample() {
        return readFile("src/test/resources/feedbackCourse.json");
    }

    private static String submitResponse() {
        return readFile("src/test/resources/submitResponse.json");
    }

    private static String pasteResponse() {
        return readFile("src/test/resources/pasteResponse.json");
    }

    private static String readFile(final String path) {
        try {
            return FileUtils.readFileToString(new File(path));
        }
        catch (IOException ex) {
            return "";
        }
    }
}
