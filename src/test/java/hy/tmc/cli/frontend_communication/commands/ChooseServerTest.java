package hy.tmc.cli.frontend_communication.commands;

import hy.tmc.cli.configuration.ConfigHandler;
import hy.tmc.cli.frontend.communication.commands.ChooseServer;
import hy.tmc.cli.frontend.communication.server.ProtocolException;
import hy.tmc.cli.testhelpers.FrontendStub;
import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.Test;


public class ChooseServerTest {

    private ChooseServer chooser;
    private final FrontendStub frontendMock;
    private final String path = "testResources/test.properties";

    public ChooseServerTest() {
        this.frontendMock = new FrontendStub();
    }

    @Before
    public void setup() {
        this.chooser = new ChooseServer(new ConfigHandler(path), 
                this.frontendMock);
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
    public void testFunctionality() throws ProtocolException {
        chooser.setParameter("tmc-server", "http://tmc.ebin.fi");
        chooser.execute();
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
