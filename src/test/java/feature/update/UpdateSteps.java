package feature.update;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import cucumber.api.PendingException;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import hy.tmc.cli.TmcCli;
import hy.tmc.cli.configuration.ConfigHandler;
import hy.tmc.cli.testhelpers.TestClient;
import hy.tmc.cli.testhelpers.builders.ExerciseBuilder;
import hy.tmc.core.TmcCore;
import hy.tmc.core.configuration.TmcSettings;
import hy.tmc.core.domain.Course;
import hy.tmc.core.domain.Exercise;
import hy.tmc.core.domain.ProgressObserver;
import hy.tmc.core.exceptions.TmcCoreException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import static org.junit.Assert.assertEquals;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

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
        when(coreMock.listCourses(any(TmcSettings.class))).thenReturn(Futures.immediateFuture(fake));
        tmcCli.login("bossman", "samu");
        new ConfigHandler().writeLastUpdate(new Date());
        
        setupExercises();

        mockDownload();
    }

    private void setupExercises() {
        example1 = new ExerciseBuilder().withName("duck").build();
        example2 = new ExerciseBuilder().withName("two").build();
    }

    private ListenableFuture<List<Exercise>> fakeExercises(Exercise... exercises) {
        return Futures.immediateFuture(Arrays.asList(exercises));
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
        verify(coreMock).downloadExercises(any(List.class), any(TmcSettings.class), any(ProgressObserver.class));
    }

    @Then("^the user should see how many updates were downloaded$")
    public void the_user_should_see_how_many_updates_were_downloaded() throws Throwable {
        String[] messages = testClient.getAllFromSocket().split("\n");
        String line1 = "Starting command update";
        String line2 = "update information received, starting download";
        String line3 = "2 updates downloaded";
        assertEquals(line1, messages[0]);
        assertEquals(line2, messages[1]);
        assertEquals(line3, messages[2]);
    }

    @Given("^there are no updates$")
    public void there_are_no_updates() throws Throwable {
        when(coreMock.getNewAndUpdatedExercises(any(Course.class), any(TmcSettings.class)))
                .thenReturn(fakeExercises());
    }

    @Then("^the output should say no updates were downloaded$")
    public void the_output_should_say_no_updates_were_downloaded() throws Throwable {
        String[] messages = testClient.getAllFromSocket().split("\n");
        String line1 = "Starting command update";
        String line2 = "0 updates downloaded";
        assertEquals(line1, messages[0]);
        assertEquals(line2, messages[1]);
    }

    @Then("^no downloads should have happened$")
    public void no_downloads_should_have_happened() throws Throwable {
        verify(coreMock, times(0)).downloadExercises(any(List.class), any(TmcSettings.class));
        verify(coreMock, times(0)).downloadExercises(any(List.class), any(TmcSettings.class), any(ProgressObserver.class));
        verify(coreMock, times(0)).downloadExercises(anyString(), anyString(), any(TmcSettings.class), any(ProgressObserver.class));
    }

    @Given("^the course cannot be determined$")
    public void the_course_cannot_be_determined() throws Throwable {
        List<Course> courses = new ArrayList<>();
        courses.add(new Course("alksdfj"));
        when(coreMock.listCourses(any(TmcSettings.class))).thenReturn(Futures.immediateFuture(courses));
    }

    @Then("^the user will see the error message \"(.*?)\"$")
    public void the_user_will_see_the_error_message(String expectedErrorMessage) throws Throwable {
        testClient.reply();
        String error = testClient.reply();
        assertEquals(expectedErrorMessage, error);
    }

    @Given("^the user has not logged in$")
    public void the_user_has_not_logged_in() throws Throwable {
        tmcCli.logout();
    }

    private void mockDownload() throws TmcCoreException {
        when(coreMock.downloadExercises(
                any(List.class), any(TmcSettings.class), any(ProgressObserver.class)
        )).thenAnswer(new Answer<ListenableFuture<List<Exercise>>>() {
            @Override
            public ListenableFuture<List<Exercise>> answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArguments();
                return Futures.immediateFuture((List<Exercise>) args[0]);
            }
        });
    }
}
