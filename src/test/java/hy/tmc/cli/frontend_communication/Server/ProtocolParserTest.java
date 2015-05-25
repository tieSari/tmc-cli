package hy.tmc.cli.frontend_communication.Server;

import hy.tmc.cli.frontend.communication.server.ProtocolParser;
import hy.tmc.cli.frontend.communication.server.ProtocolException;
import hy.tmc.cli.frontend.communication.commands.Command;
import hy.tmc.cli.logic.Logic;
import hy.tmc.cli.testhelpers.FrontendStub;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;


public class ProtocolParserTest {
    
    private FrontendStub server;
    private Logic logic;
    
    public ProtocolParserTest() {
        this.logic = new Logic();
        
    }
    
    @Before
    public void startServer(){
       this.server = new FrontendStub(); 
       this.server.start();
    }

    /**
     * Test of getCommand method, of class ProtocolParser.
     * @throws java.lang.Exception
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
        String inputLine = "login username asdf password bsdf";
        ProtocolParser instance = new ProtocolParser(this.server, this.logic);
        Command echo = instance.getCommand(inputLine);
        try {
            echo.checkData();
        } catch(ProtocolException p){
            fail("an exception was thrown although data was provided");
        }     
    }
    
    
    
}

