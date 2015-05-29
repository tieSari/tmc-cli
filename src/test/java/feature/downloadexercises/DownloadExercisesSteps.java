package feature.downloadexercises;


import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.configureFor;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.urlMatching;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;

import hy.tmc.cli.configuration.ClientData;
import hy.tmc.cli.configuration.ConfigHandler;
import hy.tmc.cli.frontend.communication.server.Server;
import hy.tmc.cli.testhelpers.ExampleJson;
import hy.tmc.cli.testhelpers.TestClient;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import java.io.File;
import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Path;

import java.util.ArrayList;

public class DownloadExercisesSteps {

    private int port;
    private Thread serverThread;
    private Server server;
    private TestClient testClient;
    private Path tempDir;
    private ArrayList<String> output;
    private ConfigHandler config;
    private WireMockServer wireMockServer;

    private static final String SERVER_URI = "127.0.0.1";
    private static final int SERVER_PORT = 5055;
    private static final String SERVER_ADDRESS = "http://" + SERVER_URI + ":" + SERVER_PORT;
    /**
     * Set up server.
     * @throws IOException if server initializing fails
     */
    @Before
    public void setUpServer() throws IOException {
        wireMockServer = new WireMockServer(wireMockConfig().port(SERVER_PORT));
        config = new ConfigHandler();
        config.writeServerAddress(SERVER_ADDRESS);
        server = new Server(null);
        ClientData.setUserData("pihla", "juuh");
        port = config.readPort();
        System.out.println(port);
        serverThread = new Thread(server);
        output = new ArrayList<>();
        configureFor(SERVER_URI, SERVER_PORT);
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
                        .withBody(ExampleJson.courseExample.replace("https://tmc.mooc.fi/staging", "http://127.0.0.1:5055"))));

        wireMockServer.stubFor(get(urlMatching("/exercises/[0-9]+.zip"))
                .withHeader("Authorization", equalTo("Basic cGlobGE6anV1aA=="))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "text/json")
                        .withBodyFile("test.zip")));
    }

    /**
     * Create test client.
     * @throws IOException if creating fails
     */
    private void createTestClient() throws IOException {
        testClient = new TestClient(port);
    }

    /**
     * Tests that user sends login request.
     * @param username string
     * @param password string
     * @throws Throwable if something fails
     */
    @Given("^user has logged in with username \"(.*?)\" and password \"(.*?)\"\\.$")
    public void user_has_logged_in(String username, String password) throws Throwable {
        createTestClient();
        testClient.sendMessage("login username " + username + " password " + password);

        // waiting for command to complete
        while (true) {
            String out = testClient.reply();
            if (!(out != null && !out.equals("fail"))) {
                break;
            }
        }

        verify(getRequestedFor(urlEqualTo("/user"))
                .withHeader("Authorization", equalTo("Basic cGlobGE6anV1aA==")));
    }

    /**
     * Verifies that user gives a download exercises command and course id.
     * @throws Throwable if test fails
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
     * Verifies that output contains zip files and folders contain unzipped files.
     * @throws Throwable if something fails
     */
    @Then("^output should contain zip files and folders containing unzipped files$")
    public void output_should_contain_zip_files_and_folders_containing_unzipped_files()
            throws Throwable {
        assertTrue(new File(tempDir.toAbsolutePath()
                + File.separator + "2013_ohpeJaOhja"
                + File.separator + "viikko1").exists());
    }

    /**
     * Verifies that downloading gives information about progress.
     * @throws Throwable if something fails
     */
    @Then("^information about download progress\\.$")
    public void information_about_download_progress()
            throws Throwable {
        System.out.println(output);
        assertEquals("Downloading exercise viikko1-Viikko1_000.Hiekkalaatikko 0.0%", output.get(0));
    }

    /**
     * Closes server after test.
     * @throws IOException if server operations fail
     */
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
    
    public File[] getFileArray(String filepath) {
        File fi = new File(filepath);
        File[] paths = fi.listFiles();
        return paths;
    }

    @After
    public void closeServer() throws IOException {
        tempDir.toFile().delete();
        WireMock.reset();
        wireMockServer.stop();
        server.close();
        serverThread.interrupt();
    }
}
