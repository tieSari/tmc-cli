/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hy.tmc.cli.frontend_communication.Commands;

import helpers.FrontendMock;
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

    private FrontendMock frontendMock;
    private Logic logic;

    public HelpTest() {
        this.logic = new Logic();
        this.frontendMock = new FrontendMock();
    }

    /**
     * Create new Echo
     */
    @Test
    public void createNewHelp() {
        Help help = new Help(this.frontendMock, new Logic());
        assertNotNull(help);
    }

    @Test
    public void testFunctionality() throws ProtocolException {
        FrontendListener l = mock(FrontendListener.class);
        Help h = new Help(l, new Logic());
        h.functionality();
        verify(l, times(1)).printLine("Commands: login, help, ping");
    }
}
