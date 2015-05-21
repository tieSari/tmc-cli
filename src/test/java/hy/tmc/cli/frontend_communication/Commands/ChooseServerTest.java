package hy.tmc.cli.frontend_communication.Commands;

import hy.tmc.cli.Configuration.ConfigHandler;
import hy.tmc.cli.frontend_communication.Server.ProtocolException;
import hy.tmc.cli.logic.Logic;
import hy.tmc.cli.testhelpers.FrontendStub;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

public class ChooseServerTest {

    private ChooseServer chooser;
    private final FrontendStub frontendMock;
    private final Logic logic;
    private final String path = "testResources/test.properties";

    public ChooseServerTest() {
        this.logic = new Logic();
        this.frontendMock = new FrontendStub();
    }

    @Before
    public void setup() {
        this.chooser = new ChooseServer(new ConfigHandler(path), 
                this.frontendMock, this.logic);
    }
    
    @After
    public void teardown(){
        new File(path).delete();
    }

    @Test
    public void createNewHelp() {
        assertNotNull(chooser);
    }

    @Test
    public void testFunctionality() {
        chooser.setParameter("tmc-server", "http://tmc.ebin.fi");
        chooser.functionality();
        try {
            String propFile = FileUtils.readFileToString(new File(path));
            assertTrue(propFile.contains("tmc.ebin.fi"));
        }
        catch (IOException ex) {
            fail("unable to read propertiesfile");
        }
    }
    
    @Test (expected = ProtocolException.class)
    public void throwsExceptionWithoutData() throws ProtocolException{
        chooser.checkData();
    }
    
    @Test
    public void correctURLisAccepted() {
        chooser.setParameter("tmc-server", "http://tmc.mooc.fi");
        try {
            chooser.checkData();
        }
        catch (ProtocolException ex) {
            fail("checkData threw exception");
        }
    }
    
    @Test (expected = ProtocolException.class)
    public void incorrectURLThrowsException() throws ProtocolException {
        chooser.setParameter("tmc-server", "lak3jf02ja3fji23j");
        chooser.checkData();
    }
}
