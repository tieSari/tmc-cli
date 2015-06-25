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
    public static String noDeadlineCourseExample = noDeadlineCourseExample();
    public static String failingCourse = failingCourse();
    public static String failedSubmitResponse = failedSubmitResponse();
    public static String pasteResponse = pasteResponse();
    public static String feedbackExample = feedbackExample();
    public static String noFeedbackExample = noFeedbackExample();
    public static String feedbackCourse = feedbackCourseExample();
    public static String trivialNoFeedback = trivialNoFeedback();
    public static String checkstyleFailed = checkstyleFailed();
    public static String valgrindFailed = valgrindFailed();
    public static String expiredCourseExample = expiredCourseExample();
    
    private static String failingCourse() {
        return readFile("src/test/resources/failingCourse.json");
    }
    
    private static String failedSubmitResponse() {
        return readFile("src/test/resources/failedSubmitResponse.json");
    }

    private static String successfulSubmission() {
        return readFile("src/test/resources/successfulSubmission.json");
    }

    private static String feedbackExample() {
        return readFile("src/test/resources/feedback.json");
    }

    private static String noFeedbackExample() {
        return readFile("src/test/resources/noFeedback.json");
    }

    private static String trivialNoFeedback() {
        return readFile("src/test/resources/trivialNoFeedback.json");
    }

    private static String failedSubmission() {
        return readFile("src/test/resources/failedSubmission.json");
    }

    private static String courseExample() {
        return readFile("src/test/resources/course.json");
    }
    
    private static String noDeadlineCourseExample(){
        return readFile("src/test/resources/nodeadlinecourse.json");
    }
    
    private static String expiredCourseExample(){
        return readFile("src/test/resources/expiredCourse.json");
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

    private static String checkstyleFailed() {
        return readFile("src/test/resources/checkstyleFailed.json");
    }

    private static String valgrindFailed() {
        return readFile("src/test/resources/valgrindFailed.json");
    }

    private static String readFile(final String path) {
        try {
            return FileUtils.readFileToString(new File(path));
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
            return "";
        }
    }
}
