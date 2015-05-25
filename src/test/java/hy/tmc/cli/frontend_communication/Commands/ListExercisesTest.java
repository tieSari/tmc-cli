package hy.tmc.cli.frontend_communication.Commands;

import hy.tmc.cli.configuration.ClientData;
import hy.tmc.cli.backendcommunication.HttpResult;
import hy.tmc.cli.backendcommunication.UrlCommunicator;
import hy.tmc.cli.frontend_communication.Server.ProtocolException;
import hy.tmc.cli.logic.Logic;
import hy.tmc.cli.testhelpers.ExampleJSON;
import hy.tmc.cli.testhelpers.FrontendStub;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
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

    @Before
    public void setup() {
        front = new FrontendStub();
        list = new ListExercises(front, new Logic());

        PowerMockito.mockStatic(UrlCommunicator.class);

        HttpResult fakeResult = new HttpResult(ExampleJSON.courseExample, 200, true);

        ClientData.setUserData("chang", "paras");
        PowerMockito
                .when(UrlCommunicator.makeGetRequest(
                                Mockito.eq(UrlCommunicator.createClient()),
                                Mockito.anyString(), Mockito.anyString()))
                .thenReturn(fakeResult);
    }

    @Test
    public void testCheckDataSuccess() throws ProtocolException {
        ListExercises ls = new ListExercises(front, new Logic());
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
            list.execute();
            System.out.println(front.getMostRecentLine());
            assertTrue(front.getMostRecentLine().contains("RobottiOhjain"));
        }
        catch (ProtocolException ex) {
            fail("unexpected exception");
        }

    }

    @Test
    public void doesntContainWeirdName() {
        list.setParameter("courseUrl", "any");
        try {
            list.execute();
            assertFalse(front.getMostRecentLine().contains("Ilari"));
        }
        catch (ProtocolException ex) {
            fail("unexpected exception");
        }

    }

}
