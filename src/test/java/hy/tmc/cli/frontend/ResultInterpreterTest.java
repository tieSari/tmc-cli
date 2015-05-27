package hy.tmc.cli.frontend;

import fi.helsinki.cs.tmc.langs.RunResult;
import static fi.helsinki.cs.tmc.langs.RunResult.Status.COMPILE_FAILED;
import static fi.helsinki.cs.tmc.langs.RunResult.Status.GENERIC_ERROR;
import static fi.helsinki.cs.tmc.langs.RunResult.Status.PASSED;
import static fi.helsinki.cs.tmc.langs.RunResult.Status.TESTS_FAILED;
import hy.tmc.cli.testhelpers.testresults.RunResultBuilder;
import hy.tmc.cli.testhelpers.testresults.TestResultFactory;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

public class ResultInterpreterTest {

    RunResult allPassed;
    RunResult allFailed;
    RunResult someFailed;
    RunResult compileError;
    RunResult genericError;

    public ResultInterpreterTest() {
        allPassed = new RunResultBuilder().withStatus(PASSED).build();
        createAllFailed();
        createSomeFailed();
        compileError = new RunResultBuilder().withStatus(COMPILE_FAILED).build();
        genericError = new RunResultBuilder().withStatus(GENERIC_ERROR).build();
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    private void createAllFailed() {
        RunResultBuilder builder = new RunResultBuilder();
        allFailed = builder
                .withStatus(TESTS_FAILED)
                .withTests(TestResultFactory.failedTests())
                .build();
    }

    private void createSomeFailed() {
        RunResultBuilder builder = new RunResultBuilder();
        someFailed = builder
                .withStatus(TESTS_FAILED)
                .withTests(TestResultFactory.passedTests())
                .withTests(TestResultFactory.failedTests())
                .build();
    }

}
