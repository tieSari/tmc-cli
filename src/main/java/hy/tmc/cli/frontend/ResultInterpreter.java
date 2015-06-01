package hy.tmc.cli.frontend;

import static hy.tmc.cli.frontend.ColorFormatter.coloredString;
import static hy.tmc.cli.frontend.CommandLineColor.GREEN;
import static hy.tmc.cli.frontend.CommandLineColor.RED;
import static hy.tmc.cli.frontend.CommandLineColor.WHITE;

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
     * Transform the RunResult given to this interpreter in its constructor into a human
     * readable output.
     * 
     * @return a String representation of a RunResult 
     */
    public String interpret() {
        switch (result.status) {
          case PASSED:
              return allPassedOutput();
          case TESTS_FAILED:
              return testFailureReport();
          case COMPILE_FAILED:
              return compileErrorOutput();
          case GENERIC_ERROR:
              return genericErrorOutput();
          default:
              throw new IllegalArgumentException("bad argument");
        }
    }
    
    private String genericErrorOutput() {
        return ColorFormatter.coloredString("Failed due to an internal error", RED, WHITE);
    }
    
    private String compileErrorOutput() {
        return ColorFormatter.coloredString("Code did not compile.", RED, WHITE);
    }
    
    private String allPassedOutput() {
        return ColorFormatter.coloredString("All tests passed.", GREEN) + " You can now submit";
    }

    private String testFailureReport() {
        StringBuilder reportBuilder = new StringBuilder();
        reportBuilder.append("Some tests failed:\n");

        succesfulTests(reportBuilder);
        failedTests(reportBuilder);

        return reportBuilder.toString();
    }

    private void succesfulTests(StringBuilder builder) {
        List<TestResult> passedTests = getPassedTests();
        if (passedTests.isEmpty()) {
            builder.append("No tests passed.\n");
            return;
        }
        String passes = passedTests.size() + " tests passed:\n";
        builder.append(coloredString(passes, GREEN));
        for (TestResult testResult : passedTests) {
            builder.append(testPadding).append(testResult.name).append("\n");
        }
    }

    private void failedTests(StringBuilder builder) {
        List<TestResult> failures = getFailedTests();
        String fails = failures.size() + " tests failed:\n";
        builder.append(coloredString(fails, RED));
        for (TestResult testResult : failures) {
            failedTestOutput(builder, testResult);
        }
    }

    private void failedTestOutput(StringBuilder builder, TestResult testResult) {
        builder.append(testPadding);
        builder.append(testResult.name)
                .append(" failed: ")
                .append(testResult.errorMessage)
                .append("\n");
        builder.append(stackTrace(testResult)).append("\n");
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
