/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hy.tmc.cli.frontend_communication.Server;

import hy.tmc.cli.frontend_communication.Commands.Command;
import hy.tmc.cli.logic.Logic;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
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
public class ProtocolParserTest {
    
    private Server server;
    private Logic logic;
    
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    
    public ProtocolParserTest() {
        this.logic = new Logic();
        this.server = new Server(1234, logic);
        
    }
    
    @Before
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @After
    public void cleanUpStreams() {
        System.setOut(null);
        System.setErr(null);
    }

    /**
     * Test of getCommand method, of class ProtocolParser.
     */
    @Test
    public void testGetCommand() throws Exception {
        System.out.println("getCommand");
        String inputLine = "help";
        ProtocolParser instance = new ProtocolParser(this.server, this.logic);
        Command result = instance.getCommand(inputLine);
        assertNotNull(result);
    }
    
    @Test (expected=ProtocolException.class)
    public void testInvalidData() throws ProtocolException{
        String inputLine = "";
        ProtocolParser instance = new ProtocolParser(this.server, this.logic);
        Command result = instance.getCommand(inputLine);
        
    }
    
}
