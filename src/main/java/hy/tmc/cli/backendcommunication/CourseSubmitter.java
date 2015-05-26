package hy.tmc.cli.backendcommunication;

import hy.tmc.cli.configuration.ConfigHandler;
import hy.tmc.cli.domain.Course;

import hy.tmc.cli.zipping.DefaultRootDetector;
import hy.tmc.cli.zipping.ProjectRootFinder;
import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class CourseSubmitter {

    private ProjectRootFinder rootFinder;

    public CourseSubmitter(ProjectRootFinder rootFinder) {
        this.rootFinder = rootFinder;
    }

    public void submit(String currentPath, String exerciseName) {
        new ConfigHandler().readCoursesAddress();
    }

    public Course getCurrentCourse(String directoryPath) {
        String exerciseName = getExerciseName(directoryPath);
        return getCurrentCourseByName(exerciseName);
    }

    private Course getCurrentCourseByName(String exerciseName) {
        List<Course> courses = TmcJsonParser.getCourses();
        Course currentCourse = null;
        for (Course course : courses) {
            if (course.getName().equals(exerciseName)) {
                currentCourse = course;
            }
        }
        assert currentCourse == null;
        return currentCourse;
    }

    public String getExerciseName(String directoryPath) {
        Path path = rootFinder.getRootDirectory(Paths.get(directoryPath));
        String[] table = path.toString().split("/");
        return table[table.length - 2];
    }

}
