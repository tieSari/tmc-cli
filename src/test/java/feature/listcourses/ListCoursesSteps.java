package feature.listcourses;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;


public class ListCoursesSteps {


    @Given("^user has run shutdown script\\.$")
    public void user_has_run_shutdown_script() throws Throwable {
 
    }

    @When("^user gives command listCourses\\.$")
    public void user_gives_command_listCourses() throws Throwable {
       
    }

    @Then("^output should contain only one line\\.$")
    public void output_should_contain_only_one_line() throws Throwable {
        //please be free to feractor this!
       // System.out.println(output);
       // assertTrue(output.length() < 80);
    }

    @Given("^user has logged in with username \"(.*?)\" and password \"(.*?)\"\\.$")
    public void user_has_logged_in_with_username_and_password(String username, String password) throws Throwable {
   
    }

    @Then("^output should contain more than one line$")
    public void output_should_contain_more_than_one_line() throws Throwable {
    }

}
