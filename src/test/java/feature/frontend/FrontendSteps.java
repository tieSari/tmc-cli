package feature.frontend;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import hy.tmc.cli.Configuration.ClientData;
import hy.tmc.cli.frontend_communication.Server.Server;
import hy.tmc.cli.testhelpers.TestClient;
import java.io.IOException;

import static org.junit.Assert.assertTrue;

public class FrontendSteps {

    private final int port = ClientData.getPORT(); // change if necessary

    private Thread serverThread;
    private Server server;
    private TestClient testClient;

    @Before
    public void setUpServer() throws IOException {
        server = new Server(port, null);
        serverThread = new Thread(server);
        serverThread.start();
        testClient = new TestClient(port);
    }

    @Given("^a help command\\.$")
    public void a_help_command() throws Throwable {
        testClient.sendMessage("help");
    }

    @Then("^output should contains commands\\.$")
    public void output_should_contains_commands() throws Throwable {
        
        String contents = testClient.reply();
        assertTrue(contents.contains("help"));
    
    }

    @After
    public void closeServer() throws IOException {
        server.close();
        serverThread.interrupt();
    }
}
