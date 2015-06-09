package hy.tmc.cli.frontend.communication.commands;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import hy.tmc.cli.frontend.communication.server.ProtocolException;

import org.junit.Before;
import org.junit.Test;

public class HelpTest {

    private Help help;

    @Before
    public void setup() {
        this.help = new Help();
    }

    @Test
    public void createNewHelp() {
        assertNotNull(help);
    }

    @Test
    public void testFunctionality() throws ProtocolException {
        String output = help.call();
        assertTrue(output.contains("help"));
        assertTrue(output.contains("auth"));
    }
}
