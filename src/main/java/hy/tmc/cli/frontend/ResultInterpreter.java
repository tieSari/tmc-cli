package hy.tmc.cli.frontend;

import fi.helsinki.cs.tmc.langs.RunResult;
import fi.helsinki.cs.tmc.langs.TestResult;

import java.util.ArrayList;
import java.util.List;


public class ResultInterpreter {

    public String interpret(RunResult result) {
        switch (result.status) {
            case PASSED: 
                return "All tests passed. You can now submit";
            case TESTS_FAILED: 
                return testFailureReport(result);
            case COMPILE_FAILED:
                return "Code did not compile.";
            case GENERIC_ERROR: 
                return "A generic error occured.";
            default:
                throw new IllegalArgumentException("bad argument");
        }
    }
    
    private String testFailureReport(RunResult result) {
        StringBuilder reportBuilder = new StringBuilder();
        reportBuilder.append("Some tests failed:\n");
    
        List<TestResult> passedTests = passedTests(result);
        List<TestResult> failedTests = failedTests(result);
        
        reportBuilder.append(passedTests.size()).append(" tests passed:\n");
        for (TestResult testResult : passedTests) {
            reportBuilder.append(testResult.name).append("\n");
        }
        reportBuilder.append(failedTests.size()).append(" tests failed:\n");
        for (TestResult testResult : failedTests) {
            reportBuilder.append(testResult.name)
                    .append(" failed: ")
                    .append(testResult.errorMessage)
                    .append("\n");
            reportBuilder.append(stackTrace(testResult)).append("\n");
        }
        return reportBuilder.toString();
    }
    
    private String stackTrace(TestResult testResult) {
        StringBuilder builder = new StringBuilder();
        for (String line : testResult.backtrace){
            builder.append(line).append("\n");
        }
        return builder.toString();
    }
    
    private List<TestResult> passedTests(RunResult result) {
        List<TestResult> passes = new ArrayList<>();
        for (TestResult test : result.testResults){
            if (test.passed) {
                passes.add(test);
            }
        }
        return passes;
    }
    
    private List<TestResult> failedTests(RunResult result) {
        List<TestResult> fails = new ArrayList<>();
        for (TestResult test : result.testResults){
            if (!test.passed) {
                fails.add(test);
            }
        }
        return fails;
    }
}
