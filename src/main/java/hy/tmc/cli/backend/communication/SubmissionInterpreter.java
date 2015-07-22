package hy.tmc.cli.backend.communication;

import com.google.common.base.Optional;


import hy.tmc.cli.frontend.formatters.SubmissionResultFormatter;
import hy.tmc.core.domain.submission.SubmissionResult;
import hy.tmc.core.domain.submission.TestCase;
import hy.tmc.core.domain.submission.ValidationError;


import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class SubmissionInterpreter {

    private final SubmissionResultFormatter formatter;
    
    public SubmissionInterpreter(SubmissionResultFormatter formatter) {
        this.formatter = formatter;
    }

    /**
     * Organizes SubmissionResult into human-readable form.
     *
     * @param detailed true for stack trace, always show successful.
     * @return a String containing human-readable information about tests. TimeOutMessage
    if result is null.
     */
    public String summary(SubmissionResult result, boolean detailed) {
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
            builder.append(formatter.someScenariosFailed());
        }

        for (Entry<String, List<ValidationError>> entry : errors.entrySet()) {
            parseValidationErrors(builder, entry);
        }
        return builder.toString();
    }

    private void parseValidationErrors(StringBuilder builder,
                                       Entry<String, List<ValidationError>> entry) {
        builder.append(formatter.parseValidationErrors(entry));
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

    private String testCaseResults(List<TestCase> cases, boolean showSuccessful) {
        StringBuilder result = new StringBuilder();
        for (TestCase testCase : cases) {
            if (showSuccessful || !testCase.isSuccessful()) {
                result.append(failOrSuccess(testCase));
            }
        }
        return result.toString();
    }

    private String failOrSuccess(TestCase testCase) {
        return formatter.testCaseDescription(testCase);
    }
}
