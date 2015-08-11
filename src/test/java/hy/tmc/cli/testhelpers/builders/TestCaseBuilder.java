package hy.tmc.cli.testhelpers.builders;

import fi.helsinki.cs.tmc.core.domain.submission.TestCase;

public class TestCaseBuilder {

    private TestCase testCase;

    public TestCaseBuilder() {
        testCase = new TestCase();
    }

    public TestCaseBuilder withDetailedMessage(String message) {
        testCase.setDetailedMessage(message);
        return this;
    }

    public TestCaseBuilder withMessage(String message) {
        testCase.setMessage(message);
        return this;
    }

    public TestCaseBuilder withName(String name) {
        testCase.setName(name);
        return this;
    }

    public TestCaseBuilder asSuccessfull() {
        testCase.setSuccessful(true);
        return this;
    }

    public TestCase build() {
        TestCase built = testCase;
        testCase = new TestCase();
        return built;
    }
}
