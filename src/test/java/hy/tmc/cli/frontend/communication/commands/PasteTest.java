package hy.tmc.cli.frontend.communication.commands;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

import com.google.common.base.Optional;

import hy.tmc.cli.backend.Mailbox;
import hy.tmc.cli.backend.communication.CourseSubmitter;
import hy.tmc.cli.configuration.ClientData;
<<<<<<< HEAD
import hy.tmc.cli.domain.Course;
import hy.tmc.cli.frontend.communication.server.ExpiredException;
import hy.tmc.cli.frontend.communication.server.ProtocolException;
import hy.tmc.cli.synchronization.TmcServiceScheduler;
import hy.tmc.cli.testhelpers.FrontendStub;
import hy.tmc.cli.testhelpers.ProjectRootFinderStub;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
=======
import hy.tmc.cli.frontend.communication.server.ProtocolException;
>>>>>>> 7061d626a3951db33faf53d915810654bf6c1720
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.text.ParseException;

@RunWith(PowerMockRunner.class)
@PrepareForTest(ClientData.class)
public class PasteTest {

    private Paste paste;
    private CourseSubmitter submitterMock;
    private String pasteUrl = "http://legit.paste.url.fi";

    /**
     * Mocks CourseSubmitter and injects it into Paste command.
     */
    @Before
<<<<<<< HEAD
    public void setup() throws IOException, InterruptedException, IOException, ParseException, ExpiredException {
        Mailbox.create();
        mock();
        ClientData.setUserData("Bossman", "Samu");
        ClientData.setProjectRootFinder(new ProjectRootFinderStub());
        TmcServiceScheduler.disablePolling();
        submitterMock = Mockito.mock(CourseSubmitter.class);
        when(submitterMock.submitPaste(Mockito.anyString())).thenReturn(pasteUrl);
        front = new FrontendStub();
        paste = new Paste(front, submitterMock);

    }

    private void mock() throws ParseException, ExpiredException, IOException {
        ClientData.setUserData("Massbon", "Samu");
        PowerMockito.mockStatic(ClientData.class);
        PowerMockito
                .when(ClientData.getCurrentCourse(Mockito.anyString()))
                .thenReturn(Optional.<Course>of(new Course()));
        PowerMockito
                .when(ClientData.getFormattedUserData())
                .thenReturn("Bossman:Samu");
=======
    public void setup() throws Exception {
        submitterMock = Mockito.mock(CourseSubmitter.class);
        when(submitterMock.submitPaste(Mockito.anyString())).thenReturn(pasteUrl);

        paste = new Paste(submitterMock);
        ClientData.setUserData("Bossman", "Samu");
>>>>>>> 7061d626a3951db33faf53d915810654bf6c1720
    }

    @After
    public void clean() {
        ClientData.clearUserData();
    }

    @Test
<<<<<<< HEAD
    public void submitReturnsBadOutputWhenCodeIsBad() throws ProtocolException, InterruptedException {
        PowerMockito.when(ClientData.userDataExists()).thenReturn(true);
        front.start();
=======
    public void submitReturnsBadOutputWhenCodeIsBad() throws Exception {
>>>>>>> 7061d626a3951db33faf53d915810654bf6c1720
        paste.setParameter("path", "/hieno/path");
        String result = paste.parseData(paste.call()).get();
        assertTrue(result.contains(pasteUrl));
    }

    /**
     * Check that data checking success.
     */
    @Test
    public void testCheckDataSuccess() {
<<<<<<< HEAD
        PowerMockito.when(ClientData.userDataExists()).thenReturn(true);
        paste.setParameter("path", "/home/tmccli/uolevipuistossa");
=======
        Paste pasteCommand = new Paste();
        pasteCommand.setParameter("path", "/home/tmccli/uolevipuistossa");
>>>>>>> 7061d626a3951db33faf53d915810654bf6c1720
        try {
            paste.checkData();
        } catch (ProtocolException p) {
            fail("testCheckDataSuccess failed");
        }
    }

    /**
     * Check that if user didn't give correct data, data checking fails.
     */
    @Test(expected = ProtocolException.class)
<<<<<<< HEAD
    public void testCheckDataFail() throws Exception {
        paste.checkData();
    }

    @Test(expected = ProtocolException.class)
    public void checkDataFailIfNoAuth() throws Exception {
        ClientData.clearUserData();
        paste.checkData();
    }

    @Test(expected = ProtocolException.class)
    public void throwsErrorIfNoCredentialsPresent() throws Exception {
        paste.data.put("path", "asdsad");
        ClientData.clearUserData();
        paste.checkData();
=======
    public void testCheckDataFail() throws ProtocolException {
        Paste pasteCommand = new Paste();
        pasteCommand.checkData();
>>>>>>> 7061d626a3951db33faf53d915810654bf6c1720
    }

    @Test(expected = ProtocolException.class)
<<<<<<< HEAD
    public void throwsErrorIfCourseCantBeRetrieved() throws Exception {
        PowerMockito.when(ClientData.userDataExists()).thenReturn(true);
        PowerMockito
                .when(ClientData.getCurrentCourse(Mockito.anyString()))
                .thenReturn(Optional.<Course>absent());
        paste.data.put("path", "asdsad");
=======
    public void checkDataFailIfNoAuth() throws ProtocolException {
        Paste pasteCommand = new Paste();
>>>>>>> 7061d626a3951db33faf53d915810654bf6c1720
        ClientData.clearUserData();
        paste.checkData();
    }
}
