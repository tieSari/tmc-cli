package feature.downloadexercises;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import cucumber.api.java.After;
import cucumber.api.java.Before;
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
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertTrue;
import org.junit.Rule;

public class DownloadExercisesSteps {

    private int port;
    private Thread serverThread;
    private Server server;
    private TestClient testClient;
    private Path tempDir;
    private ArrayList<String> output;
    private ConfigHandler config;
    private WireMockServer wireMockServer;

    @Before
    public void setUpServer() throws IOException {
        wireMockServer = new WireMockServer(wireMockConfig().port(8080));
        config = new ConfigHandler();
        config.writeServerAddress("http://127.0.0.1:8080");
        server = new Server(null);
        ClientData.setUserData("pihla", "juuh");
        port = config.readPort();
        System.out.println(port);
        serverThread = new Thread(server);
        output = new ArrayList<>();
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
                        .withBody(ExampleJSON.courseExample.replace("https://tmc.mooc.fi/staging", "http://127.0.0.1:8080"))));
        
        wireMockServer.stubFor(get(urlMatching("/exercises/[0-9]+.zip"))
                .withHeader("Authorization", equalTo("Basic cGlobGE6anV1aA=="))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "text/json")
                        .withBodyFile("test.zip")));
    }

    private void createTestClient() throws IOException {
        testClient = new TestClient(port);
    }

    @Given("^user has logged in with username \"(.*?)\" and password \"(.*?)\"\\.$")
    public void user_has_logged_in_with_username_and_password(String username, String password) throws Throwable {
        createTestClient();
        testClient.sendMessage("login username " + username + " password " + password);
        verify(getRequestedFor(urlEqualTo("/user")));
                //.withHeader("Authorization", equalTo("Basic cGlobGE6anV1aA==")));
    }

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
        wireMockServer.verify(getRequestedFor(urlEqualTo("/courses/21.json?api_version=7"))
                .withHeader("Authorization", equalTo("Basic cGlobGE6anV1aA==")));
        wireMockServer.verify(getRequestedFor(urlMatching("/exercises/[0-9]+.zip"))
                .withHeader("Authorization", equalTo("Basic cGlobGE6anV1aA==")));
    }

    @Then("^output should contain zip files and folders containing unzipped files$")
    public void output_should_contain_zip_files_and_folders_containing_unzipped_files() throws Throwable {
        assertTrue(new File(tempDir.toAbsolutePath() + "/viikko1").exists());
    }

    @Then("^information about download progress\\.$")
    public void information_about_download_progress() throws Throwable {
        System.out.println(output);
        assertEquals("Downloading exercise viikko1-Viikko1_000.Hiekkalaatikko 0.0%", output.get(0));
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
