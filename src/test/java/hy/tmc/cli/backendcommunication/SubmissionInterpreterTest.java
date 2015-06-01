package hy.tmc.cli.backendcommunication;


import hy.tmc.cli.backend.communication.HttpResult;
import hy.tmc.cli.backend.communication.SubmissionInterpreter;
import hy.tmc.cli.backend.communication.UrlCommunicator;
import hy.tmc.cli.configuration.ClientData;
import hy.tmc.cli.testhelpers.ExampleJson;
import org.junit.After;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.mockito.Mockito;


import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(UrlCommunicator.class)
public class SubmissionInterpreterTest {

    SubmissionInterpreter SI;
    String url = "https://tmc.mooc.fi/staging/submissions/1764.json?api_version=7";

    @Before
    public void setup() {
        PowerMockito.mockStatic(UrlCommunicator.class);

        ClientData.setUserData("chang", "paras");

        SI = new SubmissionInterpreter();
    }

    @After
    public void teardown() {
        ClientData.clearUserData();
    }

    private void initFailedMock() {
        HttpResult fakeResult = new HttpResult(ExampleJson.failedSubmission, 200, true);
        PowerMockito
                .when(UrlCommunicator.makeGetRequest(Mockito.anyString(),
                                Mockito.anyString()))
                .thenReturn(fakeResult);
    }

    private void initSuccessMock() {
        HttpResult fakeResult = new HttpResult(ExampleJson.successfulSubmission, 200, true);
        PowerMockito
                .when(UrlCommunicator.makeGetRequest(Mockito.anyString(),
                                Mockito.anyString()))
                .thenReturn(fakeResult);
    }

    @Test
    public void passedResultOutputsPassed() throws InterruptedException {
        initSuccessMock();

        String output = SI.resultSummary(url, false);
        assertTrue(output.contains("passed"));
    }

    @Test
    public void failedResultOutputsFailed() throws InterruptedException {
        initFailedMock();

        String output = SI.resultSummary(url, false);
        assertTrue(output.contains("failed"));

    }

    @Test
    public void failedResultOutputContainsFailedMessages()
            throws InterruptedException {
        initFailedMock();

        String output = SI.resultSummary(url, false);
        assertTrue(output.contains("et tulosta mitään!"));
    }

    @Test
    public void succesfulResultOutputContainsPassedTestsIfDetailedOn()
            throws InterruptedException {
        initSuccessMock();

        String output = SI.resultSummary(url, true);
        assertTrue(output.contains("PASSED"));
        assertTrue(output.contains("KayttajatunnuksetTest sopivatKayvat"));

    }

    @Test
    public void successfulResultOutputDoesntContainPassedTestsIfDetailedOn()
            throws InterruptedException {
        initSuccessMock();

        String output = SI.resultSummary(url, false);
        assertFalse(output.contains("PASSED"));
        assertFalse(output.contains("KayttajatunnuksetTest sopivatKayvat"));

    }
}
