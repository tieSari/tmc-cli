package hy.tmc.cli.frontend.communication.commands;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

import hy.tmc.cli.backend.communication.CourseSubmitter;
import hy.tmc.cli.backend.communication.SubmissionInterpreter;
import hy.tmc.cli.configuration.ClientData;

import com.github.tomakehurst.wiremock.junit.WireMockRule;

import hy.tmc.cli.frontend.communication.server.ExpiredException;
import hy.tmc.cli.frontend.communication.server.ProtocolException;
import hy.tmc.cli.frontend.formatters.CommandLineSubmissionResultFormatter;
import hy.tmc.cli.frontend.formatters.SubmissionResultFormatter;

import java.io.IOException;
import java.text.ParseException;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mockito;

public class SubmitTest {

    private Submit submit;
    CourseSubmitter submitterMock;
    SubmissionResultFormatter formatter;
    private SubmissionInterpreter interpreter;
    private final String submissionUrl = "/submissions/1781.json?api_version=7";

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(8080);

    /**
     * Mocks CourseSubmitter and injects it into Submit command.
     */
    @Before
    public void setup() throws IOException, InterruptedException, IOException, ParseException, ExpiredException, Exception {
        submitterMock = Mockito.mock(CourseSubmitter.class);
        when(submitterMock.submit(Mockito.anyString())).thenReturn("http://127.0.0.1:8080" + submissionUrl);
        formatter = Mockito.mock(CommandLineSubmissionResultFormatter.class);
        interpreter = Mockito.mock(SubmissionInterpreter.class);
        submit = new Submit(submitterMock, interpreter);
        ClientData.setUserData("Bossman", "Samu");
    }

    private void wireMockStart(String json) {
        stubFor(get(urlEqualTo(submissionUrl))
                .willReturn(aResponse()
                        .withBody(json)));
    }

    @After
    public void clean() {
        ClientData.clearUserData();
    }

    @Test
    public void submitReturnsBadOutputWhenCodeIsBad() throws Exception {
        when(interpreter.resultSummary(Mockito.anyString(), Mockito.anyBoolean())).thenReturn("No tests passed.");
        submit.setParameter("path", "/hieno/path");
        String result = submit.parseData(submit.call()).get();
        assertTrue(result.contains("No tests passed."));
    }

    @Test
    public void submitPrintsAllTestsPassedWhenCodeIsCorrect() throws Exception {
        when(interpreter.resultSummary(Mockito.anyString(), Mockito.anyBoolean())).thenReturn("All tests passed.");
        submit.setParameter("path", "/hieno/path");
        String result = submit.parseData(submit.call()).get();

        assertTrue(result.contains("All tests passed."));
    }

    /**
     * Check that data checking success.
     */
    @Test
    public void testCheckDataSuccess() {
        Submit submitCommand = new Submit();
        submitCommand.setParameter("path", "/home/tmccli/testi");
        try {
            submitCommand.checkData();
        } catch (ProtocolException p) {
            fail("testCheckDataSuccess failed");
        }
    }

    /**
     * Check that if user didn't give correct data, data checking fails.
     */
    @Test(expected = ProtocolException.class)
    public void testCheckDataFail() throws ProtocolException {
        Submit submitCommand = new Submit();
        submitCommand.checkData();
    }

    @Test(expected = ProtocolException.class)
    public void checkDataFailIfNoAuth() throws ProtocolException {
        Submit submitCommand = new Submit();
        ClientData.clearUserData();
        submitCommand.checkData();
    }

}
