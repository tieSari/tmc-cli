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

    @Before
    public void setup() throws IOException {
        configHandler = new ConfigHandler();
        configHandler.writeServerAddress("http://127.0.0.1:8080");
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
        System.out.println("Login tulostus: " + testClientA.reply());
        testClientA.init();
        Thread.sleep(1200);
    }

    @When("^user starts command \"(.*?)\" and path \"(.*?)\" and exercise \"(.*?)\" and then user starts command help$")
    public void user_starts_command_and_path_and_exercise_and_then_user_starts_command_help(String submitCommand, String pathFromProjectRoot, String exercise) throws Throwable {
        String submitPath = System.getProperty("user.dir") + pathFromProjectRoot + File.separator + exercise;
        final String message = submitCommand + submitPath;
        System.out.println(message);
        testClientA.sendMessage(message);
        testClientB.sendMessage("help");
        final String firstA = testClientA.reply();
        final String firstB = testClientB.reply();
        testClientA.init();
        System.out.println("A tulostus: " + firstA);
        System.out.println("B tulostus: " + firstB);
        
    }

    @Then("^user sees first output of help and after that submit$")
    public void user_sees_first_output_of_help_and_after_that_submit() throws Throwable {
        System.out.println("hei");
        String result = testClientA.reply();
        System.out.println("hei");
        System.out.println(result);
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
