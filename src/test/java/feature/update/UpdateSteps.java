package feature.update;

import com.github.tomakehurst.wiremock.WireMockServer;
import cucumber.api.PendingException;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import hy.tmc.cli.TmcCli;
import hy.tmc.cli.configuration.ConfigHandler;
import hy.tmc.cli.testhelpers.TestClient;
import hy.tmc.cli.testhelpers.TestFuture;
import hy.tmc.cli.testhelpers.builders.ExerciseBuilder;
import hy.tmc.core.TmcCore;
import hy.tmc.core.configuration.TmcSettings;
import hy.tmc.core.domain.Course;
import hy.tmc.core.domain.Exercise;
import hy.tmc.core.exceptions.TmcCoreException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UpdateSteps {

    private TestClient testClient;

    private WireMockServer wireMockServer;
    private TmcCli tmcCli;
    private TmcCore coreMock;
    private Exercise example1;
    private Exercise example2;

    private String output;

    /**
     * Setups client's config and starts WireMock.
     */
    @Before
    public void setUpServer() throws IOException, TmcCoreException {
        coreMock = mock(TmcCore.class);
        tmcCli = new TmcCli(coreMock);
        tmcCli.startServer();
        testClient = new TestClient(new ConfigHandler().readPort());
        List<Course> fake = new ArrayList<>();
        fake.add(new Course("course"));
        when(coreMock.listCourses(any(TmcSettings.class))).thenReturn(new TestFuture<>(fake));
        tmcCli.login("bossman", "samu");
        new ConfigHandler().writeLastUpdate(new Date());
        
        setupExercises();
    }
    
    private void setupExercises() {
        example1 = new ExerciseBuilder().withName("duck").build();
        example2 = new ExerciseBuilder().withName("two").build();
    }
    
    private TestFuture<List<Exercise>> fakeExercises(Exercise... exercises) {
        return new TestFuture<>(Arrays.asList(exercises));
    }

    @Given("^changed and new exercises$")
    public void changed_and_new_exercises() throws Throwable {
        when(coreMock.getNewAndUpdatedExercises(any(Course.class), any(TmcSettings.class)))
                .thenReturn(fakeExercises(example1, example2));
    }

    @When("^the user gives the update command$")
    public void the_user_gives_the_update_command() throws Throwable {
        testClient.sendMessage("update path /course/");
    }

    @Then("^the updates should be downloaded$")
    public void the_updates_should_be_downloaded() throws Throwable {
        String message = testClient.getAllFromSocket();
        System.out.println("----\n" + message + "\n------");
        
    }

    @Then("^the user should see how many updates were downloaded$")
    public void the_user_should_see_how_many_updates_were_downloaded() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @Given("^there are no updates$")
    public void there_are_no_updates() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @Then("^the output should say no updates were downloaded$")
    public void the_output_should_say_no_updates_were_downloaded() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @Then("^no downloads should have happened$")
    public void no_downloads_should_have_happened() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @Given("^the course cannot be determined$")
    public void the_course_cannot_be_determined() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @Then("^the user will see the error message \"(.*?)\"$")
    public void the_user_will_see_the_error_message(String arg1) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @Given("^the user has not logged in$")
    public void the_user_has_not_logged_in() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }
}
