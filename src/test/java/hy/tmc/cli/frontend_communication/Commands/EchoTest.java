/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hy.tmc.cli.frontend_communication.Commands;

import hy.tmc.cli.frontend_communication.FrontendListener;
import hy.tmc.cli.frontend_communication.Server.ProtocolException;
import hy.tmc.cli.frontend_communication.Server.ProtocolParserTest;
import hy.tmc.cli.frontend_communication.Server.Server;
import hy.tmc.cli.logic.Logic;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author pihla
 */
public class EchoTest {
    
    private Server server;
    private Logic logic;
    
    public EchoTest() {
        this.logic = new Logic();    
    }
    
    @Before
    public void startServer(){
       this.server = new Server(8034, logic); 
       this.server.start();
    }

    /**
     * Create new Echo
     */
    @Test
    public void createNewEcho() {
       Echo echo = new Echo(this.server, new Logic());
       assertNotNull(echo);
    }
    
    @Test (expected=ProtocolException.class)
    public void testCheckDataFails() throws ProtocolException{
        Echo echo = new Echo(this.server, new Logic());
        echo.checkData();
    }
    
    @Test 
    public void testCheckDataSuccess() throws ProtocolException{
        Echo echo = new Echo(this.server, new Logic());
        echo.setParameter("", "juuh");
        try {
            echo.checkData();
        } catch(ProtocolException p){
            fail("testCheckDataSuccess failed");
        }
    }
    
    @After
    public void closeServer(){
        try {
            this.server.close();
        } catch (IOException ex) {
            fail("Closing server failed");
        }
    }
    
}
