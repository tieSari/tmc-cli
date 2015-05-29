package hy.tmc.cli.frontend_communication.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import hy.tmc.cli.frontend.communication.commands.RunTests;
import hy.tmc.cli.frontend.communication.server.ProtocolException;
import hy.tmc.cli.testhelpers.FrontendStub;

import org.junit.Before;
import org.junit.Test;

import java.io.File;

public class RunTestsTest {

    private FrontendStub front;
    private RunTests runTests;

    /**
     * Create FrontendStub and RunTests command.
     */
    @Before
    public void setup() {
        front = new FrontendStub();
        runTests = new RunTests(front, null);
    }

    /**
     * Check that data checking success.
     */
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

    /**
     * Check that if user didn't give correct data, data checking fails.
     */
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
    
    /**
     * Test that failing exercise output is correct.
     */
    @Test(timeout = 15000)
    public void testFailedExercise() {
        RunTests run = new RunTests(front, null);
        String folders = "testResources" + File.separator + "failingExercise" + File.separator;
        String filepath = folders + "viikko1" + File.separator + "Viikko1_001.Nimi";
        File file = new File(filepath);
        run.setParameter("filepath", file.getAbsolutePath());
        try {
            run.execute();
        } catch (ProtocolException ex) {
            fail("Test executing failed");
        }
        
        assertTrue(front.getMostRecentLine().contains("Some tests failed:"));
        
        assertTrue(front.getMostRecentLine().contains("No tests passed"));
        assertTrue(front.getMostRecentLine().contains("1 tests failed:"));
        
        assertTrue(front.getMostRecentLine().contains("NimiTest"));
        assertTrue(front.getMostRecentLine().contains("Et tulostanut"));
    }
    
    /**
     * Check that successfull exercise output is correct.
     */
    @Test(timeout = 15000)
    public void testSuccessfulExercise() {
        RunTests run = new RunTests(front, null);
        String folders = "testResources" + File.separator + "successExercise" + File.separator;
        String filepath = folders + "viikko1" + File.separator + "Viikko1_001.Nimi";
        File file = new File(filepath);
        run.setParameter("filepath", file.getAbsolutePath());
        try {
            run.execute();
        } catch (ProtocolException ex) {
            fail("Test executing failed");
        }
        
        assertFalse(front.getMostRecentLine().contains("tests failed:"));
        
        assertEquals("All tests passed. You can now submit", front.getMostRecentLine());
    }
    
    /**
     * Check that nonexercise folder is recognized.
     */
    @Test(timeout = 15000)
    public void testNonExercise() {
        RunTests run = new RunTests(front, null);
        String folders = "testResources" + File.separator + "successExercise" + File.separator;
        String filepath = folders + "viikko1";
        File file = new File(filepath);
        run.setParameter("filepath", file.getAbsolutePath());
        try {
            run.execute();
        } catch (ProtocolException ex) {
            fail("Test executing failed");
        }
        
        assertFalse(front.getMostRecentLine().contains("tests failed:"));
        
        assertEquals("Not an exercise.", front.getMostRecentLine());
    }

}
