/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hy.tmc.cli.frontend_communication.Commands;

import hy.tmc.cli.Configuration.ClientData;
import hy.tmc.cli.frontend_communication.Server.ProtocolException;
import hy.tmc.cli.frontend_communication.Server.Server;
import hy.tmc.cli.frontend_communication.Server.ServerTest;
import hy.tmc.cli.frontend_communication.Server.TestClient;
import hy.tmc.cli.logic.Logic;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author chang
 */
public class ListCoursesTest {

    private Server server;
    private Logic logic;
    private TestClient client;
    
    public ListCoursesTest() {
        this.logic = new Logic();
    }

    @Before
    public void startServer() {
        this.server = new Server(8035, logic);
        this.server.start();
        
         try {
            client = new TestClient(8035);
        } catch (IOException ex) {
             System.out.println("fail");
        }
    }
    
     @Test
    public void createNewEcho() {
       ListCourses lc = new ListCourses(this.server, new Logic());
       assertNotNull(lc);
    }
    
   @Test 
    public void testCheckDataSuccess() throws ProtocolException{
        ListCourses ls = new ListCourses(this.server, new Logic());
        ls.setParameter("", "juuh");
        try {
            ls.checkData();
        } catch(ProtocolException p) {
            fail("testCheckDataSuccess failed");
        }
    }
    
    @Test
    public void testServerRepliesToPing() {
        ClientData.setUserData("", "");
        
        try {
            client.sendMessage("ping");
            assertEquals("pong", client.reply());
        } catch (IOException ex) {
            Logger.getLogger(ServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail("IOException was raised");
        }
    }
    
//    @Test
//    public void testNoAuthPrintsError() {
//        try {
//            client.sendMessage("listCourses");
//            assertTrue(client.reply().contains("authorize first"));
//        }
//        catch (IOException ex) {
//            fail("Invalid response");
//        }
//    }
//    
//    @Test
//    public void testWithAuthPrintsCourses() {
//        ClientData.setUserData("test", "1234");
//        
//        try {
//            client.sendMessage("listCourses");
//            assertTrue(client.reply().contains("tira"));
//        }
//        catch (IOException ex) {
//            fail("Invalid response");
//        }
//    }
    

    @After
    public void closeServer() {
        try {
            this.server.close();
        }
        catch (IOException ex) {
            fail("Closing server failed");
        }
    }

}
