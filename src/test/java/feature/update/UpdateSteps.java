package feature.update;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import fi.helsinki.cs.tmc.core.TmcCore;
import fi.helsinki.cs.tmc.core.configuration.TmcSettings;
import fi.helsinki.cs.tmc.core.domain.Course;
import fi.helsinki.cs.tmc.core.domain.Exercise;
import fi.helsinki.cs.tmc.core.domain.ProgressObserver;
import fi.helsinki.cs.tmc.core.exceptions.TmcCoreException;

import hy.tmc.cli.TmcCli;
import hy.tmc.cli.configuration.ConfigHandler;
import hy.tmc.cli.testhelpers.TestClient;
import hy.tmc.cli.testhelpers.builders.ExerciseBuilder;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import javax.swing.text.DateFormatter;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.hamcrest.CoreMatchers.containsString;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThat;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UpdateSteps {

    private static final String SERVER_URI = "127.0.0.1";
    private static final int SERVER_PORT = 8080;
    private static final String SERVER_ADDRESS = "http://" + SERVER_URI + ":" + SERVER_PORT;
    private TestClient testClient;
    private WireMockServer wireMockServer;
    private TmcCli tmcCli;
    private TmcCore coreMock;
    private Exercise example1;
    private Exercise example2;
    private String output;
    private long timeAtStart;

    /**
     * Setups client's config and starts WireMock.
     */
    @Before
    public void setUpServer() throws IOException, TmcCoreException {
        coreMock = mock(TmcCore.class);
        tmcCli = new TmcCli(coreMock, true);
        tmcCli.startServer();
        int a = new ConfigHandler().readPort();
        System.out.println(a);
        testClient = new TestClient(a);

        List<Course> fake = new ArrayList<>();
        fake.add(new Course("course"));
        when(coreMock.listCourses()).thenReturn(Futures.immediateFuture(fake));
        tmcCli.login("bossman", "samu");
        tmcCli.setServer(SERVER_ADDRESS);
        new ConfigHandler().writeLastUpdate(new Date());
        new ConfigHandler().writeServerAddress(SERVER_ADDRESS);

        setupExercises();

        mockDownload();
    }

    @After
    public void clean() throws IOException {
        new File(new ConfigHandler().getConfigFilePath()).delete();
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
        when(coreMock.getNewAndUpdatedExercises(any(Course.class)))
            .thenReturn(fakeExercises(example1, example2));
        timeAtStart = System.currentTimeMillis();
    }

    @When("^the user gives the update command$")
    public void the_user_gives_the_update_command()
        throws Throwable {
        testClient.sendMessage("update path /course/");
    }

    @Then("^the updates should be downloaded$")
    public void the_updates_should_be_downloaded()
        throws Throwable {
        verify(coreMock).downloadExercises(any(List.class), any(ProgressObserver.class));
    }

    @Then("^the last update time should be the current time$")
    public void the_last_update_time_should_be_the_current_time() throws Throwable {
        new DateFormatter(new SimpleDateFormat("dd-mm-yyyy hh:mm:ss"));
        Date d = new ConfigHandler().readLastUpdate();
        assertNotEquals(timeAtStart, d.getTime());
    }

    @Then("^the user should see how many updates were downloaded$")
    public void the_user_should_see_how_many_updates_were_downloaded() throws Throwable {
        String messages = testClient.getAllFromSocket();
        String line1 = "update information received, starting download";
        String line2 = "2 updates downloaded";
        assertThat(messages, containsString(line1));
        assertThat(messages, containsString(line2));
    }

    @Given("^there are no updates$")
    public void there_are_no_updates() throws Throwable {
        when(coreMock.getNewAndUpdatedExercises(any(Course.class)))
            .thenReturn(fakeExercises());
    }

    @Then("^the output should say no updates were downloaded$")
    public void the_output_should_say_no_updates_were_downloaded() throws Throwable {
        String messages = testClient.getAllFromSocket();
        String line = "0 updates downloaded";
        assertThat(messages, containsString(line));
    }

    @Then("^no downloads should have happened$")
    public void no_downloads_should_have_happened()
        throws Throwable {
        verify(coreMock, times(0)).downloadExercises(any(List.class));
        verify(coreMock, times(0)).downloadExercises(any(List.class),
            any(ProgressObserver.class));
        verify(coreMock, times(0))
            .downloadExercises(anyString(), anyString(),
                any(ProgressObserver.class));
    }

    @Given("^the course cannot be determined$")
    public void the_course_cannot_be_determined()
        throws Throwable {
        List<Course> courses = new ArrayList<>();
        courses.add(new Course("alksdfj"));
        when(coreMock.listCourses())
            .thenReturn(Futures.immediateFuture(courses));
    }

    @Then("^the user will see the error message \"(.*?)\"$")
    public void the_user_will_see_the_error_message(String expectedErrorMessage) throws Throwable {
        String error = testClient.getAllFromSocket();
        assertThat(error, containsString(expectedErrorMessage));
    }

    @Given("^the user has not logged in$")
    public void the_user_has_not_logged_in()
        throws Throwable {
        tmcCli.logout();
    }

    private void mockDownload() throws TmcCoreException {
        when(coreMock.downloadExercises(any(List.class),
            any(ProgressObserver.class)))
            .thenAnswer(new Answer<ListenableFuture<List<Exercise>>>() {
                @Override
                public ListenableFuture<List<Exercise>> answer(InvocationOnMock invocation)
                    throws Throwable {
                    Object[] args = invocation.getArguments();
                    return Futures.immediateFuture((List<Exercise>) args[0]);
                }
            });
    }
}
