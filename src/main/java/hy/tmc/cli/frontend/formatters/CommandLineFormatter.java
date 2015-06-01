package hy.tmc.cli.frontend.formatters;

import fi.helsinki.cs.tmc.langs.RunResult;
import fi.helsinki.cs.tmc.langs.TestResult;

import java.util.List;


public class CommandLineFormatter implements ResultFormatter {

    private String testPadding;
    private String stackTracePadding;
    
    /**
     * CommandLineFormatter gives result explainings for command line user interface. 
     * ResultInterpreter class uses this class. 
     */
    public CommandLineFormatter() {
        this.testPadding = "  ";
        this.stackTracePadding = testPadding + " ";
    }
    
    /**
     * Interprets status of RunResult.
     * @param result RunResult
     * @return status
     */
    @Override
    public String interpretStatus(RunResult result) {
        switch (result.status) {
          case PASSED:
              return "All tests passed. You can now submit";
          case COMPILE_FAILED:
              return "Code did not compile.";
          case GENERIC_ERROR:
              return "Failed to run tests.";
          default:
              throw new IllegalArgumentException("bad argument");
        }
    }

    /**
     * Some tests failed -explanation
     * @return 
     */
    @Override
    public String someTestsFailed() {
        return "Some tests failed:\n";
    }

    /**
     * No tests passed -explanation
     * @return 
     */
    @Override
    public String noTestsPassed() {
        return "No tests passed.\n";
    }

    /**
     * How much tests passed.
     * @param amount
     * @return 
     */
    @Override
    public String howMuchTestsPassed(int amount) {
        return (amount + " tests passed:\n");
    }

    /**
     * Get explanation of passed tests
     * @param passed
     * @return 
     */
    @Override
    public String getPassedTests(List<TestResult> passed) {
        StringBuilder passedList = new StringBuilder();
        for (TestResult testResult : passed) {
            passedList.append(testPadding).append(testResult.name).append("\n");
        }
        return passedList.toString();
    }

    /**
     * Get explanation of a single failed testResult. Expects that result is failed.
     * @param failed
     * @return 
     */
    @Override
    public String getFailedTestOutput(TestResult failed) {
        StringBuilder output = new StringBuilder();
        output.append(testPadding);
        output.append(failed.name)
                .append(" failed: ")
                .append(failed.errorMessage)
                .append("\n");
        return output.toString();
    }

    /**
     * Get stack trace of test result.
     * @param result
     * @return 
     */
    @Override
    public String getStackTrace(TestResult result) {
        StringBuilder builder = new StringBuilder();
        for (String line : result.backtrace) {
            builder.append(stackTracePadding).append(line).append("\n");
        }
        return builder.toString();
    }

    /**
     * Get how much tests failed.
     * @param amount
     * @return 
     */
    @Override
    public String howMuchTestsFailed(int amount) {
        return (amount + " tests failed:\n");
    }

}
