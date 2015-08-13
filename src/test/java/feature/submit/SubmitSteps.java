package feature.submit;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.junit.WireMockRule;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import fi.helsinki.cs.tmc.core.TmcCore;
import fi.helsinki.cs.tmc.core.communication.UrlHelper;

import hy.tmc.cli.CliSettings;
import hy.tmc.cli.TmcCli;
import hy.tmc.cli.configuration.ConfigHandler;
import hy.tmc.cli.testhelpers.TestClient;
import hy.tmc.cli.testhelpers.Wiremocker;

import org.hamcrest.CoreMatchers;
import org.junit.Rule;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Date;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class SubmitSteps {

    private static final String SERVER_URI = "127.0.0.1";
    private static final int SERVER_PORT = 8080;
    private static final String SERVER_ADDRESS = "http://" + SERVER_URI + ":" + SERVER_PORT;
    private final String coursesExtension;
    @Rule WireMockRule wireMockRule = new WireMockRule();
    private int port;
    private TestClient testClient;
    private TmcCli tmcCli;
    private ConfigHandler configHandler;
    private WireMockServer wireMockServer;
    private UrlHelper urlHelper;
    private String submitCommand;
    public SubmitSteps() {
        CliSettings settings = new CliSettings();
        settings.setServerAddress(SERVER_ADDRESS);
        this.urlHelper = new UrlHelper(settings);
        coursesExtension = urlHelper.withParams("/courses.json");
    }

    /**
     * Writes wiremock-serveraddress to config-file, starts wiremock-server and
     * defines routes for two scenario.
     */
    @Before
    public void initializeServer() throws IOException {
        configHandler = new ConfigHandler();
        configHandler.writeServerAddress(SERVER_ADDRESS);

        tmcCli = new TmcCli(new TmcCore(), false);

        tmcCli.setServer(SERVER_ADDRESS);
        tmcCli.startServer();
        testClient = new TestClient(new ConfigHandler().readPort());

        Wiremocker mocker = new Wiremocker();
        wireMockServer = mocker.mockAnyUserAndSubmitPaths();
        mocker.wireMockSuccesfulSubmit(wireMockServer);
        mocker.wireMockExpiredSubmit(wireMockServer);
        mocker.wiremockFailingSubmit(wireMockServer);
        new ConfigHandler().writeLastUpdate(new Date());

    }

    @Given("^user has logged in with username \"(.*?)\" and password \"(.*?)\"$")
    public void user_has_logged_in_with_username_and_password(String username, String password)
        throws Throwable {
        testClient.sendMessage("login username " + username + " password " + password);
        testClient.getAllFromSocket();
        testClient.init();
    }

    @When("^user gives command submit with valid path \"(.*?)\" and exercise \"(.*?)\"$")
    public void user_gives_command_submit_with_valid_path_and_exercise(String pathFromProjectRoot,
        String exercise) throws Throwable {

        pathFromProjectRoot = Paths.get(pathFromProjectRoot).toString();
        exercise = Paths.get(exercise).toString();
        submitCommand =
            "submit path " + System.getProperty("user.dir") + pathFromProjectRoot + File.separator
                + exercise + " courseID 3 username test password 1234";
    }

    @When("^flag \"(.*?)\"$")
    public void flag(String flag) throws Throwable {
        this.submitCommand += " " + flag;
    }

    @When("^user executes the command$")
    public void user_executes_the_command() throws Throwable {
        testClient.init();
        Thread.sleep(300);
        testClient.sendMessage(submitCommand);
        Thread.sleep(300);
    }

    @Then("^user will see all test passing$")
    public void user_will_see_all_test_passing()
        throws Throwable {
        final String result = testClient.reply();
        assertThat(result, CoreMatchers.containsString("All tests passed"));
    }

    @Then("^user will see the some test passing$")
    public void user_will_see_the_some_test_passing()
        throws Throwable {
        final String result = testClient.getAllFromSocket().toLowerCase();
        assertTrue(result.contains("some tests failed"));
    }

    @Then("^user will see a message which tells that exercise is expired\\.$")
    public void user_will_see_a_message_which_tells_that_exercise_is_expired() throws Throwable {
        final String result = testClient.getAllFromSocket();
        assertTrue(result.contains("expired"));
    }

    @When("^user gives command submit with path \"([^\"]*)\" and exercise \"([^\"]*)\"$")
    public void user_gives_command_submit_with_path_and_exercise(String pathFromProjectRoot,
        String exercise) throws Throwable {
        submitCommand =
            "submit path " + System.getProperty("user.dir") + pathFromProjectRoot + File.separator
                + exercise + " courseID 21";
    }

    @After
    public void closeAll() throws IOException {
        wireMockServer.stop();
        tmcCli.stopServer();
        new File(new ConfigHandler().getConfigFilePath()).delete();
    }

}
