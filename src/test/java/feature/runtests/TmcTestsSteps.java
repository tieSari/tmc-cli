package feature.runtests;

<<<<<<< HEAD
import cucumber.api.PendingException;
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
import hy.tmc.cli.testhelpers.MailExample;
import hy.tmc.cli.testhelpers.ProjectRootFinderStub;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
=======
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import hy.tmc.cli.frontend.communication.commands.RunTests;
import hy.tmc.cli.frontend.communication.server.ProtocolException;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import fi.helsinki.cs.tmc.langs.NoLanguagePluginFoundException;
>>>>>>> 7061d626a3951db33faf53d915810654bf6c1720

public class TmcTestsSteps {

    private RunTests testRunner;
<<<<<<< HEAD
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
=======
    private String output;
    
    /**
     * Create RunTests command and set path parameter.
>>>>>>> 7061d626a3951db33faf53d915810654bf6c1720
     *
     * @param exerciseDirectory directory path
     */
    @Given("^the user is in the exercise directory \"(.*?)\"$")
    public void theUserIsInTheExerciseDirectory(String exerciseDirectory) {
<<<<<<< HEAD
        testRunner = new RunTests(front);
        System.getProperty("user.dir");
=======
        testRunner = new RunTests();
>>>>>>> 7061d626a3951db33faf53d915810654bf6c1720
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

<<<<<<< HEAD
    @Given("^the user has mail in the mailbox and exercise path is \"(.*?)\"$")
    public void the_user_has_mail_in_the_mailbox_and_exercise_path_is(String path) throws Throwable {
        Mailbox.getMailbox().get().fill(MailExample.reviewExample());
        testRunner = new RunTests(front);
        testRunner.setParameter("path", path);
    }

    @Then("^user will see the new mail$")
    public void user_will_see_the_new_mail() throws Throwable {
        String output = front.getMostRecentLine();
        assertTrue(output.contains("Bossman Samu"));
    }

    @Given("^polling for reviews is not in progress and exercise path is \"(.*?)\"$")
    public void polling_for_reviews_is_not_in_progress_and_exercise_path_is(String path) throws Throwable {
        TmcServiceScheduler.enablePolling();
        testRunner = new RunTests(front);
        testRunner.setParameter("path", path);
        assertFalse(TmcServiceScheduler.isRunning());
    }

    @Then("^the polling will be started$")
    public void the_polling_will_be_started() throws Throwable {
        assertTrue(TmcServiceScheduler.isRunning());
        TmcServiceScheduler.disablePolling();
    }

    @After
    public void clean() throws InterruptedException {
        Mailbox.destroy();
        TmcServiceScheduler.getScheduler().stop();
        ClientData.clearUserData();
=======
    @Given("^the user gives the vim flag$")
    public void gives_the_vim_flag() throws Throwable {
        testRunner.setParameter("--vim", "");
    }

    @Then("^the user sees that all tests have passed formatted with vim formatter\\.$")
    public void the_user_sees_that_all_tests_have_passed_formatted_with_vim_formatter() throws Throwable {
        assertEquals("All tests passed. You can now submit", output);
>>>>>>> 7061d626a3951db33faf53d915810654bf6c1720
    }
}
