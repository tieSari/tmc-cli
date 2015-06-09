package hy.tmc.cli.frontend.communication.commands;

import hy.tmc.cli.backend.communication.HttpResult;
import hy.tmc.cli.backend.communication.UrlCommunicator;
import hy.tmc.cli.configuration.ClientData;
import hy.tmc.cli.frontend.communication.server.ProtocolException;
import hy.tmc.cli.testhelpers.ExampleJson;
import hy.tmc.cli.testhelpers.FrontendStub;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.mockito.Mockito;

import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.logging.Level;
import java.util.logging.Logger;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

@RunWith(PowerMockRunner.class)
@PrepareForTest(UrlCommunicator.class)
public class ListCoursesTest {

    private FrontendStub front;
    private Command list;

    /**
     * Set up FrontendStub, ListCourses command, power mockito and fake http result.
     */
    @Before
    public void setUp() {
        front = new FrontendStub();
        list = new ListCourses();
        
        
        PowerMockito.mockStatic(UrlCommunicator.class);

        HttpResult fakeResult = new HttpResult(ExampleJson.allCoursesExample, 200, true);

        ClientData.setUserData("mockattu", "ei tarvi");
        PowerMockito
                .when(UrlCommunicator.makeGetRequest(
                            Mockito.anyString(), Mockito.anyString()))
                .thenReturn(fakeResult);
        
    }

    @Test
    public void testCheckDataSuccess() throws ProtocolException {
        ListCourses ls = new ListCourses();
        ClientData.setUserData("asdf", "bsdf");
        try {
            ls.checkData();
        } catch (ProtocolException p) {
            fail("testCheckDataSuccess failed");
        }
    }
    
    @Test (expected = ProtocolException.class)
    public void testNoAuthThrowsException() throws ProtocolException {
        ClientData.setUserData("", "");
        list.call();   
    }

    @Test
    public void testWithAuthPrintsCourses() {
        try {
            assertTrue(list.call().contains("WEPAMOOC-STAGE"));
        } catch (ProtocolException ex) {
            Logger.getLogger(ListCoursesTest.class.getName()).log(Level.SEVERE, null, ex);
            fail("unexpected exception");
        }
    }

    @Test
    public void testWithAuthPrintsSeveralCourses() {
        try {
            assertTrue(list.call().contains("WEPATEST"));
        } catch (ProtocolException ex) {
            Logger.getLogger(ListCoursesTest.class.getName()).log(Level.SEVERE, null, ex);
            fail("unexpected exception");
        }
    }


    @Test
    public void checkDataTest() {
        try {
            list.checkData();
        } catch (ProtocolException ex) {
            fail("listcourses should not throw exception from checkData");
        }
    }

}
