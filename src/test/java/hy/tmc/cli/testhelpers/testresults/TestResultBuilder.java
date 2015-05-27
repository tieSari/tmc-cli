package hy.tmc.cli.testhelpers.testresults;

import com.google.common.collect.ImmutableList;
import fi.helsinki.cs.tmc.langs.TestResult;
import java.util.ArrayList;
import java.util.List;

public class TestResultBuilder {

    private String name;
    private boolean passed;
    private ArrayList<String> points;
    private String errorMessage;
    private ArrayList<String> stackTrace;

    public TestResultBuilder() {
        this.points = new ArrayList<>();
        this.stackTrace = new ArrayList<>();
        this.name = "";
        this.errorMessage = "";
    }

    public TestResultBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public TestResultBuilder withPassedStatus(boolean passed) {
        this.passed = passed;
        return this;
    }

    public TestResultBuilder withPoint(String point) {
        this.points.add(point);
        return this;
    }
    
    public TestResultBuilder withErrorMessage(String errMsg){
        this.errorMessage = errMsg;
        return this;
    }

    public TestResultBuilder withPoints(List<String> points) {
        this.points.addAll(points);
        return this;
    }

    public TestResultBuilder withStackTrace(List<String> trace) {
        this.stackTrace.addAll(trace);
        return this;
    }

    public TestResultBuilder withStackTraceLine(String line) {
        this.stackTrace.add(line);
        return this;
    }

    public TestResult build() {
        return new TestResult(name, passed, ImmutableList.copyOf(points), errorMessage,
                ImmutableList.copyOf(stackTrace));
    }

    public void clear() {
        this.stackTrace.clear();
        this.points.clear();
        this.errorMessage = "";
        this.name = "";
        this.passed = false;
    }
}
