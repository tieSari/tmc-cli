package feature.downloadexercises;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.urlMatching;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import hy.tmc.cli.configuration.ClientData;
import hy.tmc.cli.configuration.ConfigHandler;
import hy.tmc.cli.frontend.communication.server.Server;
import hy.tmc.cli.testhelpers.ExampleJSON;
import hy.tmc.cli.testhelpers.TestClient;

import java.io.File;
import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Path;

import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;

public class DownloadExercisesSteps {

    private int port;
    private Thread serverThread;
    private Server server;
    private TestClient testClient;
    private Path tempDir;
    private ArrayList<String> output;
    private ConfigHandler config;
    private WireMockServer wireMockServer;

    /**
     * Set up server and stubs.
     * @throws IOException if server initializing fail
     */
    @Before
    public void setUpServer() throws IOException {
        wireMockServer = new WireMockServer(wireMockConfig().port(5055));
        config = new ConfigHandler();
        config.writeServerAddress("http://127.0.0.1:5055");
        server = new Server(null);
        ClientData.setUserData("pihla", "juuh");
        port = config.readPort();
        System.out.println(port);
        serverThread = new Thread(server);
        output = new ArrayList<>();
        WireMock.configureFor("localhost", 5055);
        wireMockServer.start();
        serverThread.start();

        tempDir = Files.createTempDirectory(null);

        wireMockServer.stubFor(get(urlEqualTo("/user"))
                .withHeader("Authorization", equalTo("Basic cGlobGE6anV1aA=="))
                .willReturn(aResponse()
                        .withStatus(200)));
        
        wireMockServer.stubFor(get(urlEqualTo("/courses/21.json?api_version=7"))
                .withHeader("Authorization", equalTo("Basic cGlobGE6anV1aA=="))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "text/json")
                        .withBody(ExampleJSON.courseExample.replace("https://tmc.mooc.fi/staging", "http://127.0.0.1:5055"))));
        
        wireMockServer.stubFor(get(urlMatching("/exercises/[0-9]+.zip"))
                .withHeader("Authorization", equalTo("Basic cGlobGE6anV1aA=="))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "text/json")
                        .withBodyFile("test.zip")));
    }

    /**
     * Create test client with port.
     * @throws IOException from test client
     */
    private void createTestClient() throws IOException {
        testClient = new TestClient(port);
    }

    /**
     * Verifies that user has send login request.
     * @param username string
     * @param password string 
     * @throws Throwable 
     */
    @Given("^user has logged in with username \"(.*?)\" and password \"(.*?)\"\\.$")
    public void user_has_logged_in_with_username_and_password(String username, String password) throws Throwable {
        createTestClient();
        testClient.sendMessage("login username " + username + " password " + password);
        verify(getRequestedFor(urlEqualTo("/user")));
    }

    /**
     * Test client sends download message and test verifies that request is sent.
     * @throws Throwable if test client initializing fail
     */
    @When("^user gives a download exercises command and course id\\.$")
    public void user_gives_a_download_exercises_command_and_course_id() throws Throwable {
        createTestClient();
        testClient.sendMessage("downloadExercises courseID 21 pwd " + tempDir.toAbsolutePath());
        while (true) {
            String out = testClient.reply();
            if (out != null && !out.equals("fail")) {
                output.add(out);
            } else {
                break;
            }
        }
        verify(getRequestedFor(urlEqualTo("/courses/21.json?api_version=7"))
                .withHeader("Authorization", equalTo("Basic cGlobGE6anV1aA==")));
        verify(getRequestedFor(urlMatching("/exercises/[0-9]+.zip"))
                .withHeader("Authorization", equalTo("Basic cGlobGE6anV1aA==")));
    }

    /**
     * Tests that output contains correct zip files.
     * @throws Throwable 
     */
    @Then("^output should contain zip files and folders containing unzipped files$")
    public void output_should_contain_zip_files_and_folders_containing_unzipped_files() throws Throwable {
        assertTrue(new File(tempDir.toAbsolutePath() + File.separator + "/viikko1").exists());
    }

    /**
     * Checks that progress information is given for user.
     * @throws Throwable 
     */
    @Then("^information about download progress\\.$")
    public void information_about_download_progress() throws Throwable {
        System.out.println(output);
        assertEquals("Downloading exercise viikko1-Viikko1_000.Hiekkalaatikko 0.0%", output.get(0));
    }

    /**
     * Closes server after test.
     * @throws IOException 
     */
    @After
    public void closeServer() throws IOException {
        tempDir.toFile().delete();
        WireMock.reset();
        wireMockServer.stop();
        server.close();
        serverThread.interrupt();
    }
}
