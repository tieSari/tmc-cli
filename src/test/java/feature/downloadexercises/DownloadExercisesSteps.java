package feature.downloadexercises;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.urlMatching;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import com.github.tomakehurst.wiremock.WireMockServer;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import fi.helsinki.cs.tmc.core.communication.UrlHelper;
import fi.helsinki.cs.tmc.core.exceptions.TmcCoreException;

import hy.tmc.cli.CliSettings;
import hy.tmc.cli.TmcCli;
import hy.tmc.cli.configuration.ConfigHandler;
import hy.tmc.cli.testhelpers.ExampleJson;
import hy.tmc.cli.testhelpers.TestClient;

import org.hamcrest.CoreMatchers;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Date;


public class DownloadExercisesSteps {

    private static final String SERVER_URI = "127.0.0.1";
    private static final int SERVER_PORT = 8080;
    private static final String SERVER_ADDRESS = "http://" + SERVER_URI + ":" + SERVER_PORT;
    private TestClient testClient;
    private WireMockServer wireMockServer;
    private TmcCli tmcCli;
    private Path tempDir;
    private String output;
    private UrlHelper urlHelper;

    public DownloadExercisesSteps() {
        CliSettings settings = new CliSettings();
        urlHelper = new UrlHelper(settings);
    }

    /**
     * Setups client's config and starts WireMock.
     */
    @Before
    public void setUpServer() throws IOException, TmcCoreException, URISyntaxException {
        tmcCli = new TmcCli(false);
        tmcCli.setServer(SERVER_ADDRESS);
        Date date = new Date();
        tmcCli.startServer();
        new ConfigHandler().writeLastUpdate(date);
        testClient = new TestClient(new ConfigHandler().readPort());

        tempDir = Files.createTempDirectory(null);

        new ConfigHandler().writeLastUpdate(new Date());
        wiremock();
    }

    private void wiremock() throws URISyntaxException {
        wireMockServer = new WireMockServer();
        wireMockServer.start();
        wireMockServer.stubFor(
            get(urlEqualTo("/user")).withHeader("Authorization", equalTo("Basic cGlobGE6anV1aA=="))
                .willReturn(aResponse().withStatus(200)));

        URI urlMock = urlHelper.withParams(new URI("/courses.json"));
        wireMockServer.stubFor(get(urlEqualTo(urlMock.toString())).willReturn(
            aResponse().withStatus(200).withHeader("Content-Type", "text/json").withBody(
                ExampleJson.allCoursesExample
                    .replace("https://tmc.mooc.fi/staging", SERVER_ADDRESS))));

        urlMock = urlHelper.withParams(new URI("/courses/3.json"));
        wireMockServer.stubFor(get(urlEqualTo(urlMock.toString())).willReturn(
            aResponse().withStatus(200).withHeader("Content-Type", "text/json").withBody(
                ExampleJson.courseExample.replace("https://tmc.mooc.fi/staging", SERVER_ADDRESS))));

        urlMock = urlHelper.withParams(new URI("/courses/21.json"));
        wireMockServer.stubFor(get(urlEqualTo(urlMock.toString())).willReturn(
            aResponse().withStatus(200).withHeader("Content-Type", "text/json").withBody(
                ExampleJson.courseExample.replace("https://tmc.mooc.fi/staging", SERVER_ADDRESS)
                    .replaceFirst("3", "21"))));

        wireMockServer.stubFor(get(urlMatching("/exercises/[0-9]+.zip")).willReturn(
            aResponse().withStatus(200).withHeader("Content-Type", "text/json")
                .withBodyFile("test.zip")));
    }

    @Given("^user has logged in with username \"(.*?)\" and password \"(.*?)\"\\.$")
    public void user_has_logged_in_with_username_and_password(String username, String password)
        throws Throwable {
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
    public void user_gives_a_download_exercises_command_and_course_id_that_isnt_a_real_id()
        throws Throwable {
        testClient.sendMessage("downloadExercises courseID 9999 path " + tempDir.toAbsolutePath());
        output = testClient.getAllFromSocket();
    }

    @When("^user gives a download exercises command and course id with locked exercises\\.$")
    public void user_gives_a_download_exercises_command_and_course_id_with_locked_exercises()
        throws Throwable {
        wireMockServer.stubFor(get(urlEqualTo(urlHelper.withParams(new URI("/courses/21.json")).toString()))
            .withHeader("Authorization", equalTo("Basic cGlobGE6anV1aA==")).willReturn(
                aResponse().withStatus(200).withHeader("Content-Type", "text/json").withBody(
                    ExampleJson.courseExample.replace("https://tmc.mooc.fi/staging", SERVER_ADDRESS)
                        .replaceFirst("\"locked\": false", "\"locked\": true"))));

        testClient.sendMessage("downloadExercises courseID 21 path " + tempDir.toAbsolutePath());
        output = testClient.getAllFromSocket();
    }

    @When("^user gives a download exercises command and course name\\.$")
    public void user_gives_a_download_exercises_command_and_course_name() throws Throwable {
        testClient.sendMessage(
            "downloadExercises courseName 2013_ohpeJaOhja path " + tempDir.toAbsolutePath());
        output = testClient.getAllFromSocket();
    }

    @When("^user gives a download exercises command with a course name not on the server$")
    public void user_gives_a_download_exercises_command_with_a_course_name_not_on_the_server()
        throws Throwable {
        testClient.sendMessage(
            "downloadExercises courseName notacourse path " + tempDir.toAbsolutePath());
        output = testClient.getAllFromSocket();
    }

    @Then("^output should contain \"(.*?)\"\\.$")
    public void output_should_contain(String expectedOutput) throws Throwable {
        assertThat(output, CoreMatchers.containsString(expectedOutput));
    }

    @Then("^output should contain zip files and folders containing unzipped files$")
    public void output_should_contain_zip_files_and_folders_containing_unzipped_files()
        throws Throwable {
        assertTrue(new File(
            tempDir.toAbsolutePath() + File.separator + "2013_ohpeJaOhja" + File.separator
                + "viikko1").exists());
        String exerciseCount = "153";
        assertThat(output, CoreMatchers.containsString(exerciseCount));
    }

    @Then("^\\.zip -files are removed\\.$")
    public void zip_files_are_removed() throws Throwable {
        String filepath = tempDir.toAbsolutePath().toString();
        File[] paths = getFileArray(filepath);
        boolean zips = false;
        for (File path : paths) {
            if (path.getAbsolutePath().endsWith(".zip")) {
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
        return fi.listFiles();
    }

    /**
     * Close the server, so that the other tests will work.
     */
    @After
    public void closeServer() throws IOException, InterruptedException {
        tempDir.toFile().delete();
        wireMockServer.stop();
        tmcCli.stopServer();
        new File(new ConfigHandler().getConfigFilePath()).delete();
    }
}
