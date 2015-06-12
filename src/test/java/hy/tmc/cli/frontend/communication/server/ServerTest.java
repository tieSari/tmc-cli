package hy.tmc.cli.frontend.communication.server;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import hy.tmc.cli.configuration.ConfigHandler;
import hy.tmc.cli.domain.submission.FeedbackQuestion;
import hy.tmc.cli.testhelpers.TestClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class ServerTest {

    private Server server;
    private Thread serverThread;

    private final int wiremockPort = 4653;
    @Rule
    public WireMockRule wireMockRule = new WireMockRule(wiremockPort);

    private final String wiremockServer = "127.0.0.1";
    private final String wiremockAddress = "http://" + wiremockServer + ":" + wiremockPort;
    private final String feedbackEndpoint = "/feedback";
    private final String feedbackStubEndpoint = feedbackEndpoint
            + "?" + new ConfigHandler().apiParam;

    /**
     * Setups the server and starts it in a thread.
     */
    @Before
    public void setUp() throws Exception {
        this.server = new Server();
        this.serverThread = new Thread(server);

        serverThread.start();
    }

    @After
    public void tearDown() throws Exception {
        serverThread.interrupt();
    }

    @Test(timeout = 10000)
    public void serverIsResponding() throws IOException {
        TestClient client = createTestClient();

        client.sendMessage("ping");

        assertEquals("pong", client.reply());
    }

    @Test(expected = java.net.ConnectException.class)
    public void serverCantBeConnectedAfterShutdown() throws IOException {
        int port = server.getCurrentPort();
        server.close();
        serverThread.interrupt();

        TestClient client = new TestClient(port);
        client.sendMessage("ping");
        assertNotEquals("pong", client.reply());
    }

    @Test(timeout = 15000)
    public void feedbackAnswerSendsCorrectJson() throws IOException {
        List<FeedbackQuestion> questions = getFeedbackQuestions();

        stubFor(post(urlEqualTo(feedbackStubEndpoint))
            .willReturn(aResponse().withStatus(200)));

        TestClient client = createTestClient();

        server.feedback(questions, wiremockAddress + feedbackEndpoint);

        String reply = waitForInput(client);

        assertEquals("Hello", reply);
        assertEquals("text", client.reply());
        answerQuestion(client, "yo", "text");

        reply = waitForInput(client);
        assertEquals("who are you", reply);
        assertEquals("text", client.reply());

        client = createTestClient();
        answerQuestion(client, "sup", "text");

        assertEquals("end", waitForInput(client));

        waitForInput(client);

        verify(postRequestedFor(urlEqualTo(feedbackStubEndpoint))
            .withRequestBody(equalTo("{\"answers"
                + "\":[{\"question_id\":1,\"answer\":\"yo\"},"
                + "{\"question_id\":2,\"answer\":\"sup\"}]}")));
    }

    private void answerQuestion(TestClient client, String answer, String kind) throws IOException {
        client.sendMessage("answerQuestion answer " + answer + " kind " + kind);
    }

    private String waitForInput(TestClient client) {
        String reply = null;
        while (reply == null) {
            reply = client.reply();
        }
        return reply;
    }

    private TestClient createTestClient() throws IOException {
        return new TestClient(server.getCurrentPort());
    }

    private List<FeedbackQuestion> getFeedbackQuestions() {
        List<FeedbackQuestion> questions = new ArrayList<>();

        FeedbackQuestion q1 = new FeedbackQuestion();
        FeedbackQuestion q2 = new FeedbackQuestion();

        q1.setId(1);
        q1.setQuestion("Hello");
        q1.setKind("text");
        q2.setId(2);
        q2.setQuestion("who are you");
        q2.setKind("text");

        questions.add(q1);
        questions.add(q2);
        return questions;
    }

    /* @Test
    public void testFeedback() throws Exception {

    }

    @Test
    public void testAskQuestion() throws Exception {

    }

    @Test
    public void testFeedbackAnswer() throws Exception {

    } */
}