package feature.login;

import com.github.tomakehurst.wiremock.WireMockServer;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import hy.tmc.cli.configuration.ConfigHandler;
import hy.tmc.cli.frontend.communication.server.Server;
import hy.tmc.cli.testhelpers.TestClient;
import java.io.IOException;
import static org.junit.Assert.assertTrue;
import org.junit.Rule;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class LoginSteps {

    private int port;

    private Thread serverThread;
    private TestClient testClient;
    private Server server;

    private ConfigHandler configHandler; // writes the test address
    private WireMockServer wireMockServer;

    @Rule
    WireMockRule wireMockRule = new WireMockRule();

    @Before
    public void initializeServer() throws IOException {
        configHandler = new ConfigHandler();
        configHandler.writeServerAddress("http://127.0.0.1:8080");

        server = new Server();
        port = new ConfigHandler().readPort();
        serverThread = new Thread(server);
        serverThread.start();
        testClient = new TestClient(port);


        startWireMock();
    }

    private void startWireMock() {
        wireMockServer = new WireMockServer();
        wireMockServer.start();

        wireMockServer.stubFor(get(urlEqualTo("/user"))
                .withHeader("Authorization", containing("Basic dGVzdDoxMjM0"))
                .willReturn(
                        aResponse()
                        .withStatus(200)
                )
        );
    }

    @When("^user gives username \"(.*?)\" and password \"(.*?)\"$")
    public void user_gives_username_and_password(String username, String password) throws Throwable {
        testClient.sendMessage("login username " + username + " password " + password);
    }

    @Then("^user should see result\\.$")
    public void user_should_see_result() {
        assertTrue(testClient.reply().contains("Saved userdata in session"));
    }

    @After
    public void closeAll() throws IOException {
        server.close();
        serverThread.interrupt();
        wireMockServer.stop();
        configHandler.writeServerAddress("http://tmc.mooc.fi/staging");
    }
}
