package hy.tmc.cli.frontend_communication.Commands;

import hy.tmc.cli.logic.Logic;
import hy.tmc.cli.testhelpers.FrontendStub;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

public class HelpTest {

    private Help help;
    private FrontendStub frontendMock;
    private Logic logic;

    public HelpTest() {
        this.logic = new Logic();
        this.frontendMock = new FrontendStub();
    }

    @Before
    public void setup() {
        this.help = new Help(this.frontendMock, this.logic);
    }

    @Test
    public void createNewHelp() {
        assertNotNull(help);
    }

    @Test
    public void testFunctionality() {
        help.functionality();
        String output = this.frontendMock.getMostRecentLine();
        assertTrue(output.contains("help"));
        assertTrue(output.contains("auth"));
    }
}
