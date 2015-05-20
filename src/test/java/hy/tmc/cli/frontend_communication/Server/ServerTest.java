package hy.tmc.cli.frontend_communication.Server;

import hy.tmc.cli.testhelpers.TestClient;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class ServerTest {

    private Server server;
    private TestClient client;
    private Thread serverThread;


    @Before
    public void setUp() {
        int port = 4321;
        server = new Server(port, null);
        //server.start();
        this.serverThread = new Thread(server);
        this.serverThread.start();
        try {
            client = new TestClient(port);
        } catch (IOException ex) {
            Logger.getLogger(ServerTest.class.getName()).log(Level.SEVERE, null, ex);
        }
         
    }

    @After
    public void tearDown() {
        try {
            server.close();
            serverThread.interrupt();
        } catch (IOException ex) {
            Logger.getLogger(ServerTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Test of start method, of class Server.
     */
    @Test
    public void testServerRepliesToPing() {
        try {
            client.sendMessage("ping");
            assertEquals("pong", client.reply());
        } catch (IOException ex) {
            Logger.getLogger(ServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail("IOException was raised");
        }
    }
    
    @Test
    public void messageViolatesProtocolTest() {
        try {
            client.sendMessage("al2kjn238fh1o");
            assertEquals(Server.PROTOCOL_ERROR_MSG, client.reply());
        } catch (IOException ex) {
            Logger.getLogger(ServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail("IOException was raised");
        }
    }

}

