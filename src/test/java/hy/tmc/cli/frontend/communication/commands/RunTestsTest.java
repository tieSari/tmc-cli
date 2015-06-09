package hy.tmc.cli.frontend.communication.commands;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import hy.tmc.cli.frontend.communication.server.ProtocolException;

import org.junit.Before;
import org.junit.Test;

import java.io.File;

public class RunTestsTest {

    private RunTests runTests;

    /**
     * Create FrontendStub and RunTests command.
     */
    @Before
    public void setup() {
        runTests = new RunTests();
    }

    /**
     * Check that data checking success.
     */
    @Test
    public void testCheckDataSuccess() {
        RunTests rt = new RunTests();
        rt.setParameter("filepath", "/home/tmccli/uolevipuistossa");
        try {
            rt.checkData();
        }
        catch (ProtocolException p) {
            fail("testCheckDataSuccess failed");
        }
    }

    /**
     * Check that if user didn't give correct data, data checking fails.
     */
    @Test
    public void testCheckDataFail() {
        RunTests rt = new RunTests();
        try {
            rt.checkData();
            fail("testCheckDataFail should have failed");
        }
        catch (ProtocolException p) {
            return;
        }
    }

    /**
     * Test that failing exercise output is correct.
     */
    @Test(timeout = 15000)
    public void testFailedExercise() {
        RunTests run = new RunTests();
        String folders = "testResources" + File.separator + "failingExercise" + File.separator;
        String filepath = folders + "viikko1" + File.separator + "Viikko1_001.Nimi";
        File file = new File(filepath);
        run.setParameter("filepath", file.getAbsolutePath());
        String result = null;
        try {
            result = run.call();
        }
        catch (ProtocolException ex) {
            fail("Test executing failed");
        }

        assertTrue(result.contains("Some tests failed:"));
        assertTrue(result.contains("No tests passed"));
        assertTrue(result.contains("1 tests failed:"));
        assertTrue(result.contains("NimiTest"));
        assertTrue(result.contains("Et tulostanut"));
    }

    /**
     * Check that successfull exercise output is correct.
     */
    @Test(timeout = 15000)
    public void testSuccessfulExercise() {
        RunTests run = new RunTests();
        String folders = "testResources" + File.separator + "successExercise" + File.separator;
        String filepath = folders + "viikko1" + File.separator + "Viikko1_001.Nimi";
        File file = new File(filepath);
        run.setParameter("filepath", file.getAbsolutePath());
        String result = null;
        try {
            result = run.call();
        }
        catch (ProtocolException ex) {
            fail("Test executing failed");
        }
        assertFalse(result.contains("tests failed:"));
        assertTrue(result.contains("All tests passed"));
    }
}
