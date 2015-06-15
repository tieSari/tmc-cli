package hy.tmc.cli.testhelpers;

import com.google.common.base.Optional;
import hy.tmc.cli.domain.Course;
import hy.tmc.cli.zipping.RootFinder;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ProjectRootFinderStub implements RootFinder {

    private String returnValue;

    public ProjectRootFinderStub() {
        this.returnValue = "";
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
        return Optional.absent();
    }

}
