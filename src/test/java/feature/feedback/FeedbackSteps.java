package feature.feedback;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.containing;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.junit.Assert.fail;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import cucumber.api.java.After;

import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import hy.tmc.cli.configuration.ClientData;

import org.junit.Rule;

import hy.tmc.cli.configuration.ConfigHandler;
import hy.tmc.cli.frontend.FeedbackHandler;
import hy.tmc.cli.frontend.communication.commands.Authenticate;
import hy.tmc.cli.frontend.communication.commands.ChooseServer;
import hy.tmc.cli.frontend.communication.server.ProtocolException;
import hy.tmc.cli.frontend.communication.server.Server;
import hy.tmc.cli.testhelpers.ExampleJson;
import hy.tmc.cli.testhelpers.FrontendStub;
import hy.tmc.cli.testhelpers.TestClient;

import java.io.IOException;

public class FeedbackSteps {

    private FrontendStub front;
    private FeedbackHandler handler;

    private int port;

    private Thread serverThread;
    private TestClient testClient;
    private Server server;

    private ConfigHandler configHandler; // writes the test address
    private WireMockServer wireMockServer;

    public FeedbackSteps() throws IOException {
        front = new FrontendStub();
        handler = new FeedbackHandler(front);
        server = new Server(handler);
    }

    @Rule
    WireMockRule wireMockRule = new WireMockRule();

    @Before
    public void initializeServer() throws IOException {
        configHandler = new ConfigHandler();
        configHandler.writeServerAddress("http://127.0.0.1:8080");

        server = new Server();
        port = configHandler.readPort();
        serverThread = new Thread(server);
        serverThread.start();
        testClient = new TestClient(port);

        startWireMock();
    }

    private void startWireMock() {
        wireMockServer = new WireMockServer();
        wireMockServer.start();

        wireMockServer.stubFor(get(urlEqualTo("/user"))
                .withHeader("Authorization", containing("Basic dGVzdDoxMjM0"))
                .willReturn(
                        aResponse()
                        .withStatus(200)
                )
        );
        wiremockGET("/courses.json?api_version=7", ExampleJson.allCoursesExample);
        wiremockGET("/courses/3.json?api_version=7", ExampleJson.courseExample);
        wiremockPOST("/exercises/286/submissions.json?api_version=7", ExampleJson.submitResponse);
        wiremockGET("/submissions/1781.json?api_version=7", ExampleJson.successfulSubmission);
    }

    private void wiremockGET(final String urlToMock, final String returnBody) {
        wireMockServer.stubFor(get(urlEqualTo(urlToMock))
                .willReturn(aResponse()
                        .withBody(returnBody)
                )
        );
    }

    private void wiremockPOST(final String urlToMock, final String returnBody) {
        wireMockServer.stubFor(post(urlEqualTo(urlToMock))
                .willReturn(aResponse()
                        .withBody(returnBody)
                )
        );
    }

    @Given("^an exercise where some tests fail$")
    public void anExerciseWhereSomeTestsFail() {

    }

    @When("^the exercise is submitted$")
    public void theExerciseIsSubmitted() throws Throwable {

    }

    @Then("^feedback questions will not be asked$")
    public void feedbackQuestionsWillNotBeAsked() {
        for (String line : front.getAllLines()) {
            if (line.contains("feedback")) {
                fail("should not ask feedback when there isn't any");
            }
        }
    }

    @Given("^the user has submitted a successful exercise$")
    public void theUserHasSubmittedASuccessfulExercise() throws ProtocolException {
    }

    @When("^the user has answered all feedback questions$")
    public void theUserHasAnsweredAllFeedbackQuestions() {
    }

    @Then("^feedback is sent to the server successfully$")
    public void feedbackIsSentToTheServerSuccessfully() {
    }

    @When("^the user gives some answer that's not in the correct range$")
    public void theUserGivesSomeAnswerThatsNotInTheCorrectRange() {
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
        wireMockServer.stop();
        configHandler.writeServerAddress("http://tmc.mooc.fi/staging");
        ClientData.clearUserData();
    }
}
