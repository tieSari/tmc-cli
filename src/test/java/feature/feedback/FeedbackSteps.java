package feature.feedback;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import gherkin.lexer.Fr;
import hy.tmc.cli.frontend.communication.commands.Submit;
import hy.tmc.cli.testhelpers.FrontendStub;

public class FeedbackSteps {

    private Submit submitter;
    private FrontendStub front;
    
    public FeedbackSteps() {
        front = new FrontendStub();
        submitter = new Submit(front);
    }
    
    @Given("^an exercise where some tests fail$")
    public void anExerciseWhereSomeTestsFail() throws Throwable {
    }

    @When("^the exercise is submitted$")
    public void theExerciseIsSubmitted() throws Throwable {
    }

    @Then("^feedback questions will not be asked$")
    public void feedbackQuestionsWillNotBeAsked() {
    }

    @Given("^the user has submitted a successful exercise$")
    public void theUserHasSubmittedASuccessfulExercise() {
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
}
