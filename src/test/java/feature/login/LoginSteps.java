package feature.login;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import hy.tmc.cli.testhelpers.Helper;
import static org.junit.Assert.assertTrue;

public class LoginSteps {

    private Helper helper;
    private Process loginDialog;

    @Given("^a login command\\.$")
    public void a_login_command() throws Throwable {
        helper = new Helper();
        loginDialog = helper.startDialogWithCommand("login", "scripts/frontend.sh");
    }

    @When("^user gives username \"(.*?)\"$")
    public void user_gives_username(String username) throws Throwable {
        loginDialog = helper.writeInputToProcess(loginDialog, username);
    }

    @When("^user gives password \"(.*?)\"$")
    public void user_gives_password(String password) throws Throwable {
        loginDialog = helper.writeInputToProcess(loginDialog, password);
    }

    @Then("^user should see result\\.$")
    public void user_should_see_result() throws Throwable {
        String output = helper.readOutputFromProcess(loginDialog);
        //assertTrue(output.contains("Auth successful."));
        assertTrue(output.contains("successful"));
    }
}
