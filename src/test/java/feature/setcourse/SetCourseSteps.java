package feature.setcourse;

import com.github.tomakehurst.wiremock.WireMockServer;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import hy.tmc.cli.TmcCli;
import hy.tmc.cli.configuration.ConfigHandler;
import hy.tmc.cli.testhelpers.ExampleJson;
import hy.tmc.cli.testhelpers.TestClient;
import fi.helsinki.cs.tmc.core.TmcCore;
import fi.helsinki.cs.tmc.core.communication.authorization.Authorization;
import org.hamcrest.CoreMatchers;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

import java.util.Date;
import hy.tmc.cli.CliSettings;
import fi.helsinki.cs.tmc.core.communication.UrlHelper;
import java.io.File;

import static org.junit.Assert.assertThat;

public class SetCourseSteps {

    private TestClient testClient;
    private WireMockServer wireMockServer;

    private TmcCli tmcCli;

    private static final String SERVER_URI = "127.0.0.1";
    private static final int SERVER_PORT = 8080;
    private static final String SERVER_ADDRESS = "http://" + SERVER_URI + ":" + SERVER_PORT;

    @Before
    public void setUp() throws Exception {
        tmcCli = new TmcCli(new TmcCore(), false);
        tmcCli.setServer(SERVER_ADDRESS);
        tmcCli.startServer();
        testClient = new TestClient(new ConfigHandler().readPort());
        new ConfigHandler().writeLastUpdate(new Date());
    }
    
    @After
    public void clean(){
        new File("config.properties").delete();
    }

    @Given("^user has logged in with username \"(.*?)\" and password \"(.*?)\"$")
    public void user_has_logged_in_with_username_and_password(String username, String password) throws Throwable {
        startWireMock(username, password);
        testClient.sendMessage("login username " + username + " password " + password);
        String result = testClient.getAllFromSocket();
        testClient.init();
        assertThat(result, CoreMatchers.containsString("Auth successful. Saved userdata in session"));
    }

    @When("^user gives command \"(.*?)\"$")
    public void user_gives_command(String command) throws Throwable {
        testClient.sendMessage(command);
    }

    @Then("^user will see result \"(.*?)\"$")
    public void user_will_see_result(String expectedOutput) throws Throwable {
        assertThat(testClient.reply(), CoreMatchers.containsString(expectedOutput));
    }

    @After
    public void clear() throws Exception {
        tmcCli.stopServer();
        tmcCli.setServer("https://tmc.mooc.fi/staging");
        wireMockServer.stop();
    }

    private void startWireMock(String username, String password) {
        wireMockServer = new WireMockServer();
        wireMockServer.start();

        wireMockServer.stubFor(get(urlEqualTo("/user"))
                .withHeader("Authorization", containing("Basic " + Authorization.encode(username + ":" + password)))
                .willReturn(aResponse().withStatus(200)));
        wiremockGET("/courses.json", ExampleJson.allCoursesExample);
        wiremockGET("/courses/21.json", ExampleJson.courseExample);
    }

    private void wiremockGET(String urlToMock, final String returnBody) {
        CliSettings settings = new CliSettings();
        settings.setServerAddress(SERVER_ADDRESS);
        urlToMock = new UrlHelper(settings).withParams(urlToMock);
        wireMockServer.stubFor(get(urlEqualTo(urlToMock))
                .willReturn(aResponse()
                        .withBody(returnBody)
                )
        );
    }
}
