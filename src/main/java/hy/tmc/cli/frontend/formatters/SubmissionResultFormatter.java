package hy.tmc.cli.frontend.formatters;

import fi.helsinki.cs.tmc.core.domain.submission.SubmissionResult;
import fi.helsinki.cs.tmc.core.domain.submission.TestCase;
import fi.helsinki.cs.tmc.core.domain.submission.ValidationError;

import java.util.List;
import java.util.Map;

/**
 * SubmissionResultFormatter interface makes easier to attach new frontends to core.
 * SubmissionResult is formatted with some class which implements this interface.
 */
public interface SubmissionResultFormatter {

    String someTestsFailed();

    String testCaseDescription(TestCase testCase);

    String allTestsPassed();

    String viewModelSolution(String solutionUrl);

    String getPointsInformation(SubmissionResult result);

    String someScenariosFailed();

    String parseValidationErrors(Map.Entry<String, List<ValidationError>> entry);
}
