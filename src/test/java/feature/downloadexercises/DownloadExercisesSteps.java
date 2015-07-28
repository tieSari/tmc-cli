package feature.downloadexercises;

import com.github.tomakehurst.wiremock.WireMockServer;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import hy.tmc.cli.TmcCli;
import hy.tmc.cli.configuration.ConfigHandler;
import hy.tmc.cli.mail.Mailbox;
import hy.tmc.cli.testhelpers.ExampleJson;
import hy.tmc.cli.testhelpers.TestClient;
import hy.tmc.core.TmcCore;
import org.hamcrest.CoreMatchers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.Assert.*;

public class DownloadExercisesSteps {

    private TestClient testClient;

    private WireMockServer wireMockServer;
    private TmcCli tmcCli;
    private Path tempDir;

    private String output;

    private static final String SERVER_URI = "127.0.0.1";
    private static final int SERVER_PORT = 8080;
    private static final String SERVER_ADDRESS = "http://" + SERVER_URI + ":" + SERVER_PORT;

    /**
     * Setups client's config and starts WireMock.
     */
    @Before
    public void setUpServer() throws IOException {
        tmcCli = new TmcCli(new TmcCore());
        tmcCli.setServer(SERVER_ADDRESS);
        tmcCli.startServer();
        testClient = new TestClient(new ConfigHandler().readPort());

        tempDir = Files.createTempDirectory(null);

        wiremock();
    }

    private void wiremock() {
        wireMockServer = new WireMockServer();
        wireMockServer.start();
        wireMockServer.stubFor(get(urlEqualTo("/user"))
                .withHeader("Authorization", equalTo("Basic cGlobGE6anV1aA=="))
                .willReturn(aResponse()
                        .withStatus(200)));

        wireMockServer.stubFor(get(urlEqualTo("/courses.json?api_version=7"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "text/json")
                        .withBody(ExampleJson.allCoursesExample
                                .replace("https://tmc.mooc.fi/staging", SERVER_ADDRESS))));

        wireMockServer.stubFor(get(urlEqualTo("/courses/21.json?api_version=7"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "text/json")
                        .withBody(ExampleJson.courseExample
                                .replace("https://tmc.mooc.fi/staging", SERVER_ADDRESS)
                                .replaceFirst("3", "21"))));

        wireMockServer.stubFor(get(urlMatching("/exercises/[0-9]+.zip"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "text/json")
                        .withBodyFile("test.zip")));
    }

    @Given("^user has logged in with username \"(.*?)\" and password \"(.*?)\"\\.$")
    public void user_has_logged_in_with_username_and_password(String username, String password) throws Throwable {
        testClient.sendMessage("login username " + username + " password " + password);
        testClient.getAllFromSocket();
        testClient.init();
    }

    @When("^user gives a download exercises command and course id\\.$")
    public void user_gives_a_download_exercises_command_and_course_id() throws Throwable {
        testClient.sendMessage("downloadExercises courseID 21 path " + tempDir.toAbsolutePath());
        output = testClient.getAllFromSocket();
    }

    @When("^user gives a download exercises command and course id that isnt a real id\\.$")
    public void user_gives_a_download_exercises_command_and_course_id_that_isnt_a_real_id() throws Throwable {
        testClient.sendMessage("downloadExercises courseID 9999 path " + tempDir.toAbsolutePath());
        output = testClient.getAllFromSocket();
    }

    @When("^user gives a download exercises command and course id with locked exercises\\.$")
    public void user_gives_a_download_exercises_command_and_course_id_with_locked_exercises() throws Throwable {
                wireMockServer.stubFor(get(urlEqualTo("/courses/21.json?api_version=7"))
                .withHeader("Authorization", equalTo("Basic cGlobGE6anV1aA=="))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "text/json")
                        .withBody(ExampleJson.courseExample
                                .replace("https://tmc.mooc.fi/staging", SERVER_ADDRESS)
                                .replaceFirst("\"locked\": false", "\"locked\": true"))));

        testClient.sendMessage("downloadExercises courseID 21 path " + tempDir.toAbsolutePath());
        output = testClient.getAllFromSocket();
    }

    @Then("^output should contain \"(.*?)\"\\.$")
    public void output_should_contain(String expectedOutput) throws Throwable {
        assertThat(output, CoreMatchers.containsString(expectedOutput));
    }

    @Then("^output should contain zip files and folders containing unzipped files$")
    public void output_should_contain_zip_files_and_folders_containing_unzipped_files() throws Throwable {
        assertTrue(new File(tempDir.toAbsolutePath()
                + File.separator + "2013_ohpeJaOhja"
                + File.separator + "viikko1").exists());
        String exerciseCount = "153";
        assertThat(output, CoreMatchers.containsString(exerciseCount));
    }

    @Then("^\\.zip -files are removed\\.$")
    public void zip_files_are_removed() throws Throwable {
        String filepath = tempDir.toAbsolutePath().toString();
        File[] paths = getFileArray(filepath);
        boolean zips = false;
        for (File path : paths) {
            if (path.getAbsolutePath().toString().endsWith(".zip")) {
                zips = true;
            }
        }
        assertFalse(zips);
    }

    /**
     * Get the files under the directory specified
     *
     * @param filepath the directory
     */
    public File[] getFileArray(String filepath) {
        File fi = new File(filepath);
        File[] paths = fi.listFiles();
        return paths;
    }

    /**
     * Close the server, so that the other tests will work.
     */
    @After
    public void closeServer() throws IOException, InterruptedException {
        Mailbox.destroy();
        tempDir.toFile().delete();
        wireMockServer.stop();
        tmcCli.stopServer();
    }
}
