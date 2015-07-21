package hy.tmc.cli.backend.communication;

import hy.tmc.cli.configuration.ClientData;
import hy.tmc.cli.frontend.communication.server.ProtocolException;
import hy.tmc.cli.frontend.formatters.CommandLineSubmissionResultFormatter;
import hy.tmc.cli.testhelpers.ExampleJson;
import hy.tmc.core.communication.HttpResult;
import hy.tmc.core.communication.UrlCommunicator;
import java.io.IOException;
import org.junit.After;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.Test;
import org.powermock.api.mockito.PowerMockito;

public class SubmissionInterpreterTest {

    SubmissionInterpreter submissionInterpreter;
    String url = "https://tmc.mooc.fi/staging/submissions/1764.json?api_version=7";

    @Before
    public void setup() {
        ClientData.setUserData("chang", "paras");

        submissionInterpreter = new SubmissionInterpreter(new CommandLineSubmissionResultFormatter());
    }

    @After
    public void teardown() {
        ClientData.clearUserData();
    }

    private void initFailedMock() throws IOException, ProtocolException {
        HttpResult fakeResult = new HttpResult(ExampleJson.failedSubmission, 200, true);
        
    }

    private void initSuccessMock() throws IOException, ProtocolException {
        HttpResult fakeResult = new HttpResult(ExampleJson.successfulSubmission, 200, true);
        fail("implement this mock");
    }

    private void initFailedCheckstyle() throws IOException, ProtocolException {
        HttpResult fakeResult = new HttpResult(ExampleJson.checkstyleFailed, 200, true);
        fail("implement this mock");
    }

    private void initFailedValgrind() throws IOException, ProtocolException {
        HttpResult fakeResult = new HttpResult(ExampleJson.valgrindFailed, 200, true);
        fail("implement this mock");
    }

//    @Test
//    public void passedResultOutputsPassed() throws InterruptedException, IOException, ProtocolException {
//        initSuccessMock();
//        String output = submissionInterpreter.summarize(url, false);
//        assertTrue(output.contains("passed"));
//    }
//
//    @Test
//    public void failedResultOutputsFailed() throws InterruptedException, IOException, ProtocolException {
//        initFailedMock();
//
//        String output = submissionInterpreter.resultSummary(url, false);
//        assertTrue(output.contains("failed"));
//
//    }
//
//    @Test
//    public void failedResultOutputContainsFailedMessages()
//            throws InterruptedException, IOException, ProtocolException {
//        initFailedMock();
//
//        String output = submissionInterpreter.resultSummary(url, false);
//        assertTrue(output.contains("et tulosta mitään!"));
//    }
//
//    @Test
//    public void succesfulResultOutputContainsPassedTestsIfDetailedOn()
//            throws InterruptedException, IOException, ProtocolException {
//        initSuccessMock();
//
//        String output = submissionInterpreter.resultSummary(url, true);
//        assertTrue(output.contains("PASSED"));
//        assertTrue(output.contains("KayttajatunnuksetTest sopivatKayvat"));
//
//    }
//
//    @Test
//    public void successfulResultOutputDoesntContainPassedTestsIfDetailedOn()
//            throws InterruptedException, IOException, ProtocolException {
//        initSuccessMock();
//
//        String output = submissionInterpreter.resultSummary(url, false);
//        assertFalse(output.contains("PASSED"));
//        assertFalse(output.contains("KayttajatunnuksetTest sopivatKayvat"));
//
//    }
//
//    @Test
//    public void resultWithCheckstyleContainsCheckstyleErrors() throws InterruptedException, IOException, ProtocolException {
//        initFailedCheckstyle();
//
//        String output = submissionInterpreter.resultSummary(url, true);
//        assertTrue(output.contains("checkstyle"));
//        assertTrue(output.contains("Class length is 478 lines (max allowed is 300)"));
//        assertTrue(output.contains("',' is not followed by whitespace."));
//    }
//
//    @Test
//    public void resultWithCheckstyleContainsLineNumberMarkings() throws InterruptedException, IOException, ProtocolException {
//        initFailedCheckstyle();
//
//        String output = submissionInterpreter.resultSummary(url, true);
//        assertTrue(output.contains("On line: 421 Column: 24"));
//        assertTrue(output.contains("On line: 202 Column: 18"));
//    }
//
//    @Test
//    public void resultWithNoCheckstyleDoesntContainCheckstyleErrors() throws InterruptedException, IOException, ProtocolException {
//        initSuccessMock();
//
//        String output = submissionInterpreter.resultSummary(url, true);
//        assertFalse(output.contains("checkstyle"));
//    }
//
//    @Test
//    public void resultWithValgridShowsValgrind() throws InterruptedException, IOException, ProtocolException {
//        initFailedValgrind();
//
//        String output = submissionInterpreter.resultSummary(url, true);
//        assertTrue(output.contains(": srunner_run_all (in /tmc/t"));
//        assertTrue(output.contains("stack size used in this run was 8388608."));
//    }
//
//    @Test
//    public void resultWithNoValgrindShowsNoValgrind() throws InterruptedException, IOException, ProtocolException {
//        initFailedMock();
//
//        String output = submissionInterpreter.resultSummary(url, true);
//        assertFalse(output.contains("Access not within mapped region at address"));
//
//    }
}
