package feature.listexercises;

import com.github.tomakehurst.wiremock.WireMockServer;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
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

public class ListExercisesSteps {

    private int port;

    private Thread serverThread;
    private Server server;

    private TestClient testClient;

    private ConfigHandler configHandler; // writes the test address
    private WireMockServer wireMockServer;

    private static final String SERVER_URI = "127.0.0.1";
    private static final int SERVER_PORT = 8080;
    private static final String SERVER_ADDRESS = "http://" + SERVER_URI + ":" + SERVER_PORT;

    /**
     * Setups client's config and starts WireMock.
     */
    @Before
    public void setUpServer() throws IOException {
        configHandler = new ConfigHandler();
        configHandler.writeServerAddress(SERVER_ADDRESS);

        server = new Server();
        port = configHandler.readPort();
        serverThread = new Thread(server);
        serverThread.start();
        testClient = new TestClient(port);

        startWireMock();
    }

    private void startWireMock() {
        wireMockServer = new WireMockServer(wireMockConfig().port(SERVER_PORT));
        wireMockServer.start();
        wireMockServer.stubFor(get(urlEqualTo("/user"))
                .willReturn(
                        aResponse()
                        .withStatus(200)
                )
        );
        wireMockServer.stubFor(get(urlEqualTo(configHandler.coursesExtension))
                .willReturn(
                        aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(ExampleJson.allCoursesExample)
                )
        );
        wireMockServer.stubFor(get(urlEqualTo("/courses/3.json?api_version=7"))
                .willReturn(
                        aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(ExampleJson.courseExample)
                )
        );

    }

    @After
    public void closeServer() throws IOException {
        server.close();
        serverThread.interrupt();
        wireMockServer.stop();
        configHandler.writeServerAddress("http://tmc.mooc.fi/staging");
        ClientData.clearUserData();
    }

    @Given("^user has logged in with username \"(.*?)\" and password \"(.*?)\"\\.$")
    public void user_has_logged_in_with_username_and_password(String username, String password) throws Throwable {
        testClient.sendMessage("login username " + username + " password " + password);
        testClient.init();
    }

    @When("^user gives command listExercises with path \"(.*?)\"\\.$")
    public void user_gives_command_listExercises_with_path(String path) throws Throwable {
        testClient.sendMessage("listExercises path " + path);
    }

    @Then("^output should contain more than one line$")
    public void output_should_contain_more_than_one_line() throws Throwable {
        String reply = testClient.reply();
        StringBuilder replybuffer = new StringBuilder();
        while (reply != null) {
            replybuffer.append(reply);
            reply = testClient.reply();
        }
        String result = replybuffer.toString();
        assertTrue(result.contains("viikko1-Viikko1_000.Hiekkalaatikko"));
        assertTrue(result.contains("viikko1-Viikko1_001.Nimi"));
    }

    @Given("^user has not logged in$")
    public void user_has_not_logged_in() throws Throwable {
        ClientData.clearUserData();
    }

    @Then("^exception should be thrown$")
    public void exception_should_be_thrown() throws Throwable {
        String result = testClient.reply();
        assertTrue(result.contains("Please authorize first."));
    }
}
