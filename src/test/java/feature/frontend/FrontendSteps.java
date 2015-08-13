package feature.frontend;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;

import fi.helsinki.cs.tmc.core.TmcCore;

import hy.tmc.cli.TmcCli;
import hy.tmc.cli.configuration.ConfigHandler;
import hy.tmc.cli.frontend.communication.server.Server;
import hy.tmc.cli.testhelpers.TestClient;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import static org.junit.Assert.assertTrue;

public class FrontendSteps {

    private static final String SERVER_URI = "127.0.0.1";
    private static final int SERVER_PORT = 8080;
    private static final String SERVER_ADDRESS = "http://" + SERVER_URI + ":" + SERVER_PORT;
    File cache;
    private int port;
    private Thread serverThread;
    private Server server;
    private TestClient testClient;
    private TmcCli tmcCli;

    /**
     * Set up server and testclient.
     *
     * @throws IOException if server initializing fails
     */
    @Before
    public void setUpServer() throws IOException {
        serverThread = new Thread(server);
        serverThread.start();
        new ConfigHandler().writePort(SERVER_PORT);
        port = new ConfigHandler().readPort();
        cache = new File("cache");
        cache.createNewFile();

        tmcCli = new TmcCli(new TmcCore(cache), false);
        tmcCli.setServer(SERVER_ADDRESS);
        tmcCli.startServer();
        testClient = new TestClient(new ConfigHandler().readPort());
        new ConfigHandler().writeLastUpdate(new Date());
    }

    @Given("^help command\\.$")
    public void help_command() throws Throwable {
        testClient.sendMessage("help");
    }

    /**
     * Tests that output contains available commands.
     *
     * @throws Throwable if something fails
     */
    @Then("^output should contains commands\\.$")
    public void output_should_contains_commands()
        throws Throwable {
        String contents = testClient.reply();
        assertTrue(contents.contains("Commands:"));
    }

    @Given("^show settings command\\.$")
    public void show_settings_command() throws Throwable {
        testClient.sendMessage("showSettings");
    }

    @Then("^output should contains settings information\\.$")
    public void output_should_contains_settings_information() throws Throwable {
        String contents = testClient.getAllFromSocket();
        System.out.println("Contents: " + contents);
        assertTrue(contents.contains("Server address"));
    }

    @After
    public void closeServer() throws IOException {
        serverThread.interrupt();
        tmcCli.stopServer();
        cache.delete();
        new File(new ConfigHandler().getConfigFilePath()).delete();
    }
}
