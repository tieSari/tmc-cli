/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hy.tmc.cli.frontend_communication.Server;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author ilari
 */
public class ServerTest {
    
    private ServerBottle server;
    private TestClient client;
    
    public ServerTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        server = new ServerBottle(new Server(1234, null));
        client = new TestClient(1234);
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of start method, of class Server.
     */
    @Test
    public void testServerRepliesToPing() {
        server.start();
        String answer = client.send("ping");
        server.stop();
        assertEquals("pong", answer);
    }


    
}
