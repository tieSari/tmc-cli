package feature.login;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.Assert.assertThat;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.junit.WireMockRule;

import hy.tmc.cli.TmcCli;
import hy.tmc.cli.configuration.ConfigHandler;
import hy.tmc.cli.testhelpers.TestClient;

import hy.tmc.core.TmcCore;
import hy.tmc.core.communication.authorization.Authorization;
import org.hamcrest.CoreMatchers;
import org.junit.Rule;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import hy.tmc.cli.testhelpers.TestFuture;
import hy.tmc.core.configuration.TmcSettings;
import hy.tmc.core.domain.Course;
import hy.tmc.core.domain.Exercise;
import hy.tmc.core.exceptions.TmcCoreException;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import static org.mockito.Matchers.any;
import org.mockito.Mockito;
import static org.mockito.Mockito.when;


public class LoginSteps {

    private int port;

    private TestClient testClient;

    private TmcCli tmcCli;
    private WireMockServer wireMockServer;

    private static final String SERVER_URI = "127.0.0.1";
    private static final int SERVER_PORT = 8080;
    private static final String SERVER_ADDRESS = "http://" + SERVER_URI + ":" + SERVER_PORT;

    @Rule
    WireMockRule wireMockRule = new WireMockRule();

    /**
     * Initialize server, set address and start thread.
     * @throws IOException if server creating fails
     */
    @Before
    public void initializeServer() throws IOException, TmcCoreException {
        //TmcCore core = Mockito.mock(TmcCore.class);
        TmcCore core = new TmcCore();
        List<Exercise> exerciseList = new ArrayList<Exercise>();
        TestFuture f = new TestFuture(exerciseList);
        
        tmcCli = new TmcCli(core, false);
        tmcCli.setServer(SERVER_ADDRESS);
        tmcCli.startServer();
        port = new ConfigHandler().readPort();
        testClient = new TestClient(port);
        
        new ConfigHandler().writeLastUpdate(new Date());

        startWireMock();
    }

    private void startWireMock() {
        wireMockServer = new WireMockServer();
        wireMockServer.start();
    }

    private void wiremockGetWithUsernamePasswordAndStatus(String username, String password, int status) {
        String auth = Authorization.encode(username+":"+password);
        wireMockServer.stubFor(get(urlEqualTo("/user"))
                        .withHeader("Authorization", containing("Basic " + auth))
                        .willReturn(
                                aResponse()
                                        .withStatus(status)
                        )
        );
    }

    @When("^user gives username \"(.*?)\" and password \"(.*?)\" and status (\\d+)$")
    public void user_gives_username_and_password_and_status(String username, String password, int status) throws Throwable {
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
     * @throws IOException if server closing fails
     */
    @After
    public void closeAll() throws IOException {
        tmcCli.stopServer();
        wireMockServer.stop();
        tmcCli.setServer("https://tmc.mooc.fi/staging");
    }
}
