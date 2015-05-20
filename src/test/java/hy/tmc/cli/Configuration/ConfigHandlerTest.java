package hy.tmc.cli.Configuration;

import java.io.File;
import java.io.IOException;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.Test;

public class ConfigHandlerTest {

    ConfigHandler handler;
    String address = "http://boss.fi";

    @Before
    public void setup() {
        handler = new ConfigHandler("test.properties");
    }

    @Test
    public void configPathIsSetCorrectly() {
        assertEquals("test.properties", handler.getConfigFilePath());
        new File("test.properties").delete();
    }
    
    @After
    public void tearstuff() {
        try {
            handler.writeServerAddress("");
        }
        catch (IOException ex) {
            fail("something went wrong");
        }
    }

    private void writeServerAddress(String address) {
        try {
            handler.writeServerAddress(address);
        }
        catch (IOException ex) {
            fail("Something went wrong");
        }
    }

    @Test
    public void readCoursesAddressLooksGood() {
        writeServerAddress(address);
        assertEquals(address + handler.coursesExtension, handler.readCoursesAddress());
    }

    @Test
    public void readCoursesAddressGivesNull() {
        assertEquals(null, handler.readCoursesAddress());
    }

    @Test
    public void readAuthAddressLooksGood() {
        writeServerAddress(address);
        assertEquals(address + handler.authExtension, handler.readAuthAddress());
    }

    @Test
    public void readAuthAddressGivesNull() {
        assertEquals(null, handler.readAuthAddress());
    }

    @Test
    public void canUpdateAddressAfterItExists() {
        writeServerAddress(address);
        assertEquals(handler.readServerAddress(), address);
        writeServerAddress("http://einiinboss.fi");
        assertEquals(handler.readServerAddress(), "http://einiinboss.fi");
    }

    @Test
    public void addressIsCleanOnInit() {
        assertTrue(handler.readServerAddress().isEmpty());
    }

    @Test
    public void canWriteAddressToConfig() {
        try {
            handler.writeServerAddress(address);
        }
        catch (IOException ex) {
            fail("Something went wrong");
        }
    }

    @Test
    public void canReadAddressFromConfig() {
        try {
            handler.writeServerAddress(address);
        }
        catch (IOException ex) {
            fail("Failed writing to file");
        }
        String readAddress = handler.readServerAddress();
        assertEquals(readAddress, address);
    }
}
