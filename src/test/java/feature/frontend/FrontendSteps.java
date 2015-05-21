package feature.frontend;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import hy.tmc.cli.frontend_communication.FrontendListener;
import hy.tmc.cli.frontend_communication.Server.Server;
import hy.tmc.cli.testhelpers.Helper;
import hy.tmc.cli.testhelpers.TestClient;
import hy.tmc.cli.testhelpers.buildhelpers.JarBuilder;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import static org.junit.Assert.assertTrue;

public class FrontendSteps {

    private final int port = 1234; // change if necessary
    
    private Thread server;
    private final Helper helper = new Helper();


    @Given("^a help command\\.$")
    public void a_help_command() throws Throwable {
        
        Server s = new Server(1234, null);
        server = new Thread(s);
        server.start();
    }

    @Then("^output should contains commands\\.$")
    public void output_should_contains_commands() throws Throwable {
        
        TestClient testClient = new TestClient(port);
        testClient.sendMessage("help");
        String contents = testClient.reply();
        
        server.interrupt();
        assertTrue(contents.contains("help"));
    }
}
