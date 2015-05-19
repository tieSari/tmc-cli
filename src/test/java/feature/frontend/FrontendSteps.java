package feature.frontend;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import hy.tmc.cli.testhelpers.Helper;

import static org.junit.Assert.assertTrue;

public class FrontendSteps {

    private String output;
    
    @Given("^a help command\\.$")
    public void a_help_command() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        Helper helper = new Helper();
        output = helper.printOutput("help", "scripts/frontend.sh");

    }

    @Then("^output should contains commands\\.$")
    public void output_should_contains_commands() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        assertTrue(output.contains("Commands: "));
    }
}