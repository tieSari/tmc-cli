package hy.tmc.cli.backendcommunication;

import hy.tmc.cli.domain.Course;
import hy.tmc.cli.domain.Exercise;

import hy.tmc.cli.zipping.RootFinder;
import hy.tmc.cli.zipping.ZipMaker;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import net.lingala.zip4j.exception.ZipException;

public class CourseSubmitter {

    private RootFinder rootFinder;
    private ZipMaker zipper;

    public CourseSubmitter(RootFinder rootFinder, ZipMaker zipper) {
        this.zipper = zipper;
        this.rootFinder = rootFinder;
    }

    /**
     * Submits folder of exercise to TMC.
     *
     * @param currentPath path from which this was called.
     * @param exerciseName name of exercise to submit
     * @return String with url from which to get results or null if exercise was not found.
     * @throws IOException if failed to create zip.
     */
    public String submit(String currentPath, String exerciseName) throws IOException {
        Exercise currentExercise = findExercise(currentPath, exerciseName);
        if (currentExercise == null) {
            return null;
        }
        String submissionZipPath = currentPath + "/submission.zip";
        String URL = currentExercise.getReturnUrl() + "?api_version=7";

        zip(findExerciseFolderToZip(currentPath), submissionZipPath);
        String resultUrl = sendSubmissionToServer(submissionZipPath, URL);
        new File(submissionZipPath).delete();
        return resultUrl;
    }

    private String findExerciseFolderToZip(String currentPath) {
        return rootFinder.getRootDirectory(
                Paths.get(currentPath)
        ).toString();
    }

    private String sendSubmissionToServer(String submissionZipPath, String URL) throws IOException {
        HttpResult makePostWithFile = UrlCommunicator.makePostWithFile(new File(submissionZipPath), URL);
        return TmcJsonParser.getSubmissionUrl(makePostWithFile);
    }

    public String submit(String currentPath) throws IOException {
        return submit(currentPath, currentPath);
    }

    private Exercise findExercise(String currentPath, String exerciseName) {
        Course currentCourse = getCurrentCourse(currentPath);
        List<Exercise> exercisesForCurrentCourse = TmcJsonParser.getExercises(currentCourse.getId());
        Exercise currentExercise = findCurrentExercise(exercisesForCurrentCourse, exerciseName);
        return currentExercise;
    }

    private void zip(String exerciseFolderToZip, String currentPath) {
        try {
            this.zipper.zip(exerciseFolderToZip, currentPath);
        }
        catch (ZipException ex) {
            System.err.println(ex.getMessage());
        }
    }

    private Exercise findCurrentExercise(List<Exercise> exercisesForCurrentCourse, String currentDir) {
        String[] path = rootFinder.getRootDirectory(Paths.get(currentDir)).toString().split("/");
        String directory = path[path.length - 1];
        for (Exercise exercise : exercisesForCurrentCourse) {
            if (exercise.getName().contains(directory)) {
                return exercise;
            }
        }
        return null;
    }

    private Course getCurrentCourse(String directoryPath) {
        String[] exerciseName = getExerciseName(directoryPath);
        return findCourseByPath(exerciseName);
    }

    public Course findCourseByPath(String[] foldersPath) {
        List<Course> courses = TmcJsonParser.getCourses();
        Course currentCourse = null;
        for (Course course : courses) {
            for (String folderName : foldersPath) {
                if (course.getName().equals(folderName)) {
                    return course;
                }
            }
        }
        assert currentCourse != null;
        return currentCourse;
    }

    public String[] getExerciseName(String directoryPath) {
        Path path = rootFinder.getRootDirectory(Paths.get(directoryPath));
        return path.toString().split("/");
    }
}
