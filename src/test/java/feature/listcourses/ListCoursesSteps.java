package feature.listcourses;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import hy.tmc.cli.Configuration.ClientData;
import hy.tmc.cli.frontend_communication.Server.Server;
import hy.tmc.cli.testhelpers.TestClient;
import java.io.IOException;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ListCoursesSteps {

    private final int port = ClientData.getPORT();

    private Thread serverThread;
    private Server server;
    private TestClient testClient;
    private boolean testThrown;

    @Before
    public void setUpServer() throws IOException {
        server = new Server(port, null);
        serverThread = new Thread(server);
        serverThread.start();
        testClient = new TestClient(port);
    }

    @Given("^user has logged in with username \"(.*?)\" and password \"(.*?)\"\\.$")
    public void user_has_logged_in_with_username_and_password(String username, String password) throws Throwable {
        testClient.sendMessage("login username " + username + " password " + password);
        testClient.init();

    }

    @When("^user gives command listCourses\\.$")
    public void user_gives_command_listCourses() throws Throwable {
        testClient.sendMessage("listCourses");

    }

    @Then("^output should contain more than one line$")
    public void output_should_contain_more_than_one_line() throws Throwable {
        String content = testClient.reply();
        String[] asd = content.split("\n");
        assertTrue(content.length() > 10);
        serverThread.interrupt();
    }

    @Given("^user has not logged in$")
    public void user_has_not_logged_in() throws Throwable {
        testClient = new TestClient(port);
    }

    @When("^user writes listCourses\\.$")
    public void user_writes_listCourses() throws Throwable {
        testThrown = false;
        try {
            testClient.sendMessage("listCourses");
        }
        catch (Exception e) {
            testThrown = true;
        }
    }

    @Then("^exception should be thrown$")
    public void exception_should_be_thrown() throws Throwable {
        assertFalse(testThrown);
        serverThread.interrupt();
    }

    @After
    public void closeServer() throws IOException {
        server.close();
        serverThread.interrupt();
    }

}
