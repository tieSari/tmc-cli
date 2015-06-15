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
<<<<<<< HEAD
import hy.tmc.cli.testhelpers.Wiremocker;
=======
>>>>>>> b4daca84b62f91a2ea5ea563447fb900c9db9f5a
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

<<<<<<< HEAD
        Wiremocker mocker = new Wiremocker();
        wireMockServer = mocker.wiremockSubmitPaths();
        mocker.wireMockSuccesfulSubmit(wireMockServer);
        mocker.wireMockExpiredSubmit(wireMockServer);
        mocker.wiremockFailingSubmit(wireMockServer);
=======
        startWireMock();
    }

    /*
     * Starts wiremock and defines routes.
     */
    private void startWireMock() {
        wireMockServer = new WireMockServer();
        wireMockServer.start();

        wireMockServer.stubFor(get(urlEqualTo("/user"))
                .withHeader("Authorization", containing("Basic dGVzdDoxMjM0"))
                .willReturn(
                        aResponse()
                        .withStatus(200)
                )
        );
        wiremockGET("/courses.json?api_version=7", ExampleJson.allCoursesExample);
        wireMockSuccesfulScenario();
        wiremockFailingScenario();
        wireMockExpiredScenario();
    }

    private void wiremockFailingScenario() {
        wiremockGET("/courses/313.json?api_version=7", ExampleJson.failingCourse);
        wiremockPOST("/exercises/285/submissions.json?api_version=7", ExampleJson.failedSubmitResponse);
        wiremockGET("/submissions/7777.json?api_version=7", ExampleJson.failedSubmission);
    }

    private void wireMockSuccesfulScenario() {
        wiremockGET("/courses/3.json?api_version=7", ExampleJson.courseExample);
        wiremockPOST("/exercises/286/submissions.json?api_version=7", ExampleJson.submitResponse);
        wiremockGET("/submissions/1781.json?api_version=7", ExampleJson.successfulSubmission);
    }

    private void wireMockExpiredScenario() {
        wiremockGET("/courses/21.json?api_version=7", ExampleJson.expiredCourseExample);
    }

    /*
     * When httpGet-request is sent to http://127.0.0.1:8080/ + urlToMock, wiremock returns returnBody
     */
    private void wiremockGET(final String urlToMock, final String returnBody) {
        wireMockServer.stubFor(get(urlEqualTo(urlToMock))
                .willReturn(aResponse()
                        .withBody(returnBody)
                )
        );
    }

    /*
     * When httpPost-request is sent to http://127.0.0.1:8080/ + urlToMock, wiremock returns returnBody
     */
    private void wiremockPOST(final String urlToMock, final String returnBody) {
        wireMockServer.stubFor(post(urlEqualTo(urlToMock))
                .willReturn(aResponse()
                        .withBody(returnBody)
                )
        );
>>>>>>> b4daca84b62f91a2ea5ea563447fb900c9db9f5a
    }

    @Given("^user has logged in with username \"(.*?)\" and password \"(.*?)\"$")
    public void user_has_logged_in_with_username_and_password(String username, String password) throws Throwable {
        testClient.sendMessage("login username " + username + " password " + password);
        Thread.sleep(300);
    }

<<<<<<< HEAD
    @When("^user gives command submit with valid path \"(.*?)\" and exercise \"(.*?)\"$")
    public void user_gives_command_submit_with_valid_path_and_exercise(String pathFromProjectRoot, String exercise) throws Throwable {
        testClient.init();
        String submitCommand = "submit path ";
        String submitPath = System.getProperty("user.dir") + pathFromProjectRoot + File.separator + exercise;
        final String message = submitCommand + submitPath;
        testClient.sendMessage(message);
=======
    @When("^user gives command submit with valid path \"(.*?)\"$")
    public void user_gives_command_submit_with_valid_path(String pathFromProjectRoot) throws Throwable {
        // testClient.init();
        this.submitCommand = "submit path ";
        String submitPath = System.getProperty("user.dir") + pathFromProjectRoot;
        this.submitCommand = submitCommand + submitPath;
        // testClient.sendMessage(message);
>>>>>>> b4daca84b62f91a2ea5ea563447fb900c9db9f5a
    }

    @When("^user gives command submit with expired path \"(.*?)\" and exercise \"(.*?)\"$")

    public void user_gives_command_submit_with_expired_path_and_exercise(String pathFromProjectRoot, String exercise) throws Throwable {
        testClient.init();
        String submitCommand = "submit path ";
        String submitPath = System.getProperty("user.dir") + pathFromProjectRoot + "/" + exercise;
        final String message = submitCommand + submitPath;
        testClient.sendMessage(message);
    }
    
    @When("^user executes the command$")
    public void user_executes_the_command() throws IOException {
        testClient.init();
        testClient.sendMessage(submitCommand);
    }

    @Then("^user will see all test passing$")
    public void user_will_see_all_test_passing() throws Throwable {
        String result = testClient.reply();
        assertTrue(result.contains("All tests passed"));
    }

    @Then("^user will see the some test passing$")
    public void user_will_see_the_some_test_passing() throws Throwable {
        final String result = testClient.reply();
        assertTrue(result.contains("failed"));
    }

    @Then("^user will see a message which tells that exercise is expired\\.$")
    public void user_will_see_a_message_which_tells_that_exercise_is_expired() throws Throwable {
        final String result = testClient.reply();
        assertTrue(result.contains("expired"));
    }

    @When("^exercise \"(.*?)\"$")
    public void exercise(String exercise) throws Throwable {
        this.submitCommand += File.separatorChar + exercise;
    }

    @When("^flag \"(.*?)\"$")
    public void flag(String flag) throws Throwable {
        this.submitCommand += " " + flag;
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
