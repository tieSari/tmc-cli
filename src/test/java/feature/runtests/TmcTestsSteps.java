package feature.runtests;

import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import hy.tmc.cli.backend.Mailbox;
import hy.tmc.cli.configuration.ClientData;
import hy.tmc.cli.frontend.communication.commands.RunTests;
import hy.tmc.cli.frontend.communication.server.ProtocolException;
import hy.tmc.cli.synchronization.TmcServiceScheduler;
import hy.tmc.cli.testhelpers.FrontendStub;
import hy.tmc.cli.testhelpers.ProjectRootFinderStub;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class TmcTestsSteps {

    private RunTests testRunner;
    private FrontendStub front;

    public TmcTestsSteps() {
        front = new FrontendStub();
    }

    @Before
    public void setUp() {
        ClientData.setProjectRootFinder(new ProjectRootFinderStub());
        ClientData.clearUserData();
        Mailbox.create();
        ClientData.setUserData("test", "1234");
        TmcServiceScheduler.disablePolling();
    }

    /**
     * Create RunTests command and set filepath parameter.
     *
     * @param exerciseDirectory directory path
     */
    @Given("^the user is in the exercise directory \"(.*?)\"$")
    public void theUserIsInTheExerciseDirectory(String exerciseDirectory) {
        testRunner = new RunTests(front);
        System.out.println("exDir: " + exerciseDirectory);
        System.out.println("Working Directory = " +
                System.getProperty("user.dir"));
        testRunner.setParameter("path", exerciseDirectory);
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
        assertEquals("\u001B[32mAll tests passed.\u001B[0m You can now submit", output);
    }

    /**
     * Test case when some tests fail and user gets information.
     */
    @Then("^the user sees which tests have failed$")
    public void theUserSeesWhichTestsHaveFailed() {
        String output = front.getMostRecentLine();
        assertEquals("Some tests failed:", output.substring(0, 18));
        assertTrue(output.contains("\u001B[31m1 tests failed:\n"));
        assertTrue(output.contains("  NimiTest test failed: Et tulostanut mitään!"));
    }

    /**
     * User should get both information about passed tests and failed tests.
     */
    @Then("^the user sees both passed and failed tests$")
    public void theUserSeesBothPassedAndFailedTests() {
        String output = front.getMostRecentLine();
        System.out.println("Output: " + output);
        assertTrue(output.contains("1 tests passed"));
        assertTrue(output.contains("2 tests failed"));

    }
    
    @After
    public void clean() throws InterruptedException {
        //Thread.sleep(5000);
        Mailbox.destroy();
        ClientData.clearUserData();
    }
}
