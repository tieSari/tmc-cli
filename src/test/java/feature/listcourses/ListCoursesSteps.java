package feature.listcourses;

import cucumber.api.PendingException;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import hy.tmc.cli.testhelpers.Helper;
import java.io.File;
import org.junit.Assert;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ListCoursesSteps {

    private final String scriptLocation = "scripts/frontend.sh";
    private final String commandName = "listCourses";

    private Helper helper;
    private Process loginDialog;
    private String output;

    @Given("^user has run shutdown script\\.$")
    public void user_has_run_shutdown_script() throws Throwable {
        helper = new Helper();
        helper.printOutput("", "scripts/shutdown.sh"); //to ensure that user is not logged in.
    }

    @When("^user gives command listCourses\\.$")
    public void user_gives_command_listCourses() throws Throwable {
        output = helper.printOutput(commandName, scriptLocation);
    }

    @Then("^output should contain only one line\\.$")
    public void output_should_contain_only_one_line() throws Throwable {
        //please be free to feractor this!
       // System.out.println(output);
       // assertTrue(output.length() < 80);
    }

    @Given("^user has logged in with username \"(.*?)\" and password \"(.*?)\"\\.$")
    public void user_has_logged_in_with_username_and_password(String username, String password) throws Throwable {
        File config = new File("scripts/config");
        config.delete();
        helper = new Helper();
        loginDialog = helper.startDialogWithCommand("login", "scripts/frontend.sh");
        loginDialog = helper.writeInputToProcess(loginDialog, username);
        loginDialog = helper.writeInputToProcess(loginDialog, password);
        loginDialog.waitFor();
    }

    @Then("^output should contain more than one line$")
    public void output_should_contain_more_than_one_line() throws Throwable {
        assertTrue(output.contains("\n"));
    }

}
