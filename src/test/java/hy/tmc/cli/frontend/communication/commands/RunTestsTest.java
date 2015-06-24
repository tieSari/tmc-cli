package hy.tmc.cli.frontend.communication.commands;

import com.google.common.base.Optional;
import hy.tmc.cli.configuration.ClientData;
import hy.tmc.cli.domain.Course;
import hy.tmc.cli.domain.Exercise;
import fi.helsinki.cs.tmc.langs.NoLanguagePluginFoundException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import hy.tmc.cli.frontend.communication.server.ProtocolException;
import hy.tmc.cli.synchronization.TmcServiceScheduler;

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
        TmcServiceScheduler.disablePolling();
        ClientData.setUserData("test", "1234");
        runTests = new RunTests();
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
    }

    /**
     * Check that data checking success.
     */
    @Test
    public void testCheckDataSuccess() throws ProtocolException {
        RunTests rt = new RunTests();
        rt.setParameter("path", "/home/tmccli/uolevipuistossa");
        rt.checkData();
    }

    /**
     * Check that if user didn't give correct data, data checking fails.
     *
     * @throws hy.tmc.cli.frontend.communication.server.ProtocolException
     */
    @Test(expected = ProtocolException.class)
    public void testCheckDataFail() throws ProtocolException {
        RunTests rt = new RunTests();
        rt.checkData();
    }

    /**
     * Test that failing exercise output is correct.
     */
    @Test(timeout = 15000)
    public void testFailedExercise() throws NoLanguagePluginFoundException, ProtocolException {
        RunTests run = new RunTests();
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
        RunTests run = new RunTests();
        String folders = "testResources" + File.separator + "successExercise" + File.separator;
        String filepath = folders + "viikko1" + File.separator + "Viikko1_001.Nimi";
        File file = new File(filepath);
        run.setParameter("path", file.getAbsolutePath());

        String result = null;
        result = run.parseData(run.call()).get();
        assertFalse(result.contains("tests failed:"));
        assertTrue(result.contains("All tests passed"));
    }
}
