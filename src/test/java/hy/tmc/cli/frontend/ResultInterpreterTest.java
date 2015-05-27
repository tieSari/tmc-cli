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
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class ResultInterpreterTest {

    RunResult allPassed;
    RunResult allFailed;
    RunResult someFailed;
    RunResult compileError;
    RunResult genericError;

    private ResultInterpreter allFailedInterpreter;
    private ResultInterpreter someFailedInterpreter;

    public ResultInterpreterTest() {
        allPassed = new RunResultBuilder().withStatus(PASSED).build();
        createAllFailed();
        createSomeFailed();
        compileError = new RunResultBuilder().withStatus(COMPILE_FAILED).build();
        genericError = new RunResultBuilder().withStatus(GENERIC_ERROR).build();
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

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        allFailedInterpreter = new ResultInterpreter(this.allFailed);
        someFailedInterpreter = new ResultInterpreter(this.someFailed);
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testAllTestsPassed() {
        ResultInterpreter allPassedInterpreter = new ResultInterpreter(this.allPassed);
        assertEquals("All tests passed. You can now submit", allPassedInterpreter.interpret());
    }

    @Test
    public void testCompileErrorMessage() {
        ResultInterpreter allPassedInterpreter = new ResultInterpreter(this.compileError);
        assertEquals("Code did not compile.", allPassedInterpreter.interpret());
    }

    @Test
    public void testGenericErrorMessage() {
        ResultInterpreter allPassedInterpreter = new ResultInterpreter(this.genericError);
        assertEquals("Failed to run tests.", allPassedInterpreter.interpret());
    }

    @Test
    public void testMainFailureMessage() {
        String summary = this.allFailedInterpreter.interpret();
        assertTrue(summary.contains("Some tests failed:"));
        summary = this.someFailedInterpreter.interpret();
        assertTrue(summary.contains("Some tests failed:"));
    }

    @Test
    public void correctAmountOfPassedTestsAllFails() {
        String summary = this.allFailedInterpreter.interpret();
        assertTrue(summary.contains("No tests passed"));
    }

    @Test
    public void correctAmountOfPassedTestsSomeFails() {
        String summary = this.someFailedInterpreter.interpret();
        assertTrue(summary.contains("3 tests passed"));
    }

    @Test
    public void correctAmountOfFailedTestsAllFails() {
        String summary = this.allFailedInterpreter.interpret();
        assertTrue(summary.contains("2 tests failed"));
    }

    @Test
    public void correctAmountOfFailedTestsSomeFails() {
        String summary = this.allFailedInterpreter.interpret();
        assertTrue(summary.contains("2 tests failed"));
    }

    @Test
    public void testErrorMessage() {
        String summary = this.someFailedInterpreter.interpret();
        assertTrue(summary.contains("Ohjelmasi pitäisi tulostaa 6 riviä, eli siinä pitäisi"));
    }

    @Test
    public void testStackTrace() {
        String summary = this.someFailedInterpreter.interpret();
        
        System.out.println(summary);
        
        String test1 = "sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)";
        String test2 = "java.lang.reflect.Method.invoke(Method.java:497)";
        String test3 = "org.junit.runners.model.FrameworkMethod.invokeExplosively(FrameworkMethod.java:42)";
        String test4 = "fi.helsinki.cs.tmc.edutestutils.MockStdio$1.evaluate(MockStdio.java:106) ";
        String test5 = "org.junit.runners.ParentRunner$1.schedule(ParentRunner.java:60)";
        assertTrue(summary.contains(test1));
        assertTrue(summary.contains(test2));
        assertTrue(summary.contains(test3));
        assertTrue(summary.contains(test4));
        assertTrue(summary.contains(test5));

    }

}
