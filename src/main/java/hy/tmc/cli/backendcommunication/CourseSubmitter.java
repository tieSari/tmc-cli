package hy.tmc.cli.backendcommunication;

import hy.tmc.cli.domain.Course;
import hy.tmc.cli.domain.Exercise;

import hy.tmc.cli.zipping.ProjectRootFinder;
import hy.tmc.cli.zipping.Zipper;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.lingala.zip4j.exception.ZipException;

public class CourseSubmitter {

    private ProjectRootFinder rootFinder;

    public CourseSubmitter(ProjectRootFinder rootFinder) {
        this.rootFinder = rootFinder;
    }

    public void submit(String currentPath, String exerciseName) {
        Course currentCourse = getCurrentCourse(currentPath);
        List<Exercise> exercisesForCurrentCourse = TmcJsonParser.getExercises(currentCourse.getId());
        Exercise currentExercise = findCurrentExercise(exercisesForCurrentCourse, exerciseName);
        
        String exerciseFolderToZip = currentPath + "/" + exerciseName;
        String destinationFolder = currentPath;
        String submissionZipPath = currentPath+"/submission.zip";
        System.out.println("exercise path:  " + exerciseFolderToZip);
        System.out.println("submission path:  " + submissionZipPath);
        zip(exerciseFolderToZip, submissionZipPath);
        System.out.println("LÖYTYY: " + new File(submissionZipPath).exists());
        //new File(submissionZipPath).delete();
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
