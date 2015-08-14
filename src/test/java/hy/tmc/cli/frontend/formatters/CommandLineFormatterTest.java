package hy.tmc.cli.frontend.formatters;

import fi.helsinki.cs.tmc.langs.domain.RunResult;
import fi.helsinki.cs.tmc.langs.domain.TestResult;

import hy.tmc.cli.testhelpers.builders.RunResultBuilder;
import hy.tmc.cli.testhelpers.builders.TestResultFactory;
import org.junit.Test;

import java.util.List;

import static fi.helsinki.cs.tmc.langs.domain.RunResult.Status.COMPILE_FAILED;
import static fi.helsinki.cs.tmc.langs.domain.RunResult.Status.GENERIC_ERROR;
import static fi.helsinki.cs.tmc.langs.domain.RunResult.Status.PASSED;
import static fi.helsinki.cs.tmc.langs.domain.RunResult.Status.TESTS_FAILED;

import static org.junit.Assert.assertTrue;

public class CommandLineFormatterTest {

    RunResult allPassed;
    RunResult allFailed;
    RunResult someFailed;
    RunResult compileError;
    RunResult genericError;
    RunResultBuilder builder;
    DefaultTestResultFormatter formatter;
    List<TestResult> passed;

    public CommandLineFormatterTest() {

        allPassed = new RunResultBuilder().withStatus(PASSED).build();
        compileError = new RunResultBuilder().withStatus(COMPILE_FAILED).build();
        genericError = new RunResultBuilder().withStatus(GENERIC_ERROR).build();
        allFailed = new RunResultBuilder().withStatus(TESTS_FAILED)
            .withTests(TestResultFactory.failedTests()).build();
        formatter = new DefaultTestResultFormatter();
        passed = TestResultFactory.passedTests();
    }

    @Test
    public void testInterpretStatus() {
        String explanation = formatter.interpretStatus(allPassed);
        assertTrue(explanation.contains("\u001B[32mAll tests passed.\u001B[0m You can now submit"));
    }

    @Test
    public void interpretGenericError() {
        String explanation = formatter.interpretStatus(genericError);
        assertTrue(explanation.contains("Failed due to an internal error"));
    }

    @Test
    public void someTestsFailed() {
        String explanation = formatter.someTestsFailed();
        assertTrue(explanation.contains("Some tests failed:\n"));
    }

    @Test
    public void noTestsPassed() {
        String explanation = formatter.noTestsPassed();
        assertTrue(explanation.contains("No tests passed.\n"));
    }

    @Test
    public void testHowMuchTestsPassed() {
        String explanation = formatter.howMuchTestsPassed(5);
        assertTrue(explanation.contains("5 tests passed:\n"));
    }

    @Test
    public void testHowMuchTestsFailed() {
        String explanation = formatter.howMuchTestsFailed(5);
        assertTrue(explanation.contains("\u001B"));
    }

    @Test
    public void testPassedTests() {
        String explanation = formatter.getPassedTests(passed);
        String shouldBe = "  Muuttujat testaaKanat";
        assertTrue(explanation.contains(shouldBe));
    }

    @Test
    public void compileFailedMessageIsRight() {
        String interpretStatus = formatter.interpretStatus(compileError);
        assertTrue(interpretStatus.contains("Code did not compile."));
    }

}
