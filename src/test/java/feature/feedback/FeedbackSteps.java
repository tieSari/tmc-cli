package feature.feedback;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import hy.tmc.cli.frontend.FeedbackHandler;
import hy.tmc.cli.frontend.communication.commands.Authenticate;
import hy.tmc.cli.frontend.communication.commands.ChooseServer;
import hy.tmc.cli.frontend.communication.server.ProtocolException;
import hy.tmc.cli.testhelpers.FrontendStub;
import hy.tmc.cli.testhelpers.ServerMock;
import java.io.IOException;
import static org.junit.Assert.fail;

public class FeedbackSteps {

    private FrontendStub front;
    private FeedbackHandler handler;
    private ServerMock server;
    private Authenticate login;
    private ChooseServer setserver;
    
    public FeedbackSteps() throws IOException {
        front = new FrontendStub();
        handler = new FeedbackHandler(front);
        server = new ServerMock(handler);
    }
     
    @Given("^an exercise where some tests fail$")
    public void anExerciseWhereSomeTestsFail() {
        
    }

    @When("^the exercise is submitted$")
    public void theExerciseIsSubmitted() throws Throwable {
        
    }

    @Then("^feedback questions will not be asked$")
    public void feedbackQuestionsWillNotBeAsked() {
        for (String line : front.getAllLines()){
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
        System.out.println("server says: "+server.getLastLine());
    }

    @Then("^feedback is sent to the server successfully$")
    public void feedbackIsSentToTheServerSuccessfully() {
    }

    @When("^the user gives some answer that's not in the correct range$")
    public void theUserGivesSomeAnswerThatsNotInTheCorrectRange() {
    }
}
