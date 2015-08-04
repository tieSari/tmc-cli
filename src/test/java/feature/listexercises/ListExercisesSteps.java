package feature.listexercises;

import com.github.tomakehurst.wiremock.WireMockServer;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import hy.tmc.cli.TmcCli;
import hy.tmc.cli.mail.Mailbox;
import hy.tmc.cli.configuration.ConfigHandler;
import hy.tmc.cli.synchronization.TmcServiceScheduler;
import hy.tmc.cli.testhelpers.ExampleJson;
import hy.tmc.cli.testhelpers.MailExample;
import hy.tmc.cli.testhelpers.TestClient;
import hy.tmc.core.TmcCore;
import org.hamcrest.CoreMatchers;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Date;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class ListExercisesSteps {

    private TmcCli tmcCli;

    private TestClient testClient;

    private ConfigHandler configHandler; // writes the test address
    private WireMockServer wireMockServer;

    private static final String SERVER_URI = "127.0.0.1";
    private static final int SERVER_PORT = 8080;
    private static final String SERVER_ADDRESS = "http://" + SERVER_URI + ":" + SERVER_PORT;
    private final String coursesExtension = "/courses.json?api_version=7";

    /**
     * Setups client's config and starts WireMock.
     */
    @Before
    public void setUpServer() throws IOException {
        TmcServiceScheduler.disablePolling();
        tmcCli = new TmcCli(new TmcCore(), false);
        tmcCli.setServer(SERVER_ADDRESS);
        tmcCli.startServer();

        testClient = new TestClient(new ConfigHandler().readPort());
        
        new ConfigHandler().writeLastUpdate(new Date());
        startWireMock();
    }

    @After
    public void closeServer() throws IOException {
        TmcServiceScheduler.enablePolling();
        tmcCli.stopServer();
        wireMockServer.stop();
        tmcCli.setServer("https://tmc.mooc.fi/staging");
    }

    private void startWireMock() {
        wireMockServer = new WireMockServer(wireMockConfig().port(SERVER_PORT));
        wireMockServer.start();
        wireMockServer.stubFor(get(urlEqualTo("/user"))
                .willReturn(
                        aResponse()
                        .withStatus(200)
                )
        );
        wireMockServer.stubFor(get(urlEqualTo(coursesExtension))
                        .willReturn(
                                aResponse()
                                        .withStatus(200)
                                        .withHeader("Content-Type", "application/json")
                                        .withBody(ExampleJson.allCoursesExample)
                        )
        );
        wireMockServer.stubFor(get(urlEqualTo("/courses/3.json?api_version=7"))
                .willReturn(
                        aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(ExampleJson.courseExample)
                )
        );

    }

    @Given("^user has logged in with username \"(.*?)\" and password \"(.*?)\"\\.$")
    public void user_has_logged_in_with_username_and_password(String username, String password) throws Throwable {
        testClient.sendMessage("login username " + username + " password " + password);
        testClient.getAllFromSocket();
        testClient.init();
    }

    @When("^user gives command listExercises with path \"(.*?)\"\\.$")
    public void user_gives_command_listExercises_with_path(String path) throws Throwable {
        path = Paths.get(path).toString();
        testClient.init();
        testClient.sendMessage("listExercises path " + path);
    }

    @Then("^output should contain \"(.*?)\"$")
    public void output_should_contain(String expectedOutput) throws Throwable {
        String result = testClient.getAllFromSocket();
        assertTrue(result.contains(expectedOutput));
    }

    @Given("^user has not logged in$")
    public void user_has_not_logged_in() throws Throwable {
    }

    @Given("^the user has mail in the mailbox$")
    public void the_user_has_mail_in_the_mailbox() throws Throwable {
        Mailbox.getMailbox().get().fill(MailExample.reviewExample());
    }

    @Then("^user will see the new mail$")
    public void user_will_see_the_new_mail() throws Throwable {
        String fullReply = testClient.getAllFromSocket();
        assertThat(fullReply, CoreMatchers.containsString("There are 3 unread code reviews"));
        assertThat(fullReply, CoreMatchers.containsString("rainfall reviewed by Bossman Samu"));
        assertThat(fullReply, CoreMatchers.containsString("Keep up the good work."));
        assertThat(fullReply, CoreMatchers.containsString("good code"));
    }

    @Given("^polling for reviews is not in progress$")
    public void polling_for_reviews_is_not_in_progress() throws Throwable {
        TmcServiceScheduler.enablePolling();
        assertFalse(TmcServiceScheduler.isRunning());
    }

    @Then("^the polling will be started$")
    public void the_polling_will_be_started() throws Throwable {
        testClient.getAllFromSocket();
        assertTrue(TmcServiceScheduler.isRunning());
    }
}
