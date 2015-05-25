package feature.downloadexercises;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import hy.tmc.cli.Configuration.ConfigHandler;
import hy.tmc.cli.frontend_communication.Server.Server;
import hy.tmc.cli.testhelpers.TestClient;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class DownloadExercisesSteps {
    
    private int port;
    private Thread serverThread;
    private Server server;
    private TestClient testClient;
    private Path tempDir;
    private ArrayList<String> output;

    @Before
    public void setUpServer() throws IOException {
        server = new Server(null);
        port = new ConfigHandler().readPort();
        serverThread = new Thread(server);
        output = new ArrayList<>();
        serverThread.start();
        
        tempDir = Files.createTempDirectory(null);
    }

    private void createTestClient() throws IOException {
        testClient = new TestClient(port);
    }

    @Given("^user has logged in with username \"(.*?)\" and password \"(.*?)\"\\.$")
    public void user_has_logged_in_with_username_and_password(String username, String password) throws Throwable {
        createTestClient();
        testClient.sendMessage("login username " + username + " password " + password);
    }

    @When("^user gives a download exercises command and course id\\.$")
    public void user_gives_a_download_exercises_command_and_course_id() throws Throwable {
        createTestClient();
        testClient.sendMessage("downloadExercises courseID 21 pwd " + tempDir.toAbsolutePath() );
        while (true) {
            String out = testClient.reply();
            if (out != null && !out.equals("fail")) {
                output.add(out);
            } else {
                break;
            }
        }
    }

    @Then("^output should contain zip files and folders containing unzipped files$")
    public void output_should_contain_zip_files_and_folders_containing_unzipped_files() throws Throwable {      
        assertTrue(new File(tempDir.toAbsolutePath() + "/viikko01").exists());
        assertTrue(new File(tempDir.toAbsolutePath() + "/viikko02").exists());
        assertTrue(new File(tempDir.toAbsolutePath() + "/viikko03").exists());
    }

    @Then("^information about download progress\\.$")
    public void information_about_download_progress() throws Throwable {
        assertEquals("Downloading exercise viikko01-tira1.1 0.0%", output.get(0));
        assertEquals("Downloading exercise viikko02-tira2.4 53.0%", output.get(8));
        assertEquals("Downloading exercise viikko03-tira3.5 93.0%", output.get(14));
        assertEquals("15 exercises downloaded.", output.get(15));
    }
    
    @After
    public void closeServer() throws IOException {
        tempDir.toFile().delete();
        server.close();
        serverThread.interrupt();
    }
}
