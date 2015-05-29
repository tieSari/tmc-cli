package feature.login;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.containing;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;

import static org.junit.Assert.assertTrue;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.junit.WireMockRule;

import hy.tmc.cli.configuration.ConfigHandler;
import hy.tmc.cli.frontend.communication.server.Server;
import hy.tmc.cli.testhelpers.TestClient;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import java.io.IOException;

public class LoginSteps {

    private int port;

    private Thread serverThread;
    private TestClient testClient;
    private Server server;

    private ConfigHandler configHandler; // writes the test address
    private WireMockServer wireMockServer;

    @Rule
    WireMockRule wireMockRule = new WireMockRule();

    /**
     * Initialize server, set address and start thread.
     * @throws IOException if server creating fails
     */
    @Before
    public void initializeServer() throws IOException {
        configHandler = new ConfigHandler();
        configHandler.writeServerAddress("http://127.0.0.1:8080");

        server = new Server(null);
        port = new ConfigHandler().readPort();
        serverThread = new Thread(server);
        serverThread.start();
        testClient = new TestClient(port);

        startWireMock();
    }

    private void startWireMock() {
        wireMockServer = new WireMockServer();
        wireMockServer.start();

        stubFor(get(urlEqualTo("/user"))
                .withHeader("Authorization", containing("Basic dGVzdDoxMjM0"))
                .willReturn(
                        aResponse()
                        .withStatus(200)
                )
        );

    }

    @When("^user gives username \"(.*?)\" and password \"(.*?)\"$")
    public void user_gives_uname_and_password(String username, String password) throws Throwable {
        testClient.sendMessage("login username " + username + " password " + password);
    }

    @Then("^user should see result\\.$")
    public void user_should_see_result() {
        assertTrue(testClient.reply().contains("Saved userdata in session"));
    }

    /**
     * Close server and wiremock after test.
     * @throws IOException if server closing fails
     */
    @After
    public void closeAll() throws IOException {
        server.close();
        serverThread.interrupt();
        wireMockServer.stop();
        configHandler.writeServerAddress("http://tmc.mooc.fi/staging");
    }
}
