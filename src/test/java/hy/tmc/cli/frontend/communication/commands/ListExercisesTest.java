package hy.tmc.cli.frontend.communication.commands;

import hy.tmc.cli.backend.communication.HttpResult;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

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

@RunWith(PowerMockRunner.class)
@PrepareForTest(UrlCommunicator.class)
public class ListExercisesTest {

    private FrontendStub front;
    private Command list;

    /**
     * Set up frontend stub, list exercises command, fake http result, client data & power mockito.
     */
    @Before
    public void setup() {
        front = new FrontendStub();
        list = new ListExercises();

        PowerMockito.mockStatic(UrlCommunicator.class);

        HttpResult fakeResult = new HttpResult(ExampleJson.courseExample, 200, true);

        ClientData.setUserData("chang", "paras");
        PowerMockito
                .when(UrlCommunicator.makeGetRequest(
                                Mockito.anyString(), Mockito.anyString()))
                .thenReturn(fakeResult);
    }

    @Test
    public void testCheckDataSuccess() throws ProtocolException {
        ListExercises ls = new ListExercises();
        ls.setParameter("courseUrl", "legit");
        try {
            ls.checkData();
        }
        catch (ProtocolException p) {
            fail("testCheckDataSuccess failed");
        }
    }

    @Test
    public void getsExerciseName() {
        list.setParameter("courseUrl", "any");
        try {
            String result = list.call();
            front.getMostRecentLine();
            assertTrue(result.contains("viikko1-Viikko1_000.Hiekkalaatikko"));
            assertTrue(result.contains("viikko3-Viikko3_046.LukujenKeskiarvo"));
        } catch (ProtocolException ex) {
            fail("unexpected exception");
        }

    }

    @Test(expected = ProtocolException.class)
    public void throwsErrorIfNoUser() throws ProtocolException {
        ClientData.clearUserData();
        list.setParameter("courseUrl", "any");
        list.call();
    }

    @Test(expected = ProtocolException.class)
    public void throwsErrorIfNoCourseSpecified() throws ProtocolException {
        list.call();
    }

    @Test
    public void doesntContainWeirdName() {
        list.setParameter("courseUrl", "any");
        try {
            assertFalse(list.call().contains("Ilari"));
        }
        catch (ProtocolException ex) {
            fail("unexpected exception");
        }
    }
}
