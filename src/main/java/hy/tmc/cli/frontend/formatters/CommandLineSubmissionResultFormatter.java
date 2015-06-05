package hy.tmc.cli.frontend.formatters;

import hy.tmc.cli.domain.submission.SubmissionResult;
import hy.tmc.cli.domain.submission.TestCase;
import java.util.Arrays;

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
        StringBuilder des = new StringBuilder();
        if (testCase.isSuccessful()) {
            des.append(" PASSED: ").append(testCase.getName());
        }
        des.append(" FAILED: ").append(testCase.getName()).append("\n  ").append(testCase.getMessage());
        return des.toString();
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
}
