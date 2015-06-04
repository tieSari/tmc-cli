package hy.tmc.cli.backend.communication;

import com.google.common.base.Optional;
import hy.tmc.cli.domain.submission.SubmissionResult;
import hy.tmc.cli.domain.submission.TestCase;
import hy.tmc.cli.domain.submission.ValidationError;
import static hy.tmc.cli.frontend.ColorFormatter.coloredString;
import static hy.tmc.cli.frontend.CommandLineColor.YELLOW;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class SubmissionInterpreter {

    /**
     * Seconds after which request times out.
     */
    private final int timeOut = 30;

    /**
     * Milliseconds to sleep between each poll attempt.
     */
    private final int pollInterval = 1000;

    /**
     * Returns a ready SubmissionResult with all fields complete after
     * processing.
     *
     * @param url url to make request to
     * @return SubmissionResult containing details of submission. Null if timed
     * out.
     * @throws InterruptedException if thread failed to sleep
     */
    private SubmissionResult pollSubmissionUrl(String url) throws InterruptedException {
        for (int i = 0; i < timeOut; i++) {
            SubmissionResult result = TmcJsonParser.getSubmissionResult(url);
            if (result.getStatus() == null || !result.getStatus().equals("processing")) {
                return result;
            }

            Thread.sleep(pollInterval);
        }
        return null;
    }

    /**
     * Organizes SubmissionResult into human-readable form.
     *
     * @param url String of url to poll results from.
     * @param detailed true for stack trace, always show successful.
     * @return a String containing human-readable information about tests.
     * @throws InterruptedException if thread was interrupted.
     */
    public String resultSummary(String url, boolean detailed) throws InterruptedException {
        SubmissionResult result = pollSubmissionUrl(url);
        return summarize(result, detailed);
    }

    private String summarize(SubmissionResult result, boolean detailed) {
        if (result.isAllTestsPassed()) {
            return buildSuccessMessage(result, detailed);
        } else {
            return "Some tests failed on server. Summary: \n"
                    + testCaseResults(result.getTestCases(), detailed)
                    + valgridErrors(result).or("")
                    + checkStyleErrors(result);
        }
    }

    private String checkStyleErrors(SubmissionResult result) {
        if (result.getValidations() == null) {
            return "";
        }
        StringBuilder builder = new StringBuilder();

        Map<String, List<ValidationError>> errors = result.getValidations().getValidationErrors();
        if (!errors.isEmpty()) {
            builder.append(coloredString("Some checkstyle scenarios failed.", YELLOW));
        }

        for (Entry<String, List<ValidationError>> entry : errors.entrySet()) {
            parseValidationErrors(builder, entry);
        }
        return builder.toString();
    }

    private void parseValidationErrors(StringBuilder builder, Entry<String, List<ValidationError>> entry) {
        builder.append("\nFile: ").append(entry.getKey());
        for (ValidationError error : entry.getValue()) {
            String errorLine = "\n  On line: " + error.getLine() + " Column: " + error.getColumn();
            builder.append(coloredString(errorLine, YELLOW));
            builder.append("\n    ").append(error.getMessage());
        }
    }

    private Optional<String> valgridErrors(SubmissionResult result) {
        return Optional.of(result.getValgrind());
    }

    private String buildSuccessMessage(SubmissionResult result, boolean detailed) {
        StringBuilder builder = new StringBuilder();
        builder.append("All tests passed. Points awarded: ")
                .append(Arrays.toString(result.getPoints()))
                .append("\n")
                .append(testCaseResults(result.getTestCases(), detailed))
                .append("View model solution: \n")
                .append(result.getSolutionUrl());
        return builder.toString();
    }

    private String testCaseResults(TestCase[] cases, boolean showSuccessful) {
        StringBuilder result = new StringBuilder();
        for (TestCase aCase : cases) {
            if (showSuccessful || !aCase.isSuccessful()) {
                result.append(failOrSuccess(aCase)).append("\n");
            }
        }
        return result.toString();
    }

    private String failOrSuccess(TestCase testCase) {
        if (testCase.isSuccessful()) {
            return "  PASSED: " + testCase.getName();
        }
        return "  FAILED: " + testCase.getName() + "\n  " + testCase.getMessage();
    }
}
