package feature.runtests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import hy.tmc.cli.frontend.communication.commands.RunTests;
import hy.tmc.cli.frontend.communication.server.ProtocolException;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import fi.helsinki.cs.tmc.langs.NoLanguagePluginFoundException;

public class TmcTestsSteps {

    private RunTests testRunner;
    private String output;
    
    /**
     * Create RunTests command and set path parameter.
     *
     * @param exerciseDirectory directory path
     */
    @Given("^the user is in the exercise directory \"(.*?)\"$")
    public void theUserIsInTheExerciseDirectory(String exerciseDirectory) {
        testRunner = new RunTests();
        testRunner.setParameter("path", exerciseDirectory);
    }

    @When("^the user runs the tests$")
    public void theUserRunsTheTests() throws ProtocolException, NoLanguagePluginFoundException {
        output = testRunner.parseData(testRunner.call()).get();
    }

    /**
     * Test case that all tmc tests pass.
     */
    @Then("^the user sees that all tests have passed\\.$")
    public void theUserSeesAllTestsPassing() {
        assertEquals("\u001B[32mAll tests passed.\u001B[0m You can now submit", output);
    }

    /**
     * Test case when some tests fail and user gets information.
     */
    @Then("^the user sees which tests have failed$")
    public void theUserSeesWhichTestsHaveFailed() {
        assertEquals("Some tests failed:", output.substring(0, 18));
        assertTrue(output.contains("\u001B[31m1 tests failed:\n"));
        assertTrue(output.contains("  NimiTest test failed: Et tulostanut mitään!"));
    }

    /**
     * User should get both information about passed tests and failed tests.
     */
    @Then("^the user sees both passed and failed tests$")
    public void theUserSeesBothPassedAndFailedTests() {
        assertTrue(output.contains("1 tests passed"));
        assertTrue(output.contains("2 tests failed"));
    }

    @Given("^the user gives the vim flag$")
    public void gives_the_vim_flag() throws Throwable {
        testRunner.setParameter("--vim", "");
    }

    @Then("^the user sees that all tests have passed formatted with vim formatter\\.$")
    public void the_user_sees_that_all_tests_have_passed_formatted_with_vim_formatter() throws Throwable {
        assertEquals("All tests passed. You can now submit", output);
    }
}
