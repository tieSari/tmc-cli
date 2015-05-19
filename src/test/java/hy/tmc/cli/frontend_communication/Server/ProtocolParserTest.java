/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hy.tmc.cli.frontend_communication.Server;

import helpers.FrontendMock;
import hy.tmc.cli.frontend_communication.Commands.Command;
import hy.tmc.cli.logic.Logic;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;


public class ProtocolParserTest {
    
    private FrontendMock server;
    private Logic logic;
    
    public ProtocolParserTest() {
        this.logic = new Logic();
        
    }
    
    @Before
    public void startServer(){
       this.server = new FrontendMock(); 
       this.server.start();
    }

    /**
     * Test of getCommand method, of class ProtocolParser.
     */
    @Test
    public void testGetCommand() throws Exception {
        // System.out.println("getCommand");
        String inputLine = "help";
        ProtocolParser instance = new ProtocolParser(this.server, this.logic);
        Command result = instance.getCommand(inputLine);
        assertNotNull(result);
    }
    
    @Test (expected=ProtocolException.class)
    public void testInvalidData() throws ProtocolException {
        String inputLine = "";
        ProtocolParser instance = new ProtocolParser(this.server, this.logic);
        Command result = instance.getCommand(inputLine);
        
    }
    
    @Test 
    public void testGiveData() throws ProtocolException {
        String inputLine = "echo data testi";
        ProtocolParser instance = new ProtocolParser(this.server, this.logic);
        Command echo = instance.getCommand(inputLine);
        try {
            echo.checkData();
        } catch(ProtocolException p){
            fail("testCheckDataSuccess failed");
        }     
    }
    
    
    
}

