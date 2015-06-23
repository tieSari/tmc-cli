package hy.tmc.cli.frontend.communication.commands;

import hy.tmc.cli.backend.Mailbox;
import hy.tmc.cli.backend.communication.HttpResult;
import hy.tmc.cli.backend.communication.UrlCommunicator;
import hy.tmc.cli.configuration.ClientData;
import hy.tmc.cli.frontend.communication.server.ProtocolException;
import hy.tmc.cli.testhelpers.ExampleJson;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.mockito.Mockito;

import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

@RunWith(PowerMockRunner.class)
@PrepareForTest(UrlCommunicator.class)
public class ListCoursesTest {

    private ListCourses list;

    /**
     * Set up FrontendStub, ListCourses command, power mockito and fake http
     * result.
     */
    @Before
    public void setUp() {
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

    @Test(expected = ProtocolException.class)
    public void testNoAuthThrowsException() throws ProtocolException, Exception {
        ClientData.setUserData("", "");
        list.call();
    }

    @Test
<<<<<<< HEAD
    public void testWithAuthPrintsCourses() {
        Mailbox.create();
        try {
            list.execute();
            assertTrue(front.getMostRecentLine().contains("WEPAMOOC-STAGE"));
        } catch (ProtocolException ex) {
            System.err.println(ex.getMessage());
            fail("unexpected exception" + ex.getMessage());
        }
=======
    public void testWithAuthPrintsCourses() throws Exception {
        String testResult = list.parseData(list.call()).get();
        assertTrue(testResult.contains("WEPAMOOC-STAGE"));
>>>>>>> 7061d626a3951db33faf53d915810654bf6c1720
    }

    @Test
    public void testWithAuthPrintsSeveralCourses() throws Exception {
        try {
            String testResult = list.parseData(list.call()).get();
            assertTrue(testResult.contains("WEPATEST"));
        } catch (ProtocolException ex) {
<<<<<<< HEAD
            fail("unexpected exception: " + ex.getMessage());
=======
            fail("unexpected exception");
>>>>>>> 7061d626a3951db33faf53d915810654bf6c1720
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
