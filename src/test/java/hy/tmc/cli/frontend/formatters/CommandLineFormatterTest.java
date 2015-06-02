
package hy.tmc.cli.frontend.formatters;

import fi.helsinki.cs.tmc.langs.RunResult;
import static fi.helsinki.cs.tmc.langs.RunResult.Status.COMPILE_FAILED;
import static fi.helsinki.cs.tmc.langs.RunResult.Status.GENERIC_ERROR;
import static fi.helsinki.cs.tmc.langs.RunResult.Status.PASSED;
import static fi.helsinki.cs.tmc.langs.RunResult.Status.TESTS_FAILED;
import fi.helsinki.cs.tmc.langs.TestResult;
import hy.tmc.cli.testhelpers.testresults.RunResultBuilder;
import hy.tmc.cli.testhelpers.testresults.TestResultFactory;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class CommandLineFormatterTest {
    
    RunResult allPassed;
    RunResult allFailed;
    RunResult someFailed;
    RunResult compileError;
    RunResult genericError;
    RunResultBuilder builder;
    CommandLineFormatter formatter;
    List<TestResult> passed;
    
    public CommandLineFormatterTest() {
        
        allPassed = new RunResultBuilder().withStatus(PASSED).build();
        compileError = new RunResultBuilder().withStatus(COMPILE_FAILED).build();
        genericError = new RunResultBuilder().withStatus(GENERIC_ERROR).build();
        allFailed = new RunResultBuilder()
                .withStatus(TESTS_FAILED)
                .withTests(TestResultFactory.failedTests())
                .build();
        formatter = new CommandLineFormatter();
        passed = TestResultFactory.passedTests();
    }
    
    @Test
    public void testInterpretStatus(){
        String explanation = formatter.interpretStatus(allPassed);
        assertTrue(explanation.contains("All tests passed. You can now submit"));
    }
    
    @Test
    public void interpretGenericError(){
        String explanation = formatter.interpretStatus(genericError);
        assertTrue(explanation.contains("Failed due to an internal error"));
    } 
    
    @Test
    public void someTestsFailed(){
        String explanation = formatter.someTestsFailed();
        assertTrue(explanation.contains("Some tests failed:\n"));
    }
    
    @Test
    public void noTestsPassed(){
        String explanation = formatter.noTestsPassed();
        assertTrue(explanation.contains("No tests passed.\n"));
    }
    
    @Test
    public void testHowMuchTestsPassed(){
        String explanation = formatter.howMuchTestsPassed(5);
        assertTrue(explanation.contains("5 tests passed:\n"));
    }
    
    @Test
    public void testHowMuchTestsFailed(){
        String explanation = formatter.howMuchTestsFailed(5);
        assertTrue(explanation.contains("5 tests failed:\n"));
    }
    
    @Test
    public void testPassedTests(){
        String explanation = formatter.getPassedTests(passed);
        String shouldBe = "  Muuttujat testaaKanat";
        assertTrue(explanation.contains(shouldBe));
    }
    
    
}
