package hy.tmc.cli;

import fi.helsinki.cs.tmc.core.TmcCore;

import hy.tmc.cli.configuration.ConfigHandler;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.text.ParseException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TmcCliTest {

    TmcCli client;
    TmcCore core;
    ConfigHandler config;

    public TmcCliTest() {
    }

    @Before
    public void setUp() throws IOException {
        config = Mockito.mock(ConfigHandler.class);
        core = Mockito.mock(TmcCore.class);
        client = new TmcCli(core, config);
    }

    @After 
    public void tearDown() {
    }

    @Test
    public void testLogin() throws IllegalStateException, ParseException, IOException {
        client.login("pekka", "ankka");
        CliSettings settings = client.defaultSettings();
        assertEquals("pekka", settings.getUsername());
        assertEquals("ankka", settings.getPassword());
    }

    @Test
    public void testLogout() throws IllegalStateException, ParseException, IOException {
        client.login("ankka", "pekka");
        client.logout();
        CliSettings settings = client.defaultSettings();
        assertTrue(settings.getUsername().isEmpty());
        assertTrue(settings.getPassword().isEmpty());
    }

    @Test
    public void testDefaultSettings()
        throws IllegalStateException, ParseException, IOException {
        String address = "https://test.mooc.ankka.fi";
        when(config.readServerAddress()).thenReturn(address);
        CliSettings settings = client.defaultSettings();
        assertEquals(address, settings.getServerAddress());
        assertEquals("7", settings.apiVersion());
    }

    @Test
    public void testSetServer() throws IOException, ParseException {
        String address = "https://test.mooc.ankka.fi";
        when(config.readServerAddress()).thenReturn(address);
        client.setServer(address);
        verify(config).writeServerAddress(eq(address));
        assertEquals(address, client.defaultSettings().getServerAddress());
    }

}
