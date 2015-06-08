package feature.feedback;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.fail;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.stubbing.ListStubMappingsResult;
import com.github.tomakehurst.wiremock.stubbing.StubMapping;
import cucumber.api.java.After;

import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import hy.tmc.cli.configuration.ClientData;

import hy.tmc.cli.configuration.ConfigHandler;
import hy.tmc.cli.frontend.FeedbackHandler;
import hy.tmc.cli.frontend.communication.commands.Submit;
import hy.tmc.cli.frontend.communication.server.ProtocolException;
import hy.tmc.cli.frontend.communication.server.Server;
import hy.tmc.cli.testhelpers.ExampleJson;
import hy.tmc.cli.testhelpers.FrontendStub;
import hy.tmc.cli.testhelpers.TestClient;

import java.io.IOException;

public class FeedbackSteps {

    private FrontendStub frontStub;
    private FeedbackHandler handler;

    // private String exercisePath = ;

    private int port;

    private Thread serverThread;
    private TestClient testClient;
    private Server server;

    private ConfigHandler configHandler; // writes the test address
    private final String serverHost = "127.0.0.1";
    private int serverPort;
    private WireMockServer wireMockServer;
    private WireMock wireMock;
    private String wiremockAddress;
    private String feedbackAnswersUrl;

    @Before
    public void initializeServer() throws IOException {
        // System.out.println("doing before");

        configHandler = new ConfigHandler();
        startWireMock();
        wiremockAddress = "http://" + serverHost + ":" + serverPort;
        configHandler.writeServerAddress(wiremockAddress);
        System.out.println(wiremockAddress);
        frontStub = new FrontendStub();
        handler = new FeedbackHandler(frontStub);
        server = new Server(handler);


        serverThread = new Thread(server);
        serverThread.start();
        port = configHandler.readPort();
        testClient = new TestClient(port);



        testClient.sendMessage("auth username test password lolxd");

        String reply = testClient.reply();

        if (reply.equals("Auth unsuccessful. Check your connection and/or credentials")) {
            fail("auth failed");
        }

        testClient = new TestClient(port);

    }

    private void startWireMock() {
        wireMockServer = new WireMockServer();
        wireMockServer.start();
        serverPort = wireMockServer.port();
        WireMock.configureFor(serverHost, serverPort);

        // this.wireMock = new WireMock(serverHost, serverPort);


        /* while (!wireMockServer.isRunning()) {
            System.out.println("starting");
        } */

        wiremockGET("/courses.json?api_version=7", ExampleJson.allCoursesExample);
        wiremockGET("/courses/27.json?api_version=7", ExampleJson.feedbackCourse.replace(
                "https://tmc.mooc.fi/staging/exercises/1653/submissions.json",
                wiremockAddress + "/submissions.json"
        ));
        wiremockPOST("/submissions.json?" + configHandler.apiParam, ExampleJson.submitResponse.replace(
                "8080", serverPort + ""
        ));
        wiremockGET("/submissions/1781.json?" + configHandler.apiParam, ExampleJson.feedbackExample.replace(
                "https://tmc.mooc.fi/staging/submissions/1933/feedback_answers.json",
                wiremockAddress + "/feedback_answers.json"
        ));
        feedbackAnswersUrl = "/feedback_answers.json?" + configHandler.apiParam;
        wiremockPOST(feedbackAnswersUrl, "{ status: \"ok\" }");
        wiremockGET("/user", "");

/*        System.out.println("here are mappings");
        ListStubMappingsResult listStubMappingsResult = listAllStubMappings();
        for (StubMapping stubMapping : listStubMappingsResult.getMappings()) {
            System.out.println(stubMapping);
        }*/

    }

    private void wiremockGET(final String urlToMock, final String returnBody) {
        stubFor(get(urlEqualTo(urlToMock))
                        .willReturn(aResponse()
                                        .withStatus(200)
                                        .withBody(returnBody)
                        )
        );
    }

