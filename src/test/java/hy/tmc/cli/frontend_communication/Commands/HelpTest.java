/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hy.tmc.cli.frontend_communication.Commands;

import hy.tmc.cli.frontend_communication.FrontendListener;
import hy.tmc.cli.frontend_communication.Server.ProtocolException;
import hy.tmc.cli.frontend_communication.Server.Server;
import hy.tmc.cli.logic.Logic;
import java.io.IOException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.Mock;
import org.mockito.*;
import static org.mockito.Mockito.*;
import org.powermock.api.mockito.PowerMockito;

/**
 *
 * @author pihla
 * 
 **/

public class HelpTest {

    private Server server;
    private Logic logic;
    @Mock
    private Help mockFoo;

    public HelpTest() {
        this.logic = new Logic();
    }

    @Before
    public void startServer() {
        this.server = new Server(8034, logic);
    }
    
    Help makeHelp(Server s, Logic l) {
        return new Help(s, l);
    }

    /**
     * Create new Echo
     */
    @Test
    public void createNewHelp() {
        Help help = new Help(this.server, new Logic());
        assertNotNull(help);
    }

    @Test
    public void testFunctionality() throws ProtocolException {
        FrontendListener l = mock(FrontendListener.class);
        Help h = new Help(l, new Logic());
        h.functionality();
        verify(l, times(1)).printLine("Commands: login, help, ping");
    }

    @After
    public void closeServer() {
        try {
            this.server.close();
        } catch (IOException ex) {
            fail("Closing server failed");
        }
    }
}
