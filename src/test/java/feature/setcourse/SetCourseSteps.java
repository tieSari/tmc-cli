package feature.setcourse;

import com.github.tomakehurst.wiremock.WireMockServer;

import com.github.tomakehurst.wiremock.verification.LoggedRequest;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import fi.helsinki.cs.tmc.core.TmcCore;
import fi.helsinki.cs.tmc.core.communication.UrlHelper;
import fi.helsinki.cs.tmc.core.communication.authorization.Authorization;

import hy.tmc.cli.CliSettings;
import hy.tmc.cli.TmcCli;
import hy.tmc.cli.configuration.ConfigHandler;
import hy.tmc.cli.testhelpers.ExampleJson;
import hy.tmc.cli.testhelpers.TestClient;

import org.apache.http.client.utils.URIBuilder;
import org.hamcrest.CoreMatchers;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.containing;
import static com.github.tomakehurst.wiremock.client.WireMock.findAll;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;

import static com.github.tomakehurst.wiremock.client.WireMock.urlMatching;
import static fi.helsinki.cs.tmc.core.communication.TmcConstants.API_VERSION_PARAM;
import static fi.helsinki.cs.tmc.core.communication.TmcConstants.CLIENT_NAME_PARAM;
import static fi.helsinki.cs.tmc.core.communication.TmcConstants.CLIENT_VERSION_PARAM;
import static org.junit.Assert.assertThat;

public class SetCourseSteps {

    private static final String SERVER_URI = "127.0.0.1";
    private static final int SERVER_PORT = 8080;
    private static final String SERVER_ADDRESS = "http://" + SERVER_URI + ":" + SERVER_PORT;
    private TestClient testClient;
    private WireMockServer wireMockServer;
    private TmcCli tmcCli;

    @Before
    public void setUp() throws Exception {
        tmcCli = new TmcCli(false);
        tmcCli.setServer(SERVER_ADDRESS);
        tmcCli.startServer();
        testClient = new TestClient(new ConfigHandler().readPort());
        new ConfigHandler().writeLastUpdate(new Date());
    }

    @After
    public void clean() throws IOException {
        new File(new ConfigHandler().getConfigFilePath()).delete();
    }

    @Given("^user has logged in with username \"(.*?)\" and password \"(.*?)\"$")
    public void user_has_logged_in_with_username_and_password(String username, String password)
        throws Throwable {
        startWireMock(username, password);
        testClient.sendMessage("login username " + username + " password " + password);
        String result = testClient.getAllFromSocket();
        testClient.init();
        assertThat(result,
            CoreMatchers.containsString("Auth successful. Saved userdata in session"));
    }

    @When("^user gives command \"(.*?)\"$")
    public void user_gives_command(String command)
        throws Throwable {
        testClient.sendMessage(command);
    }

    @Then("^user will see result \"(.*?)\"$")
    public void user_will_see_result(String expectedOutput) throws Throwable {
        assertThat(testClient.reply(), CoreMatchers.containsString(expectedOutput));
    }

    @After
    public void clear() throws Exception {
        tmcCli.stopServer();
        tmcCli.setServer("");
        wireMockServer.stop();
    }

    private void startWireMock(String username, String password) throws URISyntaxException {
        wireMockServer = new WireMockServer();
        wireMockServer.start();

        wireMockServer.stubFor(get(urlEqualTo("/user")).withHeader("Authorization",
            containing("Basic " + Authorization.encode(username + ":" + password)))
            .willReturn(aResponse().withStatus(200)));
        wiremockGET("/courses.json", ExampleJson.allCoursesExample);
        wiremockGET("/courses/21.json", ExampleJson.courseExample);
    }

    private void wiremockGET(String urlToMock, final String returnBody) throws URISyntaxException {
        CliSettings settings = new CliSettings();
        urlToMock = new URIBuilder(urlToMock)
            .addParameter("api_version", settings.apiVersion())
            .addParameter("client", settings.clientName())
            .addParameter("client_version", settings.clientVersion())
            .build().toString();

        settings.setServerAddress(SERVER_ADDRESS);

        System.out.println(urlToMock);
        wireMockServer
            .stubFor(get(urlEqualTo(urlToMock)).willReturn(aResponse().withBody(returnBody)));
    }
}
