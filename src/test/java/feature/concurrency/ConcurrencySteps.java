package feature.concurrency;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;

public class ConcurrencySteps {

    @Given("^user starts commands \"(.*?)\" and \"(.*?)\"$")
    public void user_starts_commands_and(String arg1, String arg2) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
       //throw new PendingException();
    }

    @Then("^user sees output of both commands$")
    public void user_sees_output_of_both_commands() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        //throw new PendingException();
    }

}
