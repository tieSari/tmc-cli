package feature.listexercises;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import cucumber.api.PendingException;
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
    private String path = "/tmc-cli-client/testResources/2013_ohpeJaOhja/viikko1/Viikko1_002.HeiMaailma";

    private TestClient testClient;
    private boolean testThrown;

    private ConfigHandler configHandler; // writes the test address
    private WireMockServer wireMockServer;

    private static final String SERVER_URI = "127.0.0.1";
    private static final int SERVER_PORT = 7777;
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
        System.out.println(port);
        serverThread = new Thread(server);
        serverThread.start();
        testClient = new TestClient(port);

        startWireMock();
    }

    private void startWireMock() {
        wireMockServer = new WireMockServer(wireMockConfig().port(SERVER_PORT));
        WireMock.configureFor(SERVER_URI, SERVER_PORT);
        wireMockServer.start();
        wireMockServer.stubFor(get(urlEqualTo("/user"))
                .willReturn(
                        aResponse()
                        .withStatus(200)
                )
        );
        wireMockServer.stubFor(get(urlEqualTo(new ConfigHandler().coursesExtension))
                .willReturn(
                        aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(ExampleJson.allCoursesExample)
                )
        );
        wireMockServer.stubFor(get(urlEqualTo(new ConfigHandler().getCourseUrl(3)))
                .willReturn(
                        aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(ExampleJson.courseExample)
                )
        );

    }

    @After
    public void tearDown() {
        ClientData.clearUserData();
    }

    @Given("^user has logged in with username \"(.*?)\" and password \"(.*?)\"\\.$")
    public void user_has_logged_in_with_username_and_password(String username, String password) throws Throwable {
        ClientData.setUserData(username, password);
        
    }

    @When("^user gives command listExercises with path \"(.*?)\"\\.$")
    public void user_gives_command_listExercises_with_path(String arg1) throws Throwable {
        testClient.sendMessage("listExercises path " + path);
    }

    @Then("^output should contain more than one line$")
    public void output_should_contain_more_than_one_line() throws Throwable {
        String reply = testClient.reply();
        assertTrue(reply.contains("viikko1-Viikko1_000.Hiekkalaatikko"));
        assertTrue(reply.contains("viikko1-Viikko1_001.Nimi [ ]"));
    }

    @Given("^user has not logged in$")
    public void user_has_not_logged_in() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @When("^user writes listExercises\\.$")
    public void user_writes_listExercises() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @Then("^exception should be thrown$")
    public void exception_should_be_thrown() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @Given("^user doesn't have a valid course in his path\\.$")
    public void user_doesn_t_have_a_valid_course_in_his_path() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @Then("^output should contain names of exercises\\.$")
    public void output_should_contain_names_of_exercises() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

}
