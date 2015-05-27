package hy.tmc.cli.frontend_communication.commands;

import hy.tmc.cli.frontend.communication.commands.ListExercises;
import hy.tmc.cli.frontend.communication.commands.RunTests;
import hy.tmc.cli.frontend.communication.server.ProtocolException;
import hy.tmc.cli.logic.Logic;
import hy.tmc.cli.testhelpers.FrontendStub;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class RunTestsTest {

    private FrontendStub front;
    private RunTests runTests;

    @Before
    public void setup() {
        front = new FrontendStub();
        runTests = new RunTests(front, null);



        /* PowerMockito.mockStatic(UrlCommunicator.class);

        HttpResult fakeResult = new HttpResult(ExampleJSON.courseExample, 200, true);

        ClientData.setUserData("chang", "paras");
        PowerMockito
                .when(UrlCommunicator.makeGetRequest(
                        Mockito.anyString(), Mockito.anyString()))
                .thenReturn(fakeResult); */
    }

    @Test
    public void testCheckDataSuccess() {
        RunTests rt = new RunTests(front,null);
        rt.setParameter("filepath", "/home/tmccli/uolevipuistossa");
        try {
            rt.checkData();
        } catch (ProtocolException p) {
            fail("testCheckDataSuccess failed");
        }
    }

    @Test
    public void testCheckDataFail() {
        RunTests rt = new RunTests(front,null);
        try {
            rt.checkData();
            fail("testCheckDataFail should have failed");
        } catch (ProtocolException p) {
            return;
        }
    }


}
