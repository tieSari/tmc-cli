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
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ConfigHandlerTest {

    ConfigHandler handler;
    String address = "http://boss.fi";
    EnvironmentWrapper env;

    @Before
    public void setup() {
        env = mock(EnvironmentWrapper.class);
        handler = new ConfigHandler(Paths.get("test.properties"));
    }

    @Test
    public void configPathIsSetCorrectly() {
        assertEquals("test.properties", handler.getConfigFilePath());
        new File("test.properties").delete();
    }
    
    @Test
    public void testXdgDefaultsCorrectly() {
        String xdgConf = "home/duck/.config";
        when(env.getOsName()).thenReturn("Mac OS X");
        when(env.getenv(eq("XDG_CONFIG_HOME"))).thenReturn("");
        when(env.getHomeDirectory()).thenReturn("home/duck");
        String path = new ConfigHandler(env).getConfigFilePath();
        String expected = xdgConf + File.separatorChar + "tmc" + File.separatorChar + "config.properties";
        assertEquals(expected, path);
    }

    @Test
    public void testConfigPathIsCorrectForLinux() {
        String xdgConf = "home/duck/.config";
        when(env.getOsName()).thenReturn("Linux");
        when(env.getenv(eq("XDG_CONFIG_HOME"))).thenReturn(xdgConf);
        String path = new ConfigHandler(env).getConfigFilePath();
        String expected = xdgConf + File.separatorChar + "tmc" + File.separatorChar + "config.properties";
        assertEquals(expected, path);
    }

    @Test
    public void testConfigPathIsCorrectForMac() {
        String xdgConf = "home/duck/.config";
        when(env.getOsName()).thenReturn("Mac OS X");
        when(env.getenv(eq("XDG_CONFIG_HOME"))).thenReturn(xdgConf);
        String path = new ConfigHandler(env).getConfigFilePath();
        String expected = xdgConf + File.separatorChar + "tmc" + File.separatorChar + "config.properties";
        assertEquals(expected, path);
    }
    
    @Test
    public void testConfigPathIsCorrectForFreeBsd() {
        String xdgConf = "home/duck/.config";
        when(env.getOsName()).thenReturn("FreeBsd");
        when(env.getenv(eq("XDG_CONFIG_HOME"))).thenReturn(xdgConf);
        String path = new ConfigHandler(env).getConfigFilePath();
        String expected = xdgConf + File.separatorChar + "tmc" + File.separatorChar + "config.properties";
        assertEquals(expected, path);
    }

    @Test
    public void testConfigPathIsCorrectForWindows() {
        String appdata = "C:\\asdf\\bsdfg";
        when(env.getOsName()).thenReturn("Windows 10");
        when(env.getenv(eq("APPDATA"))).thenReturn(appdata);
        String path = new ConfigHandler(env).getConfigFilePath();
        String expected = appdata + File.separatorChar + "tmc" + File.separatorChar + "config.properties";
        assertEquals(expected, path);
    }

    @Test
    public void testConfigPathIsCorrectForOther() {
        when(env.getOsName()).thenReturn("Fantasmas Minuscul-os");
        String path = new ConfigHandler(env).getConfigFilePath();
        assertEquals("config.properties", path);
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
    public void canUpdateAddressAfterItExists() {
        writeServerAddress(address);
        assertEquals(handler.readServerAddress(), address);
        writeServerAddress("http://einiinboss.fi");
        assertEquals(handler.readServerAddress(), "http://einiinboss.fi");
    }

    @Test(expected = IllegalStateException.class)
    public void exceptionIsThrownIfNoAddressFound() {
        handler.readServerAddress();
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

    @Test
    public void canWritePortAddress() {
        try {
            handler.writePort(1234);
        }
        catch (IOException ex) {
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
        }
        catch (IOException ex) {
            fail("Failed to read or write port");
        }
    }
}
