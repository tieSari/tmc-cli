package feature.paste;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.junit.WireMockRule;

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

import org.hamcrest.CoreMatchers;
import org.junit.Rule;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Date;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.containing;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class PasteSteps {

    private static final String SERVER_URI = "127.0.0.1";
    private static final int SERVER_PORT = 8080;
    private static final String SERVER_ADDRESS = "http://" + SERVER_URI + ":" + SERVER_PORT;
    private final String coursesExtension;
    @Rule WireMockRule wireMockRule = new WireMockRule();
    private TestClient testClient;
    private WireMockServer wireMockServer;
    private String pasteCommand;
    private TmcCli tmcCli;
    private UrlHelper urlHelper;

    public PasteSteps() throws IOException {
        CliSettings settings = new CliSettings();
        settings.setServerAddress(SERVER_ADDRESS);
        this.urlHelper = new UrlHelper(settings);
        coursesExtension = urlHelper.withParams("/courses.json");
    }

    @Before
    public void initializeServer() throws IOException, TmcCoreException {

        tmcCli = new TmcCli(false);

        tmcCli.setServer(SERVER_ADDRESS);
        tmcCli.startServer();
        testClient = new TestClient(new ConfigHandler().readPort());

        new ConfigHandler().writeLastUpdate(new Date());
        startWireMock();
    }

    @After
    public void closeAll() throws IOException, InterruptedException {
        tmcCli.stopServer();
        tmcCli.setServer("https://tmc.mooc.fi/staging");
        wireMockServer.stop();
        new File(new ConfigHandler().getConfigFilePath()).delete();
    }

    private void startWireMock() {
        wireMockServer = new WireMockServer();
        wireMockServer.start();

        wireMockServer.stubFor(
            get(urlEqualTo("/user")).withHeader("Authorization", containing("Basic dGVzdDoxMjM0"))
                .willReturn(aResponse().withStatus(200)));
        wiremockGET("/courses.json", ExampleJson.allCoursesExample);
        wiremockGET("/courses/3.json", ExampleJson.courseExample);
        wiremockPOSTwithPaste("/exercises/286/submissions.json", ExampleJson.pasteResponse);
        wiremockPOST("/exercises/286/submissions.json", ExampleJson.pasteResponse);
        wiremockGET("/submissions/1781.json", ExampleJson.successfulSubmission);
    }

    private void wiremockGET(String urlToMock, final String returnBody) {
        urlToMock = urlHelper.withParams(urlToMock);
        wireMockServer
            .stubFor(get(urlEqualTo(urlToMock)).willReturn(aResponse().withBody(returnBody)));
    }

    private void wiremockPOST(String urlToMock, final String returnBody) {
        urlToMock = urlHelper.withParams(urlToMock);
        wireMockServer
            .stubFor(post(urlEqualTo(urlToMock)).willReturn(aResponse().withBody(returnBody)));
    }

    private void wiremockPOSTwithPaste(String urlToMock, String returnBody) {
        urlToMock = urlHelper.withParams(urlToMock) + "&paste=1";
        wireMockServer
            .stubFor(post(urlEqualTo(urlToMock)).willReturn(aResponse().withBody(returnBody)));
    }

    @Given("^user has logged in with username \"(.*?)\" and password \"(.*?)\"$")
    public void user_has_logged_in_with_username_and_password(String username, String password)
        throws Throwable {
        testClient.sendMessage("login username " + username + " password " + password);
        String result = testClient.getAllFromSocket();
        testClient.init();
        assertTrue(result.contains("Auth successful. Saved userdata in session"));
    }

    @When("^user gives command paste with valid path \"(.*?)\" and exercise \"(.*?)\"$")
    public void user_gives_command_paste_with_valid_path_and_exercise(String path, String exercise)
        throws Throwable {
        this.pasteCommand = "paste path ";
        String pastePath = System.getProperty("user.dir") + path + File.separator + exercise;
        pastePath = Paths.get(pastePath).toString();
        this.pasteCommand = pasteCommand + pastePath;
    }

    @When("^flag \"(.*?)\"$")
    public void flag(String flag) throws Throwable {
        this.pasteCommand += " " + flag;
    }

    @When("^user executes the command$")
    public void user_executes_the_command() throws Throwable {
        testClient.sendMessage(pasteCommand);
    }

    @Then("^user will see the paste url$")
    public void user_will_see_the_paste_url()
        throws Throwable {
        String result = testClient.getAllFromSocket();
        assertThat(result, CoreMatchers.containsString("Paste submitted"));
    }

    private void assertContains(String testedString, String expectedContent) {
        assertTrue(testedString.contains(expectedContent));
    }
}
