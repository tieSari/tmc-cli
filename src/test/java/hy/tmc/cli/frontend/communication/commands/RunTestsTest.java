package hy.tmc.cli.frontend.communication.commands;

import hy.tmc.cli.listeners.TestsListener;
import fi.helsinki.cs.tmc.langs.domain.NoLanguagePluginFoundException;
import hy.tmc.cli.configuration.ClientData;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import hy.tmc.cli.frontend.communication.server.ProtocolException;
import hy.tmc.cli.synchronization.TmcServiceScheduler;

import org.junit.Before;
import org.junit.Test;

import java.io.File;
import org.junit.After;

public class RunTestsTest {

    private TestsListener runTests;

    /**
     * Create FrontendStub and RunTests command.
     */
    @Before
    public void setup() {
        TmcServiceScheduler.disablePolling();
        ClientData.setUserData("test", "1234");
        runTests = new TestsListener();
    }

    /**
     * Check that data checking success.
     */
    @Test
    public void testCheckDataSuccess() throws ProtocolException {
        TestsListener rt = new TestsListener();
        rt.setParameter("path", "/home/tmccli/uolevipuistossa");
        rt.checkData();
    }

    /**
     * Check that if user didn't give correct data, data checking fails.
     */
    @Test(expected = ProtocolException.class)
    public void testCheckDataFail() throws ProtocolException {
        TestsListener rt = new TestsListener();
        rt.checkData();
    }

    /**
     * Test that failing exercise output is correct.
     */
    @Test(timeout = 15000)
    public void testFailedExercise() throws NoLanguagePluginFoundException, ProtocolException {
        TestsListener run = new TestsListener();
        String folders = "testResources" + File.separator + "failingExercise" + File.separator;
        String filepath = folders + "viikko1" + File.separator + "Viikko1_001.Nimi";
        File file = new File(filepath);
        run.setParameter("path", file.getAbsolutePath());
        String result = run.parseData(run.call()).get();

        assertTrue(result.contains("Some tests failed:"));
        assertTrue(result.contains("No tests passed"));
        assertTrue(result.contains("1 tests failed:"));
        assertTrue(result.contains("NimiTest"));
        assertTrue(result.contains("Et tulostanut"));
    }

    /**
     * Check that successful exercise output is correct.
     */
    @Test(timeout = 15000)
    public void testSuccessfulExercise() throws ProtocolException, NoLanguagePluginFoundException {
        TestsListener run = new TestsListener();
        String folders = "testResources" + File.separator + "successExercise" + File.separator;
        String filepath = folders + "viikko1" + File.separator + "Viikko1_001.Nimi";
        File file = new File(filepath);
        run.setParameter("path", file.getAbsolutePath());
        String result = run.parseData(run.call()).get();
        assertFalse(result.contains("tests failed:"));
        assertTrue(result.contains("All tests passed"));
    }
    
    @After
    public void clear() {
        TmcServiceScheduler.enablePolling();
        ClientData.clearUserData();
    }
}