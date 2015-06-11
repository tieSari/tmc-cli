package hy.tmc.cli.frontend.communication.commands;

import com.google.common.base.Optional;
import hy.tmc.cli.backend.Mailbox;
import hy.tmc.cli.backend.communication.CourseSubmitter;
import hy.tmc.cli.backend.communication.SubmissionInterpreter;
import hy.tmc.cli.configuration.ClientData;
import hy.tmc.cli.domain.Course;
import hy.tmc.cli.frontend.communication.server.ExpiredException;
import hy.tmc.cli.frontend.communication.server.ProtocolException;
import hy.tmc.cli.synchronization.TmcServiceScheduler;
import hy.tmc.cli.testhelpers.FrontendStub;
import java.io.IOException;
import java.text.ParseException;
import org.junit.After;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import static org.mockito.Mockito.when;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(ClientData.class)
public class PasteTest {

    private FrontendStub front;
    private Paste paste;
    CourseSubmitter submitterMock;
    SubmissionInterpreter interpreter;
    String pasteUrl = "http://legit.paste.url.fi";

    /**
     * Mocks CourseSubmitter and injects it into Paste command.
     */
    @Before
    public void setup() throws IOException, InterruptedException, IOException, ParseException, ExpiredException {
        Mailbox.create();
        mock();
        ClientData.setUserData("Bossman", "Samu");
        TmcServiceScheduler.disablePolling();
        submitterMock = Mockito.mock(CourseSubmitter.class);
        when(submitterMock.submitPaste(Mockito.anyString())).thenReturn(pasteUrl);
        front = new FrontendStub();
        paste = new Paste(front, submitterMock);

    }

    private void mock() throws ParseException, ExpiredException, IOException {
        ClientData.setUserData("Massbon", "Samu");
        PowerMockito.mockStatic(ClientData.class);
        PowerMockito.
                when(ClientData.getCurrentCourse(Mockito.anyString()))
                .thenReturn(Optional.<Course>of(new Course()));
        PowerMockito
                .when(ClientData.getFormattedUserData())
                .thenReturn("Bossman:Samu");
        PowerMockito
                .when(ClientData.userDataExists())
                .thenReturn(true);
    }

    @After
    public void clean() {
        ClientData.clearUserData();
    }

    @Test
    public void submitReturnsBadOutputWhenCodeIsBad() throws ProtocolException, InterruptedException {
        front.start();
        paste.setParameter("path", "/hieno/path");
        System.out.println("USER in pastetest " + ClientData.getFormattedUserData());
        paste.execute();
        String result = front.getMostRecentLine();
        assertTrue(result.contains(pasteUrl));
    }

    /**
     * Check that data checking success.
     */
    @Test
    public void testCheckDataSuccess() {
        Paste pasteCommand = new Paste(front);
        pasteCommand.setParameter("path", "/home/tmccli/uolevipuistossa");
        try {
            pasteCommand.checkData();
        } catch (ProtocolException p) {
            fail("testCheckDataSuccess failed");
        }
    }

    /**
     * Check that if user didn't give correct data, data checking fails.
     */
    @Test(expected = ProtocolException.class)
    public void testCheckDataFail() throws ProtocolException {
        Paste pasteCommand = new Paste(front);
        pasteCommand.checkData();
    }

    @Test(expected = ProtocolException.class)
    public void checkDataFailIfNoAuth() throws ProtocolException {
        Paste pasteCommand = new Paste(front);
        ClientData.clearUserData();
        pasteCommand.checkData();
    }
}
