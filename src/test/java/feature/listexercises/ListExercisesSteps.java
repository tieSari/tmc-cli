package feature.listexercises;

import com.github.tomakehurst.wiremock.WireMockServer;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import fi.helsinki.cs.tmc.core.TmcCore;
import fi.helsinki.cs.tmc.core.communication.UrlHelper;

import fi.helsinki.cs.tmc.core.exceptions.TmcCoreException;
import hy.tmc.cli.CliSettings;
import hy.tmc.cli.TmcCli;
import hy.tmc.cli.configuration.ConfigHandler;
import hy.tmc.cli.testhelpers.ExampleJson;
import hy.tmc.cli.testhelpers.TestClient;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.Date;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

import static org.junit.Assert.assertTrue;

public class ListExercisesSteps {

    private static final String SERVER_URI = "127.0.0.1";
    private static final int SERVER_PORT = 8080;
    private static final String SERVER_ADDRESS = "http://" + SERVER_URI + ":" + SERVER_PORT;
    private final String coursesExtension;
    private TmcCli tmcCli;
    private TestClient testClient;
    private ConfigHandler configHandler; // writes the test address
    private WireMockServer wireMockServer;
    private UrlHelper urlHelper;

    public ListExercisesSteps() throws IOException, URISyntaxException {
        CliSettings settings = new CliSettings();
        settings.setServerAddress(SERVER_ADDRESS);
        this.urlHelper = new UrlHelper(settings);
        coursesExtension = urlHelper.withParams("/courses.json");
    }

    /**
     * Setups client's config and starts WireMock.
     */
    @Before
    public void setUpServer() throws IOException, TmcCoreException, URISyntaxException {
        tmcCli = new TmcCli(false);
        tmcCli.setServer(SERVER_ADDRESS);
        tmcCli.startServer();

        testClient = new TestClient(new ConfigHandler().readPort());

        new ConfigHandler().writeLastUpdate(new Date());
        startWireMock();
    }

    @After
    public void closeServer() throws IOException {
        tmcCli.stopServer();
        wireMockServer.stop();
        new File(new ConfigHandler().getConfigFilePath()).delete();
    }

    @Given("^user has not logged in$")
    public void user_has_not_logged_in() throws Throwable {
    }

    private void startWireMock() throws URISyntaxException {
        wireMockServer = new WireMockServer(wireMockConfig().port(SERVER_PORT));
        wireMockServer.start();
        wireMockServer.stubFor(get(urlEqualTo("/user")).willReturn(aResponse().withStatus(200)));
        wireMockServer.stubFor(get(urlEqualTo(coursesExtension)).willReturn(
                aResponse().withStatus(200).withHeader("Content-Type", "application/json")
                    .withBody(ExampleJson.allCoursesExample)));
        String mockUrl = urlHelper.withParams("/courses/3.json");
        wireMockServer.stubFor(get(urlEqualTo(mockUrl)).willReturn(
                aResponse().withStatus(200).withHeader("Content-Type", "application/json")
                    .withBody(ExampleJson.courseExample)));

    }

    @Given("^user has logged in with username \"(.*?)\" and password \"(.*?)\"\\.$")
    public void user_has_logged_in_with_username_and_password(String username, String password)
        throws Throwable {
        testClient.sendMessage("login username " + username + " password " + password);
        testClient.getAllFromSocket();
        testClient.init();
    }

    @When("^user gives command listExercises with path \"(.*?)\"\\.$")
    public void user_gives_command_listExercises_with_path(String path) throws Throwable {
        path = Paths.get(path).toString();
        testClient.init();
        testClient.sendMessage("listExercises path " + path);
    }

    @Then("^output should contain \"(.*?)\"$")
    public void output_should_contain(String expectedOutput) throws Throwable {
        String result = testClient.getAllFromSocket();
        assertTrue(result.contains(expectedOutput));
    }
}
