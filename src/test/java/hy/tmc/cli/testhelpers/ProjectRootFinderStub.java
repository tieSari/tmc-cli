package hy.tmc.cli.testhelpers;

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
    public Path getRootDirectory(Path zipRoot) {
        return Paths.get(returnValue);
    }

}
