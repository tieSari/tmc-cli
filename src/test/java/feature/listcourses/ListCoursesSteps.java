package feature.listcourses;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.containing;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.junit.Assert.assertThat;

import com.github.tomakehurst.wiremock.WireMockServer;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import fi.helsinki.cs.tmc.core.communication.UrlHelper;
import fi.helsinki.cs.tmc.core.exceptions.TmcCoreException;

import hy.tmc.cli.CliSettings;
import hy.tmc.cli.TmcCli;
import hy.tmc.cli.configuration.ConfigHandler;
import hy.tmc.cli.testhelpers.ExampleJson;
import hy.tmc.cli.testhelpers.TestClient;

import org.hamcrest.CoreMatchers;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;

public class ListCoursesSteps {

    private static final String SERVER_URI = "127.0.0.1";
    private static final int SERVER_PORT = 8080;
    private static final String SERVER_ADDRESS = "http://" + SERVER_URI + ":" + SERVER_PORT;
    private final URI coursesExtension;
    private TestClient testClient;
    private WireMockServer wireMockServer;
    private TmcCli tmcCli;

    public ListCoursesSteps() throws IOException, URISyntaxException {
        CliSettings settings = new CliSettings();
        settings.setServerAddress(SERVER_ADDRESS);
        coursesExtension = new UrlHelper(settings).withParams(new URI("/courses.json"));
    }

    /**
     * Setups client's config and starts WireMock.
     */
    @Before
    public void setUpServer() throws IOException, TmcCoreException {
        tmcCli = new TmcCli(false);
        tmcCli.setServer(SERVER_ADDRESS);
        tmcCli.startServer();
        testClient = new TestClient(new ConfigHandler().readPort());

        new ConfigHandler().writeLastUpdate(new Date());

        startWireMock();
    }

    private void startWireMock() {
        wireMockServer = new WireMockServer();
        wireMockServer.start();
        wireMockServer.stubFor(
            get(urlEqualTo("/user")).withHeader("Authorization", containing("Basic dGVzdDoxMjM0"))
                .willReturn(aResponse().withStatus(200)));
        wireMockServer.stubFor(get(urlEqualTo(coursesExtension.toString()))
                .withHeader("Authorization", containing("Basic dGVzdDoxMjM0")).willReturn(
                    aResponse().withStatus(200).withHeader("Content-Type", "application/json")
                        .withBody(ExampleJson.allCoursesExample)));
    }

    @Given("^user has logged in with username \"(.*?)\" and password \"(.*?)\"\\.$")
    public void user_has_logged_in_with_username_and_password(String username, String password)
        throws Throwable {
        testClient.sendMessage("login username " + username + " password " + password);
        testClient.getAllFromSocket();
        testClient.init();
    }

    @When("^user gives command listCourses\\.$")
    public void user_gives_command_listCourses() throws Throwable {
        testClient.sendMessage("listCourses");
    }

    @Then("^output should contain \"(.*?)\"$")
    public void output_should_contain(String expectedOutput) throws Throwable {
        String content = testClient.getAllFromSocket();
        assertThat(content, CoreMatchers.containsString(expectedOutput));
    }

    @Given("^user has not logged in$")
    public void user_has_not_logged_in() throws Throwable {
        testClient.init();
    }

    /**
     * Shuts down the server and WireMock after scenario.
     */
    @After
    public void closeServer() throws IOException {
        tmcCli.stopServer();
        wireMockServer.stop();
        new File(new ConfigHandler().getConfigFilePath()).delete();
    }
}
