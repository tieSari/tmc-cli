package hy.tmc.cli.frontend_communication.commands;

import hy.tmc.cli.frontend.communication.commands.ListExercises;
import hy.tmc.cli.frontend.communication.commands.RunTests;
import hy.tmc.cli.frontend.communication.server.ProtocolException;
import hy.tmc.cli.logic.Logic;
import hy.tmc.cli.testhelpers.FrontendStub;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

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
    
    @Test(timeout=15000)
    public void testFailedExercise(){
        RunTests run = new RunTests(front, null);
        String filepath = "testResources" + File.separator + "failingExercise" + File.separator + "viikko1" + File.separator + "Viikko1_001.Nimi";
        File file = new File(filepath);
        run.setParameter("filepath", file.getAbsolutePath());
        try {
            run.execute();
        }
        catch (ProtocolException ex) {
            fail("Test executing failed");
        }
        
        assertTrue(front.getMostRecentLine().contains("Some tests failed:"));
        
        assertTrue(front.getMostRecentLine().contains("0 tests passed:"));
        assertTrue(front.getMostRecentLine().contains("1 tests failed:"));
        
        assertTrue(front.getMostRecentLine().contains("NimiTest"));
        assertTrue(front.getMostRecentLine().contains("Et tulostanut"));
    }
    
    @Test(timeout=15000)
    public void testSuccessfulExercise(){
        RunTests run = new RunTests(front, null);
        String filepath = "testResources" + File.separator + "successExercise" + File.separator + "viikko1" + File.separator + "Viikko1_001.Nimi";
        File file = new File(filepath);
        run.setParameter("filepath", file.getAbsolutePath());
        try {
            run.execute();
        }
        catch (ProtocolException ex) {
            fail("Test executing failed");
        }
        
        assertFalse(front.getMostRecentLine().contains("tests failed:"));
        
        assertEquals("All tests passed. You can now submit", front.getMostRecentLine());
    }

}
