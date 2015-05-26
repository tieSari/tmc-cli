package hy.tmc.cli.frontend;

import static fi.helsinki.cs.tmc.langs.RunResult.Status.COMPILE_FAILED;
import static fi.helsinki.cs.tmc.langs.RunResult.Status.GENERIC_ERROR;
import static fi.helsinki.cs.tmc.langs.RunResult.Status.PASSED;
import static fi.helsinki.cs.tmc.langs.RunResult.Status.TESTS_FAILED;

import fi.helsinki.cs.tmc.langs.RunResult;
import fi.helsinki.cs.tmc.langs.TestResult;

import java.util.List;


public class ResultInterpreter {

    public String interpret(RunResult result) {
        String message = "";
        
        switch (result.status) {
            case PASSED: 
                return "All tests passed. You can now submit";
            case TESTS_FAILED: 
                message += "Some tests failed:\n";
                break;
            case COMPILE_FAILED:
                return "Code did not compile.";
            case GENERIC_ERROR: 
                return "A generic error occured.";
            default:
                return "";
        }
        
        List<TestResult> tests = result.testResults;
        
        for (TestResult testResult : tests) {
            if (testResult.passed) {
                continue;
            }
            message += "Test " + testResult.name + " failed.\n";
            message += testResult.errorMessage;
        }
        
        return "";
    }
}
