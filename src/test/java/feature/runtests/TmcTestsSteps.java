package feature.runtests;

import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import hy.tmc.cli.mail.Mailbox;
import hy.tmc.cli.listeners.TestsListener;
import hy.tmc.cli.frontend.communication.server.ProtocolException;
import hy.tmc.cli.synchronization.TmcServiceScheduler;
import cucumber.api.java.After;

import fi.helsinki.cs.tmc.langs.domain.NoLanguagePluginFoundException;
import hy.tmc.cli.CliSettings;
import hy.tmc.cli.TmcCli;
import hy.tmc.cli.configuration.ConfigHandler;
import hy.tmc.cli.testhelpers.TestClient;
import hy.tmc.core.TmcCore;
import java.io.IOException;
import java.util.Date;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TmcTestsSteps {

    private TestsListener testRunner;
    private String output;
    
    private TmcCli tmcCli;
    private TestClient client;

    private static final String SERVER_URI = "127.0.0.1";
    private static final int SERVER_PORT = 8080;
    private static final String SERVER_ADDRESS = "http://" + SERVER_URI + ":" + SERVER_PORT;
    
    private CliSettings settings;

    @Before
    public void setUp() throws IOException {
        
        tmcCli = new TmcCli(new TmcCore(), false);
        tmcCli.setServer(SERVER_ADDRESS);
        tmcCli.startServer();
        client  = new TestClient(new ConfigHandler().readPort());
        
        settings = new CliSettings();
        Mailbox.create();
        settings.setUserData("test", "1234");
        TmcServiceScheduler.disablePolling();
        new ConfigHandler().writeLastUpdate(new Date());
    }

    @Given("^the user is in the exercise directory \"(.*?)\"$")
    public void theUserIsInTheExerciseDirectory(String exerciseDirectory) throws IOException {
        client.sendMessage("runTests path " + exerciseDirectory + " username jani password banaani");
    }

    @When("^the user runs the tests$")
    public void theUserRunsTheTests() throws ProtocolException, NoLanguagePluginFoundException, fi.helsinki.cs.tmc.langs.domain.NoLanguagePluginFoundException, IOException {
        output = client.reply();
    }

 
    @Then("^the user sees that all tests have passed\\.$")
    public void theUserSeesAllTestsPassing() {
        assertTrue(output.contains("\u001B[32mAll tests passed.\u001B[0m You can now submit"));
    }

  
    @Then("^the user sees which tests have failed$")
    public void theUserSeesWhichTestsHaveFailed() {
        assertTrue(output.contains("Some tests failed:"));
        assertTrue(output.contains("\u001B[31m1 tests failed:\n"));
        assertTrue(output.contains("FAILED \u001B[0mNimiTest test: Et tulostanut mitään!"));
    }


    @Then("^the user sees both passed and failed tests$")
    public void theUserSeesBothPassedAndFailedTests() {
        assertTrue(output.contains("1 tests passed"));
        assertTrue(output.contains("2 tests failed"));
    }


    @Given("^polling for reviews is not in progress and exercise path is \"(.*?)\"$")
    public void polling_for_reviews_is_not_in_progress_and_exercise_path_is(String path) throws Throwable {
        TmcServiceScheduler.enablePolling();
      //  testRunner = new TestsListener();
      //  testRunner.setParameter("path", path);
        assertFalse(TmcServiceScheduler.isRunning());
    }

    @Given("^the user gives the vim flag$")
    public void gives_the_vim_flag() throws Throwable {
      //  testRunner.setParameter("--vim", "");
    }

    @Then("^the user sees that all tests have passed formatted with vim formatter\\.$")
    public void the_user_sees_that_all_tests_have_passed_formatted_with_vim_formatter() throws Throwable {
        assertTrue(output.contains("All tests passed. You can now submit"));
    }

    @After
    public void clean() throws InterruptedException, IOException {
        Mailbox.destroy();
        TmcServiceScheduler.getScheduler().stop();
        settings = new CliSettings();
        tmcCli.stopServer();
        tmcCli.setServer("https://tmc.mooc.fi/staging");
    }
}
