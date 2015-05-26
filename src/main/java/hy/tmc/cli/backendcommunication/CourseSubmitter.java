package hy.tmc.cli.backendcommunication;

import hy.tmc.cli.configuration.ConfigHandler;
import hy.tmc.cli.domain.Course;
import hy.tmc.cli.domain.Exercise;

import hy.tmc.cli.zipping.ProjectRootFinder;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class CourseSubmitter {

    private ProjectRootFinder rootFinder;

    public CourseSubmitter(ProjectRootFinder rootFinder) {
        this.rootFinder = rootFinder;
    }

    public void submit(String currentPath, String exerciseName) {

        Course currentCourse = getCurrentCourse(currentPath);
        List<Exercise> exercisesForCurrentCourse = TmcJsonParser.getExercises(currentCourse.getId());
        Exercise currentExercise = findCurrentExercise(exercisesForCurrentCourse, exerciseName);
        System.out.println("CurrentEX " + currentExercise.getName());
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
