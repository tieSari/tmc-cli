package feature.login;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import hy.tmc.cli.Configuration.ClientData;
import hy.tmc.cli.frontend_communication.Server.Server;
import hy.tmc.cli.testhelpers.TestClient;
import java.io.IOException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class LoginSteps {

    private final int port = ClientData.getPORT();

    private Thread serverThread;
    private TestClient testClient;
    private Server server;

    @Before
    public void initializeServer() throws IOException {
        server = new Server(port, null);
        serverThread = new Thread(server);
        serverThread.start();
        testClient = new TestClient(port);

    }


    @When("^user gives username \"(.*?)\" and password \"(.*?)\"$")
    public void user_gives_username_and_password(String username, String password) throws Throwable {
        testClient.sendMessage("login username " + username + " password " + password);
    }

    @Then("^user should see result\\.$")
    public void user_should_see_result() {
        assertTrue(!testClient.reply().isEmpty());
    }

    @After
    public void closeAll() throws IOException {
        server.close();
        serverThread.interrupt();
    }
}
