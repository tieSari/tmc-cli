package feature.runtests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import hy.tmc.cli.frontend.communication.commands.RunTests;
import hy.tmc.cli.frontend.communication.server.ProtocolException;
import hy.tmc.cli.testhelpers.FrontendStub;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class TmcTestsSteps {

    private RunTests testRunner;
    private FrontendStub front;

    public TmcTestsSteps() {
        front = new FrontendStub();
    }

    /**
     * Create RunTests command and set filepath parameter.
     * @param exerciseDirectory directory path
     */
    @Given("^the user is in the exercise directory \"(.*?)\"$")
    public void theUserIsInTheExerciseDirectory(String exerciseDirectory) {
        testRunner = new RunTests(front, null);
        testRunner.setParameter("filepath", exerciseDirectory);
    }

    @When("^the user runs the tests$")
    public void theUserRunsTheTests() throws ProtocolException {
        testRunner.execute();
    }

    /**
     * Test case that all tmc tests pass.
     */
    @Then("^the user sees that all tests have passed\\.$")
    public void theUserSeesAllTestsPassing() {
        String output = front.getMostRecentLine();
        assertEquals("All tests passed. You can now submit", output);
    }

    /**
     * Test case when some tests fail and user gets information.
     */
    @Then("^the user sees which tests have failed$")
    public void theUserSeesWhichTestsHaveFailed() {
        String output = front.getMostRecentLine();
        assertEquals("Some tests failed:", output.substring(0, 18));
        assertTrue(output.contains("1 tests failed:\n"
                + "  NimiTest test failed: Et tulostanut mitään!"));
    }
    
    /**
     * User should get both information about passed tests and failed tests.
     */
    @Then("^the user sees both passed and failed tests$")
    public void theUserSeesBothPassedAndFailedTests() {
        String output = front.getMostRecentLine();
        assertTrue(output.contains("1 tests passed"));
        assertTrue(output.contains("2 tests failed"));

    }
}
