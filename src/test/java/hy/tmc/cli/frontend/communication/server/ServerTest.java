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
import hy.tmc.cli.testhelpers.FeedbackBuilder;
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
    public void feedbackAnswerSendsCorrectJsonWithOnlyTextQuestions() throws IOException {
        List<FeedbackQuestion> questions = getTextFeedbackQuestions();

        TestClient client = prepareClientAndServer(questions);
        
        verifyQuestion(client, "Hello", "text");
        answerQuestion(client, "yo", "text");
        
        verifyQuestion(client, "who are you", "text");
        client = createTestClient();
        answerQuestion(client, "sup", "text");

        assertEquals("end", waitForInput(client));

        waitForInput(client);

        verify(postRequestedFor(urlEqualTo(feedbackStubEndpoint))
            .withRequestBody(equalTo("{\"answers"
                + "\":[{\"question_id\":1,\"answer\":\"yo\"},"
                + "{\"question_id\":2,\"answer\":\"sup\"}]}")));
    }
    
    @Test(timeout = 15000)
    public void feedbackAnswerSendsCorrectJsonWithOnlyRangeQuestions() throws IOException {
        List<FeedbackQuestion> questions = getRangeFeedbackQuestions();

        TestClient client = prepareClientAndServer(questions);
        
        verifyQuestion(client, "how many points",
                "Please give your answer as an integer within [0..10] (inclusive)");
        answerQuestion(client, "8", "int");
        
        verifyQuestion(client, "plus or minus",
                "Please give your answer as an integer within [-10..10] (inclusive)");
        client = createTestClient();
        answerQuestion(client, "-2", "int");
        
        verifyQuestion(client, "how enlightening was the exercise?",
                "Please give your answer as an integer within [0..10] (inclusive)");
        client = createTestClient();
        answerQuestion(client, "5", "int");

        assertEquals("end", waitForInput(client));

        waitForInput(client);

        verify(postRequestedFor(urlEqualTo(feedbackStubEndpoint))
            .withRequestBody(equalTo("{\"answers"
                + "\":[{\"question_id\":4,\"answer\":\"8\"},"
                + "{\"question_id\":5,\"answer\":\"-2\"},"
                + "{\"question_id\":6,\"answer\":\"5\"}]}")));
    }
    
    @Test(timeout = 15000)
    public void feedbackAnswerSendsCorrectJsonWithBothKindsOfQuestions() throws IOException {
        List<FeedbackQuestion> questions = getFeedbackQuestions();

        TestClient client = prepareClientAndServer(questions);
        
        verifyQuestion(client, "what is your powerlevel?", 
                "Please give your answer as an integer within [-10..10] (inclusive)");
        answerQuestion(client, "10", "int");
        
        verifyQuestion(client, "how many points",
                "Please give your answer as an integer within [0..10] (inclusive)");
        client = createTestClient();
        answerQuestion(client, "7", "int");
        
        verifyQuestion(client, "Pizza or Kebab?", "text");
        client = createTestClient();
        answerQuestion(client, "sushi", "text");
        
        verifyQuestion(client, "feels of the day", "text");
        client = createTestClient();
        answerQuestion(client, "the sun is shining, yay!", "text");

        assertEquals("end", waitForInput(client));

        waitForInput(client);

        verify(postRequestedFor(urlEqualTo(feedbackStubEndpoint))
            .withRequestBody(equalTo("{\"answers"
                + "\":[{\"question_id\":48,\"answer\":\"10\"},"
                + "{\"question_id\":50,\"answer\":\"7\"},"
                + "{\"question_id\":47,\"answer\":\"sushi\"},"
                + "{\"question_id\":49,\"answer\":\"the sun is shining, yay!\"}]}")));
    }
    
    private void verifyQuestion(TestClient client, String question, String type) {
        String reply = waitForInput(client);

        assertEquals(question, reply);
        assertEquals(type, client.reply());
    }
    
    private TestClient prepareClientAndServer(List<FeedbackQuestion> questions) throws IOException {
        stubFor(post(urlEqualTo(feedbackStubEndpoint))
            .willReturn(aResponse().withStatus(200)));

        TestClient client = createTestClient();

        server.feedback(questions, wiremockAddress + feedbackEndpoint);
        return client;
    }

    private void answerQuestion(TestClient client, String answer, String kind) throws IOException {
        if (answer.contains(" ")) {
            answer = "{ " + answer + " }";
        }
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

    private List<FeedbackQuestion> getTextFeedbackQuestions() {
        FeedbackBuilder feedback = new FeedbackBuilder();
        feedback.withSimpleTextQuestion("Hello").withSimpleTextQuestion("who are you");
        return feedback.build();
    }
    
    private List<FeedbackQuestion> getRangeFeedbackQuestions() {
        FeedbackBuilder feedback = new FeedbackBuilder(4)
            .withBasicIntRangeQuestion("how many points")
            .withNegativeIntRange("plus or minus")
            .withBasicIntRangeQuestion("how enlightening was the exercise?");
        return feedback.build();
    }
    
    private List<FeedbackQuestion> getFeedbackQuestions() {
        FeedbackBuilder feedback = new FeedbackBuilder(47)
                .withSimpleTextQuestion("Pizza or Kebab?")
                .withNegativeIntRange("what is your powerlevel?")
                .withSimpleTextQuestion("feels of the day")
                .withBasicIntRangeQuestion("how many points");
        return feedback.build();
    }
}