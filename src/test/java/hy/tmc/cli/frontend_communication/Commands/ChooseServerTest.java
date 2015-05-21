package hy.tmc.cli.frontend_communication.Commands;

import hy.tmc.cli.frontend_communication.Server.ProtocolException;
import hy.tmc.cli.logic.Logic;
import hy.tmc.cli.testhelpers.FrontendStub;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

public class ChooseServerTest {

    private ChooseServer chooser;
    private FrontendStub frontendMock;
    private Logic logic;

    public ChooseServerTest() {
        this.logic = new Logic();
        this.frontendMock = new FrontendStub();
    }

    @Before
    public void setup() {
        this.chooser = new ChooseServer(this.frontendMock, this.logic);
    }

    @Test
    public void createNewHelp() {
        assertNotNull(chooser);
    }

    @Test
    public void testFunctionality() {
        chooser.functionality();
        String output = this.frontendMock.getMostRecentLine();
        assertTrue(output.contains("help"));
        assertTrue(output.contains("auth"));
    }
    
    @Test
    public void correctURLisAccepted() {
        chooser.setParameter("tmc-server", "http://tmc.mooc.fi");
        try {
            chooser.checkData();
        }
        catch (ProtocolException ex) {
            Logger.getLogger(ChooseServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail("checkData threw exception");
        }
    }
    
    @Test (expected = ProtocolException.class)
    public void incorrectURLThrowsException() throws ProtocolException {
        chooser.setParameter("tmc-server", "lak3jf02ja3fji23j");
        chooser.checkData();
    }
}
