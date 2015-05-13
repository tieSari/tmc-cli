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
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author pihla
 */
public class EchoTest {
    
    public EchoTest() {
    }

    /**
     * Create new Echo
     */
    @Test
    public void createNewEcho() {
       Echo echo = new Echo(new Server(1234, null), new Logic());
       assertNotNull(echo);
    }
    
    @Test (expected=ProtocolException.class)
    public void testCheckDataFails() throws ProtocolException{
        Echo echo = new Echo(new Server(1234, null), new Logic());
        echo.checkData();
    }
    
    @Test 
    public void testCheckDataSuccess() throws ProtocolException{
        Echo echo = new Echo(new Server(1234, null), new Logic());
        echo.setParameter("", "juuh");
        try {
            echo.checkData();
        } catch(ProtocolException p){
            fail("testCheckDataSuccess failed");
        }
    }
    
}
