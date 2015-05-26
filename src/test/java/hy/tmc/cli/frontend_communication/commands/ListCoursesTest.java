package hy.tmc.cli.frontend_communication.commands;

import hy.tmc.cli.frontend.communication.commands.Command;
import hy.tmc.cli.frontend.communication.commands.ListCourses;
import hy.tmc.cli.testhelpers.FrontendStub;
import hy.tmc.cli.configuration.ClientData;
import hy.tmc.cli.backendcommunication.HttpResult;
import hy.tmc.cli.backendcommunication.UrlCommunicator;
import hy.tmc.cli.frontend.communication.server.ProtocolException;
import hy.tmc.cli.logic.Logic;
import hy.tmc.cli.testhelpers.ExampleJSON;
import java.util.logging.Level;
import java.util.logging.Logger;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;


@RunWith(PowerMockRunner.class)
@PrepareForTest(UrlCommunicator.class)
public class ListCoursesTest {

    private FrontendStub front;
    private Command list;

    @Before
    public void setUp() {
        front = new FrontendStub();
        list = new ListCourses(front, new Logic());
        
        
        PowerMockito.mockStatic(UrlCommunicator.class);

        HttpResult fakeResult = new HttpResult(ExampleJSON.allCoursesExample, 200, true);

        ClientData.setUserData("mockattu", "ei tarvi");
        PowerMockito
                .when(UrlCommunicator.makeGetRequest(
                            Mockito.anyString(), Mockito.anyString()))
                .thenReturn(fakeResult);
        
    }

    @Test
    public void testCheckDataSuccess() throws ProtocolException {
        ListCourses ls = new ListCourses(front, new Logic());
        ClientData.setUserData("asdf", "bsdf");
        try {
            ls.checkData();
        }
        catch (ProtocolException p) {
            fail("testCheckDataSuccess failed");
        }
    }
    
    @Test (expected=ProtocolException.class)
    public void testNoAuthThrowsException() throws ProtocolException {
        ClientData.setUserData("", "");
        list.execute();   
    }

    @Test
    public void testWithAuthPrintsCourses() {
        try {
            list.execute();
            assertTrue(front.getMostRecentLine().contains("WEPAMOOC-STAGE"));
        }
        catch (ProtocolException ex) {
            Logger.getLogger(ListCoursesTest.class.getName()).log(Level.SEVERE, null, ex);
            fail("unexpected exception");
        }
    }

    @Test
    public void testWithAuthPrintsSeveralCourses() {
        try {
            list.execute();
            assertTrue(front.getMostRecentLine().contains("WEPATEST"));
        }
        catch (ProtocolException ex) {
            Logger.getLogger(ListCoursesTest.class.getName()).log(Level.SEVERE, null, ex);
            fail("unexpected exception");
        }
    }


    @Test
    public void checkDataTest() {
        try {
            list.checkData();
        }
        catch (ProtocolException ex) {
            fail("listcourses should not throw exception from checkData");
        }
    }

}
