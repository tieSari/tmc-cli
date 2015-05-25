package hy.tmc.cli.frontend_communication.Server;

import hy.tmc.cli.Configuration.ConfigHandler;
import hy.tmc.cli.testhelpers.TestClient;
import java.io.IOException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Rule;
import org.junit.rules.ExpectedException;

public class ServerTest {

    private Server server;
    private TestClient client;
    private Thread serverThread;
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setUp() throws IOException {
        server = new Server(null);
        int port = new ConfigHandler().readPort();

        this.serverThread = new Thread(server);
        this.serverThread.start();
        client = new TestClient(port);
    }

    @After
    public void tearDown() throws IOException {
        server.close();
        serverThread.interrupt();
    }

    /**
     * Test of start method, of class Server.
     */
    @Test
    public void testServerRepliesToPing() throws IOException {
        client.sendMessage("ping");
        assertEquals("pong", client.reply());
    }

    @Test
    public void messageViolatesProtocolTest() throws IOException {
        client.sendMessage("al2kjn238fh1o");
        assertEquals(Server.PROTOCOL_ERROR_MSG, client.reply());
    }
}
