package hy.tmc.cli.backend.communication;

import hy.tmc.cli.frontend.communication.server.ProtocolException;
import hy.tmc.cli.frontend.formatters.DefaultSubmissionResultFormatter;
import hy.tmc.cli.testhelpers.builders.SubmissionResultBuilder;
import hy.tmc.cli.testhelpers.builders.TestCaseBuilder;
import hy.tmc.core.domain.submission.SubmissionResult;
import hy.tmc.core.domain.submission.TestCase;
import hy.tmc.core.domain.submission.ValidationError;
import java.io.IOException;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

public class SubmissionInterpreterTest {

    SubmissionInterpreter submissionInterpreter;
    SubmissionResultBuilder builder = new SubmissionResultBuilder();
    TestCaseBuilder testBuilder = new TestCaseBuilder();
    String successful = "week1-010.CircleCircumference";
    String failed = "week1-018.GradesAndPoints";
    String checkstyleFail = "week1-013.NhlStatisticsPart1";
    String checkstyleSuccess = "week1-014.PositiveValue";
    String valgrindFail = "week1-021.LeapYear";
    String valgrindSuccess = "week1-016.EvenOrOdd";
    String modelSolutionUrl = "https://tmc.test.mooc.fi/oopprogramming/solutions/7";

    @Before
    public void setup() {
        submissionInterpreter = new SubmissionInterpreter(new DefaultSubmissionResultFormatter());
    }

    private ValidationError validationError(int line, int column, String message, String sourceName) {
        ValidationError error = new ValidationError();
        error.setLine(line);
        error.setColumn(column);
        error.setMessage(message);
        error.setSourceName(sourceName);
        return error;
    }

    private SubmissionResult createFailedSubmission() {
        TestCase fail1 = testBuilder.withName("testChristmasTree")
                .withMessage("Expected 5 lines, there were 6").build();
        TestCase fail2 = testBuilder.withName("testMath")
                .withMessage("expected 5+3=8, got 5+3=0").withDetailedMessage("details").build();
        TestCase pass = testBuilder.withName("testPizza").asSuccessfull().build();

        SubmissionResult result = builder
                .withStatus(SubmissionResult.Status.FAIL)
                .withPoints("3.1", "3.2")
                .withTestCase(fail1)
                .withTestCase(pass)
                .withTestCase(fail2).build();
        return result;
    }

    private SubmissionResult createSuccessfullSubmission() {
        SubmissionResult result = builder
                .withExerciseName(this.successful)
                .withPoints("1.1", "1.2", "2.0")
                .withAllTestsPassed()
                .withTestCase(testBuilder.asSuccessfull().withName("e-z-test").build())
                .withTestCase(testBuilder.asSuccessfull().withName("asdfTest").build())
                .withSolutionUrl(this.modelSolutionUrl)
                .build();
        return result;
    }

    private SubmissionResult createSuccesfulSubmissionWithCheckstyle() {
        SubmissionResult result = builder
                .withExerciseName(this.checkstyleSuccess)
                .withAllTestsPassed()
                .withValidations(null)
                .build();
        return result;
    }

    private SubmissionResult createCheckstyleFailingSubmission() {
        SubmissionResult result = builder
                .withExerciseName(this.checkstyleFail)
                .withValidationError("A.java",
                        validationError(202, 18, "Class length is 478 lines (max allowed is 300)", ""))
                .withValidationError("B.java",
                        validationError(421, 24, "',' is not followed by whitespace.", ""))
                .build();
        return result;
    }

    @Test
    public void passedResultOutputsPassed() {
        SubmissionResult result = createSuccessfullSubmission();
        String output = submissionInterpreter.summary(result, false);
        assertTrue(output.contains("All tests passed"));
    }

    @Test
    public void passedResultShowsPoints() {
        SubmissionResult result = createSuccessfullSubmission();
        String output = submissionInterpreter.summary(result, false);
        assertTrue(output.contains("Points awarded:"));
        assertTrue(output.contains("[1.1, 1.2, 2.0]"));
    }

    @Test
    public void passedResultShowsModelSolution() {
        SubmissionResult result = createSuccessfullSubmission();
        String output = submissionInterpreter.summary(result, false);
        assertTrue(output.contains("View model solution:"));
        assertTrue(output.contains(modelSolutionUrl));
    }

    @Test
    public void failedResultOutputsFailed() {
        String output = submissionInterpreter.summary(createFailedSubmission(), false);
        assertTrue(output.contains("failed"));
        assertTrue(output.contains("FAILED: \u001B[0mtestMath"));
    }

    @Test
    public void failedResultDoesNotShowModelSolutionUrl() {
        String output = submissionInterpreter.summary(createFailedSubmission(), false);
        assertFalse(output.contains(modelSolutionUrl));
    }

    @Test
    public void failedResultOutputContainsFailedMessages() {
        String output = submissionInterpreter.summary(createFailedSubmission(), false);
        assertTrue(output.contains("Expected 5 lines, there were 6"));
    }

    @Test
    public void succesfulResultOutputContainsPassedTestsIfDetailedOn() {
        SubmissionResult result = createSuccessfullSubmission();

        String output = submissionInterpreter.summary(result, true);
        assertTrue(output.contains("PASSED"));
        assertTrue(output.contains("e-z-test"));
        assertTrue(output.contains("asdfTest"));

    }

    @Test
    public void successfulResultOutputDoesntContainPassedTestsIfDetailedOff() {
        String output = submissionInterpreter.summary(createSuccessfullSubmission(), false);
        assertFalse(output.contains("PASSED"));
        assertFalse(output.contains("e-z-test"));
        assertFalse(output.contains("asdfTest"));
    }

    @Test
    public void resultWithCheckstyleContainsCheckstyleErrors() {
        SubmissionResult result = createCheckstyleFailingSubmission();
        String output = submissionInterpreter.summary(result, true);

        assertTrue(output.contains("checkstyle"));
        assertTrue(output.contains("Class length is 478 lines (max allowed is 300)"));
        assertTrue(output.contains("',' is not followed by whitespace."));
    }

    @Test
    public void resultWithCheckstyleContainsLineNumberMarkings() {
        SubmissionResult result = createCheckstyleFailingSubmission();
        String output = submissionInterpreter.summary(result, true);

        assertTrue(output.contains("On line: 421 Column: 24"));
        assertTrue(output.contains("On line: 202 Column: 18"));
    }

    @Test
    public void resultWithCheckstyleContainsFileNames() {
        SubmissionResult result = createCheckstyleFailingSubmission();

        String output = submissionInterpreter.summary(result, true);

        assertTrue(output.contains("File: A.java"));
        assertTrue(output.contains("File: B.java"));
    }

    @Test
    public void resultWithNoCheckstyleDoesntContainCheckstyleErrors() throws InterruptedException, IOException, ProtocolException {
        SubmissionResult result = createSuccessfullSubmission();

        String output = submissionInterpreter.summary(result, true);
        assertFalse(output.contains("checkstyle"));
    }
}
