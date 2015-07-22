
package hy.tmc.cli.frontend.communication.commands;

import hy.tmc.cli.listeners.ListCoursesListener;
import hy.tmc.cli.mail.Mailbox;
import hy.tmc.cli.backend.communication.HttpResult;
import hy.tmc.cli.backend.communication.UrlCommunicator;
import hy.tmc.cli.configuration.ClientData;
import hy.tmc.cli.frontend.communication.server.ProtocolException;
import hy.tmc.cli.testhelpers.ExampleJson;

import java.io.IOException;

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

    private ListCoursesListener list;

    /**
     * Set up FrontendStub, ListCourses command, power mockito and fake http
     * result.
     */
    @Before
    public void setUp() throws IOException, ProtocolException {
        list = new ListCoursesListener();

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
        ListCoursesListener ls = new ListCoursesListener();
        ClientData.setUserData("asdf", "bsdf");
        ls.checkData();
    }

    @Test(expected = ProtocolException.class)
    public void testNoAuthThrowsException() throws ProtocolException, Exception {
        ClientData.setUserData("", "");
        list.checkData();
        list.call();
    }

    @Test
    public void testWithAuthPrintsCourses() throws Exception {
        Mailbox.create();
        String testResult = list.parseData(list.call()).get();
        assertTrue(testResult.contains("WEPAMOOC-STAGE"));
    }

    @Test
    public void testWithAuthPrintsSeveralCourses() throws Exception {
        String testResult = list.parseData(list.call()).get();
        assertTrue(testResult.contains("WEPATEST"));
    }

    @Test
    public void checkDataTest() throws ProtocolException {
        list.checkData();
    }
}
