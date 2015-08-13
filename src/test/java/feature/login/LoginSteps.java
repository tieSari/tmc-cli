package feature.login;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.junit.WireMockRule;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import fi.helsinki.cs.tmc.core.TmcCore;
import fi.helsinki.cs.tmc.core.communication.authorization.Authorization;
import fi.helsinki.cs.tmc.core.exceptions.TmcCoreException;

import hy.tmc.cli.TmcCli;
import hy.tmc.cli.configuration.ConfigHandler;
import hy.tmc.cli.testhelpers.TestClient;

import org.hamcrest.CoreMatchers;
import org.junit.Rule;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.containing;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;

import static org.junit.Assert.assertThat;

public class LoginSteps {

    private static final String SERVER_URI = "127.0.0.1";
    private static final int SERVER_PORT = 8080;
    private static final String SERVER_ADDRESS = "http://" + SERVER_URI + ":" + SERVER_PORT;
    @Rule WireMockRule wireMockRule = new WireMockRule();
    private int port;
    private TestClient testClient;
    private TmcCli tmcCli;
    private WireMockServer wireMockServer;

    /**
     * Initialize server, set address and start thread.
     *
     * @throws IOException if server creating fails
     */
    @Before
    public void initializeServer() throws IOException, TmcCoreException {
        //TmcCore core = Mockito.mock(TmcCore.class);
        TmcCore core = new TmcCore();

        tmcCli = new TmcCli(core, false);
        tmcCli.setServer(SERVER_ADDRESS);
        tmcCli.startServer();
        port = new ConfigHandler().readPort();
        testClient = new TestClient(port);

        new ConfigHandler().writeLastUpdate(new Date());

        startWireMock();
    }

    @After
    public void clean() throws IOException {
        new File(new ConfigHandler().getConfigFilePath()).delete();
    }

    private void startWireMock() {
        wireMockServer = new WireMockServer();
        wireMockServer.start();
    }

    private void wiremockGetWithUsernamePasswordAndStatus(String username, String password,
        int status) {
        String auth = Authorization.encode(username + ":" + password);
        wireMockServer.stubFor(
            get(urlEqualTo("/user")).withHeader("Authorization", containing("Basic " + auth))
                .willReturn(aResponse().withStatus(status)));
    }

    @When("^user gives username \"(.*?)\" and password \"(.*?)\" and status (\\d+)$")
    public void user_gives_username_and_password_and_status(String username, String password,
        int status) throws Throwable {
        wiremockGetWithUsernamePasswordAndStatus(username, password, status);
        testClient.sendMessage("login username " + username + " password " + password);
    }

    @Then("^user should see result \"(.*?)\"$")
    public void user_should_see_result(String expectedResult) throws Throwable {
        String result = testClient.getAllFromSocket();
        assertThat(result, CoreMatchers.containsString(expectedResult));
    }

    /**
     * Close server and wiremock after test.
     *
     * @throws IOException if server closing fails
     */
    @After 
    public void closeAll() throws IOException {
        tmcCli.stopServer();
        wireMockServer.stop();
        tmcCli.setServer("https://tmc.mooc.fi/staging");
    }
}
