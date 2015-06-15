package hy.tmc.cli.frontend.formatters;

import static hy.tmc.cli.frontend.ColorFormatter.coloredString;
import static hy.tmc.cli.frontend.CommandLineColor.YELLOW;

import hy.tmc.cli.domain.submission.SubmissionResult;
import hy.tmc.cli.domain.submission.TestCase;
import hy.tmc.cli.domain.submission.ValidationError;

import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;

/**
 * CommandLineSubmissionResultFormatter gives submissionresult explainings for command line user interface.
 * ResultInterpreter class uses this class.
 */
public class CommandLineSubmissionResultFormatter implements SubmissionResultFormatter {

    @Override
    public String someTestsFailed() {
        return "Some tests failed on server. Summary: \n";
    }

    /**
     * Tells if TestCase object is passed or failed, if failed, also information about failures.
     * @param testCase 
     * @return toString of StrinBuilder content
     */
    @Override
    public String testCaseDescription(TestCase testCase) {
        StringBuilder destination = new StringBuilder();
        if (testCase.isSuccessful()) {
<<<<<<< HEAD
            destination.append(" PASSED: ").append(testCase.getName());
            return destination.toString();
=======
            des.append(" PASSED: ").append(testCase.getName());
            return des.toString();
>>>>>>> b4daca84b62f91a2ea5ea563447fb900c9db9f5a
        }
        destination.append(" FAILED: ").append(testCase.getName()).append("\n  ").append(testCase.getMessage());
        return destination.toString();
    }

    @Override
    public String allTestsPassed() {
        return "All tests passed. Points awarded: ";
    }

    /**
     * Gives information about model solution.
     */
    @Override
    public String viewModelSolution(String solutionUrl) {
        return "View model solution: \n" + solutionUrl;
    }

    /**
     * Gives information about result points. 
     */
    @Override
    public String getPointsInformation(SubmissionResult result) {
        return Arrays.toString(result.getPoints()) + "\n";
    }

    @Override
    public String someScenariosFailed() {
        return coloredString("Some checkstyle scenarios failed.", YELLOW);
    }

    @Override
    public String parseValidationErrors(Entry<String, List<ValidationError>> entry) {
        StringBuilder builder = new StringBuilder();
        builder.append("\nFile: ").append(entry.getKey());
        for (ValidationError error : entry.getValue()) {
            String errorLine = "\n  On line: " + error.getLine() + " Column: " + error.getColumn();
            builder.append(coloredString(errorLine, YELLOW));
            builder.append("\n    ").append(error.getMessage());
        }
        return builder.toString();
    }
}
