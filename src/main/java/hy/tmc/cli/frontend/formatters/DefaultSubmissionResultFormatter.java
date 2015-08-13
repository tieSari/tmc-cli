package hy.tmc.cli.frontend.formatters;

import fi.helsinki.cs.tmc.core.domain.submission.SubmissionResult;
import fi.helsinki.cs.tmc.core.domain.submission.TestCase;
import fi.helsinki.cs.tmc.core.domain.submission.ValidationError;

import java.util.List;
import java.util.Map.Entry;

import static hy.tmc.cli.frontend.ColorFormatter.coloredString;
import static hy.tmc.cli.frontend.CommandLineColor.BLUE;
import static hy.tmc.cli.frontend.CommandLineColor.GREEN;
import static hy.tmc.cli.frontend.CommandLineColor.RED;
import static hy.tmc.cli.frontend.CommandLineColor.YELLOW;

/**
 * CommandLineSubmissionResultFormatter gives submissionresult explainings for command line user
 * interface. ResultInterpreter class uses this class.
 */
public class DefaultSubmissionResultFormatter implements SubmissionResultFormatter {

    private CheckstyleFormatter checkstyleFormatter;

    public DefaultSubmissionResultFormatter() {
        this.checkstyleFormatter = new DefaultCheckstyleFormatter();
    }

    @Override
    public String someTestsFailed() {
        return "Some tests failed on server. Summary: \n";
    }

    /**
     * Tells if TestCase object is passed or failed, if failed, also information about failures.
     *
     * @param testCase
     * @return toString of StrinBuilder content
     */
    @Override
    public String testCaseDescription(TestCase testCase) {
        StringBuilder destination = new StringBuilder();
        if (testCase.isSuccessful()) {
            destination.append(coloredString(" PASSED: ", GREEN)).append(testCase.getName())
                .append("\n");
            return destination.toString();
        }
        destination.append(coloredString(" FAILED: ", RED)).append(testCase.getName()).append(" ")
            .append(testCase.getMessage()).append("\n");
        return destination.toString();
    }

    @Override
    public String allTestsPassed() {
        return coloredString("All tests passed. Points awarded: ", GREEN);
    }

    /**
     * Gives information about model solution.
     */
    @Override
    public String viewModelSolution(String solutionUrl) {
        return "View model solution: \n" + coloredString(solutionUrl, BLUE);
    }

    /**
     * Gives information about result points.
     */
    @Override
    public String getPointsInformation(SubmissionResult result) {
        return result.getPoints().toString() + "\n";
    }

    @Override
    public String someScenariosFailed() {
        return coloredString("Some checkstyle scenarios failed.", YELLOW);
    }

    @Override
    public String parseValidationErrors(Entry<String, List<ValidationError>> entry) {
        return this.checkstyleFormatter.checkstyleErrors(entry.getKey(), entry.getValue());
    }
}
