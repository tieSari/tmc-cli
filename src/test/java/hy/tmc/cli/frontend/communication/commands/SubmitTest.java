package hy.tmc.cli.frontend.communication.commands;

import hy.tmc.cli.backend.Mailbox;
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

public class SubmitTest {

    private FrontendStub front;
    private Submit submit;
    CourseSubmitter submitterMock;
    SubmissionInterpreter interpreter;

    /**
     * Mocks CourseSubmitter and injects it into Submit command.
     */
    @Before
    public void setup() throws IOException, InterruptedException, IOException, ParseException, ExpiredException {
        Mailbox.create();
        ClientData.setPolling(true);
        submitterMock = Mockito.mock(CourseSubmitter.class);
        when(submitterMock.submit(Mockito.anyString())).thenReturn("http://127.0.0.1:8080/submissions/1781.json?api_version=7");

        interpreter = Mockito.mock(SubmissionInterpreter.class);
        
        front = new FrontendStub();
        submit = new Submit(front, submitterMock, interpreter);
        ClientData.setUserData("Bossman", "Samu");
    }

    @After
    public void clean() {
        ClientData.clearUserData();
    }

    @Test
    public void submitReturnsBadOutputWhenCodeIsBad() throws ProtocolException, InterruptedException {
        front.start();
        when(interpreter.resultSummary(Mockito.anyString(), Mockito.anyBoolean())).thenReturn("No tests passed.");

        submit.setParameter("path", "/hieno/path");
        submit.execute();
        String result = front.getMostRecentLine();
        assertTrue(result.contains("No tests passed."));
    }

    @Test
    public void submitPrintsAllTestsPassedWhenCodeIsCorrect() throws ProtocolException, InterruptedException {
        front.start();
        when(interpreter.resultSummary(Mockito.anyString(), Mockito.anyBoolean())).thenReturn("All tests passed.");

        submit.setParameter("path", "/hieno/path");
        submit.execute();
        String result = front.getMostRecentLine();
        assertTrue(result.contains("All tests passed."));
    }

    /**
     * Check that data checking success.
     */
    @Test
    public void testCheckDataSuccess() {
        Submit submitCommand = new Submit(front);
        submitCommand.setParameter("path", "/home/tmccli/testi");
        try {
            submitCommand.checkData();
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
        Submit submitCommand = new Submit(front);
        submitCommand.checkData();
    }

    @Test(expected = ProtocolException.class)
    public void checkDataFailIfNoAuth() throws ProtocolException {
        Submit submitCommand = new Submit(front);
        ClientData.clearUserData();
        submitCommand.checkData();
    }
}
