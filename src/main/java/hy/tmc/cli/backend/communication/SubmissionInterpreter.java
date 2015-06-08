package hy.tmc.cli.backend.communication;

import com.google.common.base.Optional;
import hy.tmc.cli.domain.submission.SubmissionResult;
import hy.tmc.cli.domain.submission.TestCase;
import hy.tmc.cli.domain.submission.ValidationError;
import static hy.tmc.cli.frontend.ColorFormatter.coloredString;
import static hy.tmc.cli.frontend.CommandLineColor.YELLOW;
import hy.tmc.cli.frontend.formatters.CommandLineSubmissionResultFormatter;
import hy.tmc.cli.frontend.formatters.SubmissionResultFormatter;

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
    
    private final String timeOutmessage = "Something went wrong. Please check your internet connection.";
    
    private final SubmissionResultFormatter formatter = new CommandLineSubmissionResultFormatter();

    /**
     * Returns a ready SubmissionResult with all fields complete after
     * processing.
     *
     * @param url url to make request to
     * @return SubmissionResult containing details of submission. Null if timed
     * out.
     * @throws InterruptedException if thread failed to sleep
     */
    private Optional<SubmissionResult> pollSubmissionUrl(String url) throws InterruptedException {
        for (int i = 0; i < timeOut; i++) {
            SubmissionResult result = TmcJsonParser.getSubmissionResult(url);
            if (result.getStatus() == null || !result.getStatus().equals("processing")) {
                return Optional.of(result);
            }
            Thread.sleep(pollInterval);
        }
        return Optional.absent();
    }

    /**
     * Organizes SubmissionResult into human-readable form.
     *
     * @param url String of url to poll results from.
     * @param detailed true for stack trace, always show successful.
     * @return a String containing human-readable information about tests. TimeOutMessage
     * if result is null.
     * @throws InterruptedException if thread was interrupted.
     */
    public String resultSummary(String url, boolean detailed) throws InterruptedException {
        Optional<SubmissionResult> result = pollSubmissionUrl(url);
        if (result.isPresent()) {
            return summarize(result.get(), detailed);
        }
        return timeOutmessage;
    }

    private String summarize(SubmissionResult result, boolean detailed) {
        if (result.isAllTestsPassed()) {
            return buildSuccessMessage(result, detailed);
        } else {
            return formatter.someTestsFailed()
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
        builder.append(formatter.allTestsPassed())
                .append(formatter.getPointsInformation(result))
                .append(testCaseResults(result.getTestCases(), detailed))
                .append(formatter.viewModelSolution(result.getSolutionUrl()));
        return builder.toString();
    }

    private String testCaseResults(TestCase[] cases, boolean showSuccessful) {
        StringBuilder result = new StringBuilder();
        for (TestCase aCase : cases) {
            if (showSuccessful || !aCase.isSuccessful()) {
                result.append(failOrSuccess(aCase));
            }
        }
        return result.toString();
    }

    private String failOrSuccess(TestCase testCase) {
        return formatter.testCaseDescription(testCase);
    }
}
