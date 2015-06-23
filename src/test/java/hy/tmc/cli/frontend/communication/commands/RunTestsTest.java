package hy.tmc.cli.frontend.communication.commands;

<<<<<<< HEAD
import com.google.common.base.Optional;
import hy.tmc.cli.configuration.ClientData;
import hy.tmc.cli.domain.Course;
import hy.tmc.cli.domain.Exercise;
=======
import fi.helsinki.cs.tmc.langs.NoLanguagePluginFoundException;
>>>>>>> 7061d626a3951db33faf53d915810654bf6c1720
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import hy.tmc.cli.frontend.communication.server.ProtocolException;
<<<<<<< HEAD
import hy.tmc.cli.synchronization.TmcServiceScheduler;
import hy.tmc.cli.testhelpers.FrontendStub;
=======
>>>>>>> 7061d626a3951db33faf53d915810654bf6c1720

import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(ClientData.class)
public class RunTestsTest {

    private RunTests runTests;

    /**
     * Create FrontendStub and RunTests command.
     */
    @Before
    public void setup() {
<<<<<<< HEAD
        TmcServiceScheduler.disablePolling();
        ClientData.setUserData("test", "1234");
        front = new FrontendStub();
        runTests = new RunTests(front);
        mock();
    }

    private void mock() {
        Course c = new Course();
        c.setName("2014-mooc-no-deadline");
        c.setId(0);
        c.setDetailsUrl("www.asd.com");
        c.setExercises(new ArrayList<Exercise>());
        c.setReviewsUrl("www.asd.com");
        c.setSpywareUrls(new ArrayList<String>());

        PowerMockito.mockStatic(ClientData.class);
        PowerMockito.
                when(ClientData.getCurrentCourse(Mockito.anyString()))
                .thenReturn(Optional.<Course>of(c));
        PowerMockito
                .when(ClientData.getFormattedUserData())
                .thenReturn("test:1234");
        PowerMockito
                .when(ClientData.userDataExists())
                .thenReturn(true);
=======
        runTests = new RunTests();
>>>>>>> 7061d626a3951db33faf53d915810654bf6c1720
    }

    /**
     * Check that data checking success.
     */
    @Test
<<<<<<< HEAD
    public void testCheckDataSuccess() {
        RunTests rt = new RunTests(front);
        rt.setParameter("path", "/home/tmccli/uolevipuistossa");
        try {
            rt.checkData();
        }
        catch (ProtocolException p) {
            fail("testCheckDataSuccess failed" + p.getMessage());
        }
=======
    public void testCheckDataSuccess() throws ProtocolException {
        RunTests rt = new RunTests();
        rt.setParameter("path", "/home/tmccli/uolevipuistossa");
        rt.checkData();
>>>>>>> 7061d626a3951db33faf53d915810654bf6c1720
    }

    /**
     * Check that if user didn't give correct data, data checking fails.
     *
     * @throws hy.tmc.cli.frontend.communication.server.ProtocolException
     */
    @Test(expected = ProtocolException.class)
    public void testCheckDataFail() throws ProtocolException {
<<<<<<< HEAD
        RunTests rt = new RunTests(front);
        rt.checkData();

=======
        RunTests rt = new RunTests();
        rt.checkData();
>>>>>>> 7061d626a3951db33faf53d915810654bf6c1720
    }

    /**
     * Test that failing exercise output is correct.
     */
<<<<<<< HEAD
    //@Test(timeout = 15000)
    public void testFailedExercise() {
        RunTests run = new RunTests(front);
=======
    @Test(timeout = 15000)
    public void testFailedExercise() throws NoLanguagePluginFoundException, ProtocolException {
        RunTests run = new RunTests();
>>>>>>> 7061d626a3951db33faf53d915810654bf6c1720
        String folders = "testResources" + File.separator + "failingExercise" + File.separator;
        String filepath = folders + "viikko1" + File.separator + "Viikko1_001.Nimi";
        File file = new File(filepath);
        run.setParameter("path", file.getAbsolutePath());
<<<<<<< HEAD
        try {
            run.execute();
        }
        catch (ProtocolException ex) {
            fail("Test executing failed cause: " + ex.getMessage());
        }

        assertTrue(front.getMostRecentLine().contains("Some tests failed:"));

        assertTrue(front.getMostRecentLine().contains("No tests passed"));
        assertTrue(front.getMostRecentLine().contains("1 tests failed:"));

        assertTrue(front.getMostRecentLine().contains("NimiTest"));
        assertTrue(front.getMostRecentLine().contains("Et tulostanut"));
=======
        String result = null;
        result = run.parseData(run.call()).get();

        assertTrue(result.contains("Some tests failed:"));
        assertTrue(result.contains("No tests passed"));
        assertTrue(result.contains("1 tests failed:"));
        assertTrue(result.contains("NimiTest"));
        assertTrue(result.contains("Et tulostanut"));
>>>>>>> 7061d626a3951db33faf53d915810654bf6c1720
    }

    /**
     * Check that successful exercise output is correct.
     */
<<<<<<< HEAD
    //@Test(timeout = 15000)
    public void testSuccessfulExercise() {
        RunTests run = new RunTests(front);
=======
    @Test(timeout = 15000)
    public void testSuccessfulExercise() throws ProtocolException, NoLanguagePluginFoundException {
        RunTests run = new RunTests();
>>>>>>> 7061d626a3951db33faf53d915810654bf6c1720
        String folders = "testResources" + File.separator + "successExercise" + File.separator;
        String filepath = folders + "viikko1" + File.separator + "Viikko1_001.Nimi";
        File file = new File(filepath);
        run.setParameter("path", file.getAbsolutePath());
<<<<<<< HEAD
        try {
            run.execute();
        }
        catch (ProtocolException ex) {
            fail("Test executing failed");
        }
        assertFalse(front.getMostRecentLine().contains("tests failed:"));

        assertTrue(front.getMostRecentLine().contains("All tests passed"));
=======
        String result = null;
        result = run.parseData(run.call()).get();
        assertFalse(result.contains("tests failed:"));
        assertTrue(result.contains("All tests passed"));
>>>>>>> 7061d626a3951db33faf53d915810654bf6c1720
    }
}
