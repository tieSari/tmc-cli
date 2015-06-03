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
import java.io.IOException;
import static org.junit.Assert.assertTrue;
import org.junit.Rule;

public class SubmitSteps {

    private int port;

    private Thread serverThread;
    private TestClient testClient;
    private Server server;

    private ConfigHandler configHandler; // writes the test address
    private WireMockServer wireMockServer;

    @Rule
    WireMockRule wireMockRule = new WireMockRule();

    @Before
    public void initializeServer() throws IOException {
        configHandler = new ConfigHandler();
        configHandler.writeServerAddress("http://127.0.0.1:8080");

        server = new Server();
        port = configHandler.readPort();
        serverThread = new Thread(server);
        serverThread.start();
        testClient = new TestClient(port);

        startWireMock();
    }

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
        wiremockGET("/courses/3.json?api_version=7", ExampleJson.courseExample);
        wiremockPOST("/exercises/286/submissions.json?api_version=7", ExampleJson.submitResponse);
        wiremockGET("/submissions/1781.json?api_version=7", ExampleJson.successfulSubmission);
    }

    private void wiremockGET(final String urlToMock, final String returnBody) {
        wireMockServer.stubFor(get(urlEqualTo(urlToMock))
                .willReturn(aResponse()
                        .withBody(returnBody)
                )
        );
    }

    private void wiremockPOST(final String urlToMock, final String returnBody) {
        wireMockServer.stubFor(post(urlEqualTo(urlToMock))
                .willReturn(aResponse()
                        .withBody(returnBody)
                )
        );
    }

    @Given("^user has logged in with username \"(.*?)\" and password \"(.*?)\"$")
    public void user_has_logged_in_with_username_and_password(String username, String password) throws Throwable {
        testClient.sendMessage("login username " + username + " password " + password);
    }

    @When("^user gives command submit with valid path \"(.*?)\" and exercise \"(.*?)\"$")
    public void user_gives_command_submit_with_valid_path_and_exercise(String pathFromProjectRoot, String exercise) throws Throwable {
        testClient.init();
        String submitCommand = "submit path ";
        String submitPath = System.getProperty("user.dir") + pathFromProjectRoot + "/" + exercise;
        final String message = submitCommand + submitPath;
        testClient.sendMessage(message);
    }

    @Then("^user will see all test passing$")
    public void user_will_see_all_test_passing() throws Throwable {
        String result = testClient.reply();
        assertTrue(result.contains("All tests passed"));
    }

    @Then("^user will see the some test passing$")
    public void user_will_see_the_some_test_passing() throws Throwable {
        System.out.println(testClient.reply());
    }

    @After
    public void closeAll() throws IOException {
        server.close();
        serverThread.interrupt();
        wireMockServer.stop();
        configHandler.writeServerAddress("http://tmc.mooc.fi/staging");
        ClientData.clearUserData();
    }
}
