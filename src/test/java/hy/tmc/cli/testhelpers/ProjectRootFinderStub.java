package hy.tmc.cli.testhelpers;

import com.google.common.base.Optional;
import hy.tmc.cli.backend.communication.TmcJsonParser;
import hy.tmc.cli.domain.Course;
import hy.tmc.cli.zipping.RootFinder;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;

public class ProjectRootFinderStub implements RootFinder {

    private String returnValue;
    private HashMap<String, Course> courseStubs;

    public ProjectRootFinderStub() {
        this.returnValue = "";
        courseStubs = new HashMap<>();
        fillCourseStubs();
    }

    private void fillCourseStubs() {
        String allCourses = ExampleJson.allCoursesExample;
        List<Course> courses = TmcJsonParser.getCoursesFromString(allCourses);
        for (Course c:courses) {
            courseStubs.put(c.getName(), c);
        }
    }

    public String getReturnValue() {
        return returnValue;
    }

    public void setReturnValue(String returnValue) {
        this.returnValue = returnValue;
    }

    @Override
    public Optional<Path> getRootDirectory(Path zipRoot) {
        return Optional.of(Paths.get(returnValue));
    }

    public Optional<Course> getCurrentCourse(String path) {
        String[] folders = path.split("/");

        for(String folder:folders) {
            if(courseStubs.containsKey(folder)) {
                return Optional.of(courseStubs.get(folder));
            }
        }
        return Optional.absent();
    }

}
