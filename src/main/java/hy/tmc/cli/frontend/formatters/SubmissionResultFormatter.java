
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

    public String someTestsFailed();
    public String testCaseDescription(TestCase testCase);
    public String allTestsPassed();
    public String viewModelSolution(String solutionUrl);
    public String getPointsInformation(SubmissionResult result);
    public String someScenariosFailed();
    public String parseValidationErrors(Map.Entry<String, List<ValidationError>> entry);
}
