package feature.concurrency;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import hy.tmc.cli.configuration.ClientData;
import hy.tmc.cli.configuration.ConfigHandler;
import hy.tmc.cli.frontend.communication.server.Server;
import hy.tmc.cli.testhelpers.TestClient;
import hy.tmc.cli.testhelpers.Wiremocker;
import java.io.File;
import java.io.IOException;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.Rule;

public class ConcurrencySteps {

    private TestClient testClientA;
    private TestClient testClientB;
    private Server server;
    private int port;
    private Thread serverThread;
    private ConfigHandler configHandler;
    private WireMockServer wireMockServer;

    @Rule
    WireMockRule wireMockRule = new WireMockRule();
    private final String defaultWiremockAddress = "http://127.0.0.1:8080";

    @Before
    public void setup() throws IOException {
        configHandler = new ConfigHandler();
        configHandler.writeServerAddress(defaultWiremockAddress);
        server = new Server();
        port = configHandler.readPort();
        serverThread = new Thread(server);
        serverThread.start();
        testClientA = new TestClient(port);
        testClientB = new TestClient(port);
    }

    @Given("^user has logged in with username \"(.*?)\" and password \"(.*?)\"$")
    public void user_has_logged_in_with_username_and_password(String username, String password) throws Throwable {
        startWireMock();
        testClientA.sendMessage("login username " + username + " password " + password);
        assertTrue(testClientA.reply().contains("Auth successful"));
        testClientA.init();
    }

    @When("^user starts command \"(.*?)\" and path \"(.*?)\" and exercise \"(.*?)\" and then user starts command help$")
    public void user_starts_command_and_path_and_exercise_and_then_user_starts_command_help(String submitCommand, String pathFromProjectRoot, String exercise) throws Throwable {
        String submitPath = System.getProperty("user.dir") + pathFromProjectRoot + File.separator + exercise;
        final String submitMessage = submitCommand + submitPath;
        testClientA.sendMessage(submitMessage);
        testClientB.sendMessage("help");
    }

    @Then("^user A gets output of submit and user B gets output of help$")
    public void user_A_gets_output_of_submit_and_user_B_gets_output_of_help() throws Throwable {
        final String replyA = testClientA.reply();
        assertTrue(replyA.contains("All tests passed"));
        final String replyB = testClientB.reply();
        assertTrue(replyB.contains("Available commands"));
    }

    @Then("^user A gets output after user B$")
    public void user_A_gets_output_after_user_B() throws Throwable {
        for (int i = 0; i < 10; i++) {
           if (testClientA.isReadyToBeRead() && !testClientB.isReadyToBeRead()) {
               fail("Test client A was ready first, in other words no concurrency");
           }
            Thread.sleep(50);
        }    
    }

    @After
    public void clear() throws IOException {
        server.close();
        serverThread.interrupt();
        wireMockServer.stop();
        configHandler.writeServerAddress("http://tmc.mooc.fi/staging");
        ClientData.clearUserData();
    }

    private void startWireMock() {
        Wiremocker mocker = new Wiremocker();
        wireMockServer = mocker.wiremockSubmitPaths();
        mocker.wireMockSuccesfulSubmit(wireMockServer);
        mocker.wireMockExpiredSubmit(wireMockServer);
        mocker.wiremockFailingSubmit(wireMockServer);
    }
}
