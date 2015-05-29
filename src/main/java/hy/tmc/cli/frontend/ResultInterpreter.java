package hy.tmc.cli.frontend;

import fi.helsinki.cs.tmc.langs.RunResult;
import fi.helsinki.cs.tmc.langs.TestResult;

import java.util.ArrayList;
import java.util.List;

public class ResultInterpreter {

    private final RunResult result;
    private final String testPadding = "  ";
    private final String stackTracePadding = testPadding + " ";

    public ResultInterpreter(RunResult result) {
        this.result = result;
    }

    /**
     * Param showStackTrace defaults to false.
     * @see interpet(boolean showStackTrace)
     */
    public String interpret() {
        return this.interpret(false);
    }

    /**
     * Transform the RunResult given to this interpreter in its constructor into a human readable
     * output.
     *
     * @param showStackTrace set if stacktraces are shown for failing tests.
     * @return a String representation of a RunResult
     */
    public String interpret(boolean showStackTrace) {
        switch (result.status) {
            case PASSED:
                return "All tests passed. You can now submit";
            case TESTS_FAILED:
                return testFailureReport(showStackTrace);
            case COMPILE_FAILED:
                return "Code did not compile.";
            case GENERIC_ERROR:
                return "Failed to run tests.";
            default:
                throw new IllegalArgumentException("bad argument");
        }
    }

    private String testFailureReport(boolean showStackTrace) {
        StringBuilder reportBuilder = new StringBuilder();
        reportBuilder.append("Some tests failed:\n");

        succesfulTests(reportBuilder);
        failedTests(reportBuilder, showStackTrace);

        return reportBuilder.toString();
    }

    private void succesfulTests(StringBuilder builder) {
        List<TestResult> passedTests = getPassedTests();
        if (passedTests.isEmpty()) {
            builder.append("No tests passed.\n");
            return;
        }
        builder.append(passedTests.size()).append(" tests passed:\n");
        for (TestResult testResult : passedTests) {
            builder.append(testPadding).append(testResult.name).append("\n");
        }
    }

    private void failedTests(StringBuilder builder, boolean showStackTrace) {
        List<TestResult> failures = getFailedTests();
        builder.append(failures.size()).append(" tests failed:\n");
        for (TestResult testResult : failures) {
            failedTestOutput(builder, testResult, showStackTrace);
        }
    }

    private void failedTestOutput(StringBuilder builder, TestResult testResult,
            boolean showStackTrace) {
        builder.append(testPadding);
        builder.append(testResult.name)
                .append(" failed: ")
                .append(testResult.errorMessage)
                .append("\n");
        if (showStackTrace) {
            builder.append(stackTrace(testResult)).append("\n");
        }
    }

    private String stackTrace(TestResult testResult) {
        StringBuilder builder = new StringBuilder();
        for (String line : testResult.backtrace) {
            builder.append(stackTracePadding).append(line).append("\n");
        }
        return builder.toString();
    }

    private List<TestResult> getPassedTests() {
        List<TestResult> passes = new ArrayList<>();
        for (TestResult test : result.testResults) {
            if (test.passed) {
                passes.add(test);
            }
        }
        return passes;
    }

    private List<TestResult> getFailedTests() {
        List<TestResult> fails = new ArrayList<>();
        for (TestResult test : result.testResults) {
            if (!test.passed) {
                fails.add(test);
            }
        }
        return fails;
    }
}
