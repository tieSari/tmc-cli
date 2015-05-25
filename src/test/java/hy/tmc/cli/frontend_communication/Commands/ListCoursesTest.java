package hy.tmc.cli.frontend_communication.Commands;

import hy.tmc.cli.testhelpers.FrontendStub;
import hy.tmc.cli.configuration.ClientData;
import hy.tmc.cli.backendCommunication.HTTPResult;
import hy.tmc.cli.backendCommunication.URLCommunicator;
import hy.tmc.cli.frontend_communication.Server.ProtocolException;
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
@PrepareForTest(URLCommunicator.class)
public class ListCoursesTest {

    private FrontendStub front;
    private Command list;

    @Before
    public void setUp() {
        front = new FrontendStub();
        list = new ListCourses(front, new Logic());
        
        
        PowerMockito.mockStatic(URLCommunicator.class);

        HTTPResult fakeResult = new HTTPResult(ExampleJSON.coursesExample, 200, true);

        ClientData.setUserData("mockattu", "ei tarvi");
        PowerMockito
                .when(URLCommunicator.makeGetRequest(
                            Mockito.eq(URLCommunicator.createClient()),
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
