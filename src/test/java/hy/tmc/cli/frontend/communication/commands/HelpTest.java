package hy.tmc.cli.frontend.communication.commands;

import hy.tmc.cli.frontend.communication.commands.Help;
import hy.tmc.cli.frontend.communication.server.ProtocolException;
import hy.tmc.cli.testhelpers.FrontendStub;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

public class HelpTest {

    private Help help;
    private FrontendStub frontendMock;

    public HelpTest() {
        this.frontendMock = new FrontendStub();
    }

    @Before
    public void setup() {
        this.help = new Help(this.frontendMock);
    }

    @Test
    public void createNewHelp() {
        assertNotNull(help);
    }

    @Test
    public void testFunctionality() {
        try {
            help.execute();
        }
        catch (ProtocolException ex) {
            fail("Something went wrong");
        }
        String output = this.frontendMock.getMostRecentLine();
        assertTrue(output.contains("help"));
        assertTrue(output.contains("auth"));
    }
}
