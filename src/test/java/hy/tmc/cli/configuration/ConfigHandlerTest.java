package hy.tmc.cli.configuration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.Date;

public class ConfigHandlerTest {

    ConfigHandler handler;
    String address = "http://boss.fi";

    @Before
    public void setup() {
        handler = new ConfigHandler(Paths.get("test.properties"));
    }

    @Test
    public void configPathIsSetCorrectly() {
        assertEquals("test.properties", handler.getConfigFilePath());
        new File("test.properties").delete();
    }
    
    @Test
    public void testConfigPathIsCorrectForLinux() {

    }
    
    @Test
    public void testConfigPathIsCorrectForMac() {
    }
    
    @Test
    public void testConfigPathIsCorrectForWindows() {

    }
    
    @Test
    public void testConfigPathIsCorrectForOther() {

    }
    
    /**
     * Clean all marks of test in config files.
     */
    @After
    public void tearstuff() {
        try {
            handler.writeServerAddress("");
            File file = new File("test.properties");
            if (file.exists()) {
                file.delete();
            }
        } catch (IOException ex) {
            fail("something went wrong");
        }
    }

    private void writeServerAddress(String address) {
        try {
            handler.writeServerAddress(address);
        } catch (IOException ex) {
            fail("Something went wrong");
        }
    }

    @Test
    public void canUpdateAddressAfterItExists() {
        writeServerAddress(address);
        assertEquals(handler.readServerAddress(), address);
        writeServerAddress("http://einiinboss.fi");
        assertEquals(handler.readServerAddress(), "http://einiinboss.fi");
    }

    @Test (expected = IllegalStateException.class)
    public void exceptionIsThrownIfNoAddressFound() {
        handler.readServerAddress();
    }

    @Test
    public void canWriteAddressToConfig() {
        try {
            handler.writeServerAddress(address);
        } catch (IOException ex) {
            fail("Something went wrong");
        }
    }

    @Test
    public void canReadAddressFromConfig() {
        try {
            handler.writeServerAddress(address);
        } catch (IOException ex) {
            fail("Failed writing to file");
        }
        String readAddress = handler.readServerAddress();
        assertEquals(readAddress, address);
    }
    
    @Test
    public void canWritePortAddress() {
        try {
            handler.writePort(1234);
        } catch (IOException ex) {
            fail("failed to write port");
        }
    }
    
    @Test
    public void dontCrashWhenLastUpdateDoesntExist() throws ParseException, IOException {
        Date lastUpdate = handler.readLastUpdate();
        assertNotNull(lastUpdate);
    }
    
    @Test
    public void correctPortGetsWritten() {
        try {
            handler.writePort(12355);
            assertEquals(12355, handler.readPort());
        } catch (IOException ex) {
            fail("Failed to read or write port");
        }
    }
}
