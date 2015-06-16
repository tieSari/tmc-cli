package feature.submit;

import com.github.tomakehurst.wiremock.WireMockServer;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.containing;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import hy.tmc.cli.configuration.ClientData;
import hy.tmc.cli.configuration.ConfigHandler;
import hy.tmc.cli.frontend.communication.server.Server;
import hy.tmc.cli.testhelpers.ExampleJson;

import hy.tmc.cli.testhelpers.TestClient;
import hy.tmc.cli.testhelpers.Wiremocker;
import java.io.File;
import java.io.IOException;
import static org.junit.Assert.assertTrue;
import org.junit.Rule;

public class SubmitSteps {

    private int port;

    private Thread serverThread;
    private TestClient testClient;
    private Server server;

    private ConfigHandler configHandler;
    private WireMockServer wireMockServer;

    @Rule
    WireMockRule wireMockRule = new WireMockRule();
    private String submitCommand;

    /*
     * Writes wiremock-serveraddress to config-file, starts wiremock-server and defines routes for two scenario.
     */
    @Before
    public void initializeServer() throws IOException {
        configHandler = new ConfigHandler();
        configHandler.writeServerAddress("http://127.0.0.1:8080");

        server = new Server();
        port = configHandler.readPort();
        serverThread = new Thread(server);
        serverThread.start();
        testClient = new TestClient(port);

        Wiremocker mocker = new Wiremocker();
        wireMockServer = mocker.wiremockSubmitPaths();
        mocker.wireMockSuccesfulSubmit(wireMockServer);
        mocker.wireMockExpiredSubmit(wireMockServer);
        mocker.wiremockFailingSubmit(wireMockServer);
    }

//    @Given("^user has logged in with username \"(.*?)\" and password \"(.*?)\"$")
//    public void user_has_logged_in_with_username_and_password(String username, String password) throws Throwable {
//        testClient.sendMessage("login username " + username + " password " + password);
//        Thread.sleep(300);
//    }
//
//    @When("^user gives command submit with valid path \"(.*?)\" and exercise \"(.*?)\"$")
//    public void user_gives_command_submit_with_valid_path_and_exercise(String pathFromProjectRoot, String exercise) throws Throwable {
//        submitCommand = "submit path ";
//        String submitPath = System.getProperty("user.dir") + pathFromProjectRoot;
//        submitCommand = submitCommand + submitPath;
//    }
//
//    @When("^user gives command submit with expired path \"(.*?)\" and exercise \"(.*?)\"$")
//    public void user_gives_command_submit_with_expired_path_and_exercise(String pathFromProjectRoot, String exercise) throws Throwable {
//        testClient.init();
//        String submitCommand = "submit path ";
//        String submitPath = System.getProperty("user.dir") + pathFromProjectRoot + "/" + exercise;
//        final String message = submitCommand + submitPath;
//        testClient.sendMessage(message);
//    }
//    
//    @When("^user executes the command$")
//    public void user_executes_the_command() throws IOException {
//        testClient.init();
//        testClient.sendMessage(submitCommand);
//    }
//
//    @Then("^user will see all test passing$")
//    public void user_will_see_all_test_passing() throws Throwable {
//        String result = testClient.reply();
//        assertTrue(result.contains("All tests passed"));
//    }
//
//    @Then("^user will see the some test passing$")
//    public void user_will_see_the_some_test_passing() throws Throwable {
//        final String result = testClient.reply();
//        assertTrue(result.contains("failed"));
//    }
//
//    @Then("^user will see a message which tells that exercise is expired\\.$")
//    public void user_will_see_a_message_which_tells_that_exercise_is_expired() throws Throwable {
//        final String result = testClient.reply();
//        assertTrue(result.contains("expired"));
//    }
//
//    @When("^exercise \"(.*?)\"$")
//    public void exercise(String exercise) throws Throwable {
//        this.submitCommand += File.separatorChar + exercise;
//    }
//
//    @When("^flag \"(.*?)\"$")
//    public void flag(String flag) throws Throwable {
//        this.submitCommand += " " + flag;
//    }
    @Given("^user has logged in with username \"(.*?)\" and password \"(.*?)\"$")
    public void user_has_logged_in_with_username_and_password(String username, String password) throws Throwable {
        testClient.sendMessage("login username " + username + " password " + password);
        Thread.sleep(300);
    }

    @When("^user gives command submit with path \"(.*?)\" and exercise \"(.*?)\"$")
    public void user_gives_command_submit_with_path_and_exercise(String path, String exercise) throws Throwable {
        submitCommand = "submit path " + System.getProperty("user.dir") + path + File.separator + exercise;
    }
    
    @When("^flag \"(.*?)\"$")
    public void flag(String flag) throws Throwable {
        this.submitCommand += " " + flag;
    }

    @When("^user executes the command$")
    public void user_executes_the_command() throws Throwable {
        testClient.init();
        testClient.sendMessage(submitCommand);
    }

    @Then("^user will see all test passing$")
    public void user_will_see_all_test_passing() throws Throwable {
        final String result = testClient.reply();
        assertTrue(result.contains("All tests passed"));
    }


    @Then("^user will see a message which tells that exercise is expired\\.$")
    public void user_will_see_a_message_which_tells_that_exercise_is_expired() throws Throwable {
        final String result = testClient.reply();
        assertTrue(result.contains("expired"));
    }

    /*
     * Returns everything to it's original state.
     */
    @After
    public void closeAll() throws IOException {
        server.close();
        serverThread.interrupt();
        wireMockServer.stop();
        configHandler.writeServerAddress("http://tmc.mooc.fi/staging");
        ClientData.clearUserData();
    }
}
