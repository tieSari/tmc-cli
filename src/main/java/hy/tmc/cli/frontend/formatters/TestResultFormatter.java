package hy.tmc.cli.frontend.formatters;

import fi.helsinki.cs.tmc.langs.domain.RunResult;
import fi.helsinki.cs.tmc.langs.domain.TestResult;

import java.util.List;

/**
 * TestResultFormatter interface makes easier to attach new frontends to core.
 * TestResult is formatted with some class which implements this interface.
 */
public interface TestResultFormatter {
    String interpretStatus(RunResult result);

    String someTestsFailed();

    String noTestsPassed();

    String howMuchTestsPassed(int amount);

    String howMuchTestsFailed(int amount);

    String getPassedTests(List<TestResult> passed);

    String getFailedTestOutput(TestResult failed);

    String getStackTrace(TestResult result);
}
