package hy.tmc.cli.backend.communication;

import hy.tmc.cli.domain.submission.FeedbackQuestion;
import hy.tmc.cli.domain.submission.SubmissionResult;
import hy.tmc.cli.domain.submission.TestCase;

import java.util.Arrays;
import java.util.List;

public class SubmissionInterpreter {

    /**
     * Seconds after which request times out.
     */
    private final int timeOut = 30;

    /**
     * Milliseconds to sleep between each poll attempt.
     */
    private final int pollInterval = 1000;

    private SubmissionResult latestResult;


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
            if (!result.getStatus().equals("processing")) {
                return result;
            }

            Thread.sleep(pollInterval);
        }
        return null;
    }

    /**
     * Returns feedback questions from the latest submission result.
     */
    public List<FeedbackQuestion> getFeedbackQuestions() {
        return latestResult.getFeedbackQuestions();
    }

    /**
     * Organizes SubmissionResult into human-readable form.
     *
     * @param detailed true for stack trace, always show successful.
     * @return a String containing human-readable information about tests.
     * @throws InterruptedException if thread was interrupted.
     */
    public String resultSummary(boolean detailed) throws InterruptedException {
        return summarize(latestResult, detailed);
    }

    /**
     * Get a new submissionResult. This will update the classes state so that calls to methods
     * like resultSummary will be based on the submissionResult fetched by this method.
     *
     * @param url the submission url
     */
    public SubmissionResult getSubmissionResult(String url) throws InterruptedException {
        latestResult = pollSubmissionUrl(url);
        return latestResult;
    }

    private String summarize(SubmissionResult result, boolean detailed) {
        if (result.isAllTestsPassed()) {
            return buildSuccessMessage(result, detailed);
        } else {
            return "Some tests failed on server. Summary: \n"
                    + testCaseResults(result.getTestCases(), detailed);
        }
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
