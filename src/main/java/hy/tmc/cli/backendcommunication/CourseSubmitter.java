package hy.tmc.cli.backendcommunication;

import hy.tmc.cli.domain.Course;
import hy.tmc.cli.domain.Exercise;

import hy.tmc.cli.zipping.RootFinder;
import hy.tmc.cli.zipping.Zipper;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import net.lingala.zip4j.exception.ZipException;

public class CourseSubmitter {

    private RootFinder rootFinder;

    public CourseSubmitter(RootFinder rootFinder) {
        this.rootFinder = rootFinder;
    }

    /**
     * Submits folder of exercise to TMC.
     * 
     * @param currentPath path from which this was called.
     * @param exerciseName name of exercise to submit
     * @return String with url from which to get results.
     * @throws IOException if failed to create zip.
     */
    
    public String submit(String currentPath, String exerciseName) throws IOException {
        Exercise currentExercise = findExercise(currentPath, exerciseName);

        String exerciseFolderToZip = currentPath + "/" + exerciseName;
        String submissionZipPath = currentPath + "/submission.zip";
        String URL = currentExercise.getReturnUrl() + "?api_version=7";

        zip(exerciseFolderToZip, submissionZipPath);
        HttpResult makePostWithFile = UrlCommunicator.makePostWithFile(new File(submissionZipPath), URL);

        String resultUrl = TmcJsonParser.getSubmissionUrl(makePostWithFile);
        new File(submissionZipPath).delete();
        return resultUrl;
    }
    
    public String submit(String currentPath) throws IOException {
        return submit(currentPath, getLastDirectoryFromPath(currentPath));
    }

    private Exercise findExercise(String currentPath, String exerciseName) {
        Course currentCourse = getCurrentCourse(currentPath);
        List<Exercise> exercisesForCurrentCourse = TmcJsonParser.getExercises(currentCourse.getId());
        Exercise currentExercise = findCurrentExercise(exercisesForCurrentCourse, exerciseName);
        return currentExercise;
    }

    private String getLastDirectoryFromPath(String currentPath) {
        String exerciseName;
        String[] directories = currentPath.split("/");
        exerciseName = directories[directories.length - 1];
        return exerciseName;
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
        for (Exercise exercise : exercisesForCurrentCourse) {
            if (exercise.getName().contains(exerciseName)) {
                return exercise;
            }
        }
        return null;
    }

    private Course getCurrentCourse(String directoryPath) {
        String[] exerciseName = getExerciseName(directoryPath);
        return getCurrentCourseByName(exerciseName);
    }

    private Course getCurrentCourseByName(String[] foldersPath) {
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
