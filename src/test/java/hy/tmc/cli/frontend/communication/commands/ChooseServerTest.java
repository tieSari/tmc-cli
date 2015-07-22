package hy.tmc.cli.frontend.communication.commands;

import hy.tmc.cli.configuration.ConfigHandler;
import hy.tmc.cli.frontend.communication.server.ProtocolException;

import org.apache.commons.io.FileUtils;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;


public class ChooseServerTest {

    private SetServer chooser;
    private final String path = "testResources/test.properties";

    @Before
    public void setup() {
        this.chooser = new SetServer(new ConfigHandler(path));
    }
    
    @After
    public void teardown() {
        new File(path).delete();
    }

    @Test
    public void createNewHelp() {
        assertNotNull(chooser);
    }

    @Test
    public void testFunctionality() throws ProtocolException, Exception {
        chooser.setParameter("tmc-server", "http://tmc.ebin.fi");
        chooser.call();
        try {
            String propFile = FileUtils.readFileToString(new File(path));
            assertTrue(propFile.contains("tmc.ebin.fi"));
        } catch (IOException ex) {
            fail("unable to read propertiesfile");
        }
    }
    
    @Test (expected = ProtocolException.class)
    public void throwsExceptionWithoutData() throws ProtocolException {
        chooser.checkData();
    }
    
    @Test
    public void correctUrlisAccepted() {
        chooser.setParameter("tmc-server", "http://tmc.mooc.fi");
        try {
            chooser.checkData();
        } catch (ProtocolException ex) {
            fail("checkData threw exception");
        }
    }
    
    @Test (expected = ProtocolException.class)
    public void incorrectUrlThrowsException() throws ProtocolException {
        chooser.setParameter("tmc-server", "lak3jf02ja3fji23j");
        chooser.checkData();
    }
}