    private void wiremockPOST(final String urlToMock, final String returnBody) {
        stubFor(post(urlEqualTo(urlToMock))
                        .willReturn(aResponse()
                                        .withBody(returnBody)
                        )
        );
    }

    private String feedbackAnswer(String answer) {
        if (answer.contains(" ")) {
            return "answerQuestion answer { "+ answer +" }";
        }
        return "answerQuestion answer "+ answer;
    }

    @Given("^an exercise where some tests fail$")
    public void anExerciseWhereSomeTestsFail() throws IOException {
        //wiremockGET("/submissions/1781.json?" + configHandler.apiParam, ExampleJson.feedbackExample.replace(
        //        "https://tmc.mooc.fi/staging/submissions/1933/feedback_answers.json",
        //        wiremockAddress + "/feedback_answers.json"));
        //System.out.println("nyt pitäisi tulla after");
    }

    @When("^the exercise is submitted$")
    public void theExerciseIsSubmitted() throws Throwable {
        sendExercise("/testResources/tmc-testcourse/trivial");
    }

    @Then("^feedback questions will not be asked$")
    public void feedbackQuestionsWillNotBeAsked() throws IOException, InterruptedException {
        //System.out.println("moi");
        String reply = testClient.reply();
        //System.out.println("vastaus: ");
        //System.out.println(reply);
        while(testClient.hasNewMessages()) {
            //System.out.println("------------");
            //System.out.println(reply);
            if (reply.contains("feedback")) {
                //System.out.println("feilas");
                fail("asked for feedback, even though tests failed");
            }
            reply = testClient.reply();
        }
    }



    @Given("^the user has submitted a successful exercise$")
    public void theUserHasSubmittedASuccessfulExercise() throws ProtocolException, IOException {
        sendExercise("/testResources/tmc-testcourse/trivial");
    }

    private void sendExercise(String exercisePath) throws IOException {
        String submitCommand = "submit path ";
        String submitPath = System.getProperty("user.dir") + exercisePath;
        final String message = submitCommand + submitPath;
        testClient.sendMessage(message);
        testClient.reply();
    }

    @When("^the user has answered all feedback questions$")
    public void theUserHasAnsweredAllFeedbackQuestions() throws IOException {
        // intrange [0..10]
        feedbackAnswer("3");
        // intrange [10..100]
        testClient = new TestClient(port);
        feedbackAnswer("42");
        // text
        testClient = new TestClient(port);
        feedbackAnswer("Hello world!");
    }

    @Then("^feedback is sent to the server successfully$")
    public void feedbackIsSentToTheServerSuccessfully() {
        /*verify(postRequestedFor(urlEqualTo(feedbackAnswersUrl))
        .withRequestBody(equalToJson("{ answers: [{ id: 30, answer: \"3\"}, { id: 31, answer: \"Hello world!\"}, { id: 32, answer: \"42\"} ]}")));*/
    }

    @When("^the user gives some answer that's not in the correct range$")
    public void theUserGivesSomeAnswerThatsNotInTheCorrectRange() throws IOException {
        // intrange [0..10]
        feedbackAnswer("12");
        // intrange [10..100]
        testClient = new TestClient(port);
        feedbackAnswer("150");
        // text
        testClient = new TestClient(port);
        feedbackAnswer("Hello world!");
    }

    @Given("^an exercise with no feedback$")
    public void anExerciseWithNoFeedback() {
    }

    @When("^the user submits and all tests pass$")
    public void theUserSubmitsAndAllTestsPass() {
    }

    @Then("^no feedback questions are asked$")
    public void noFeedbackQuestionsAreAsked() {
    }

    @After
    public void closeAll() throws IOException {
        server.close();
        serverThread.interrupt();
        WireMock.reset();
        wireMockServer.stop();
        configHandler.writeServerAddress("http://tmc.mooc.fi/staging");
        ClientData.clearUserData();
    }
}
