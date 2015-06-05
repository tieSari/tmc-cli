package hy.tmc.cli.frontend.communication.commands;

import hy.tmc.cli.backend.communication.CourseSubmitter;
import hy.tmc.cli.backend.communication.SubmissionInterpreter;
import hy.tmc.cli.configuration.ClientData;
import hy.tmc.cli.frontend.communication.server.ExpiredException;
import hy.tmc.cli.frontend.communication.server.ProtocolException;
import hy.tmc.cli.testhelpers.FrontendStub;
import java.io.IOException;
import java.text.ParseException;
import org.junit.After;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.when;

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
        submitterMock = Mockito.mock(CourseSubmitter.class);
        when(submitterMock.submitPaste(Mockito.anyString())).thenReturn(pasteUrl);

        front = new FrontendStub();
        paste = new Paste(front, submitterMock);
        ClientData.setUserData("Bossman", "Samu");
    }

    @After
    public void clean() {
        ClientData.clearUserData();
    }

    @Test
    public void submitReturnsBadOutputWhenCodeIsBad() throws ProtocolException, InterruptedException {
        front.start();
        paste.setParameter("path", "/hieno/path");
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
        }
        catch (ProtocolException p) {
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
