package feature.runTests;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import hy.tmc.cli.frontend.communication.commands.RunTests;
import hy.tmc.cli.frontend.communication.server.ProtocolException;
import hy.tmc.cli.testhelpers.FrontendStub;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TmcTestsSteps {

    private RunTests testRunner;
    private FrontendStub front;

    public TmcTestsSteps() {
        front = new FrontendStub();
    }

    @Given("^the user is in the exercise directory \"(.*?)\"$")
    public void theUserIsInTheExerciseDirectory(String exerciseDirectory) {
        testRunner = new RunTests(front, null);
        testRunner.setParameter("filepath", exerciseDirectory);
    }

    @When("^the user runs the tests$")
    public void theUserRunsTheTests() throws ProtocolException {
        testRunner.execute();
    }

    @Then("^the user sees that all tests have passed\\.$")
    public void theUserSeesAllTestsPassing() {
        String output = front.getMostRecentLine();
        System.out.println(output);
        assertEquals("All tests passed. You can now submit", output);
    }

    @Then("^the user sees which tests have failed$")
    public void theUserSeesWhichTestsHaveFailed() {
        String output = front.getMostRecentLine();
        System.out.println(output);
        assertEquals("Some tests failed:", output.substring(0, 18));
        assertTrue(output.contains("1 tests failed:\n"
                + "  NimiTest test failed: Et tulostanut mitään!"));
    }
    
    @Then("^the user sees both passed and failed tests$")
    public void theUserSeesBothPassedAndFailedTests() {
        String output = front.getMostRecentLine();
        System.out.println(output);
        assertTrue(output.contains("2 tests passed"));
        assertTrue(output.contains("1 tests failed"));

    }
}
