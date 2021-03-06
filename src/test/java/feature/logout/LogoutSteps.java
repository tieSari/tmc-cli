package feature.logout;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import fi.helsinki.cs.tmc.core.TmcCore;

import fi.helsinki.cs.tmc.core.exceptions.TmcCoreException;
import hy.tmc.cli.TmcCli;
import hy.tmc.cli.configuration.ConfigHandler;
import hy.tmc.cli.testhelpers.TestClient;

import org.hamcrest.CoreMatchers;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;

import static org.mockito.Mockito.mock;

public class LogoutSteps {

    private TestClient testClient;
    private TmcCli tmcCli;

    @Before
    public void initializeServer() throws IOException, TmcCoreException {
        tmcCli = new TmcCli(false);
        tmcCli.startServer();
        testClient = new TestClient(new ConfigHandler().readPort());

        new ConfigHandler().writeLastUpdate(new Date());
    }

    @After
    public void clean() throws IOException {
        new File(new ConfigHandler().getConfigFilePath()).delete();
    }

    @Given("^logout command\\.$")
    public void logout_command() throws Throwable {
        tmcCli.setServer("hello world");
        tmcCli.login("Samu", "Samuonparas3443434334");
        testClient.sendMessage("logout");
    }

    @When("^user sees message \"(.*?)\"$")
    public void user_sees_message(String expectedOutput) throws Throwable {
        String output = testClient.reply();
        assertThat(output, CoreMatchers.containsString(expectedOutput));
    }

    @Then("^user data should be cleared\\.$")
    public void user_data_should_be_cleared() throws Throwable {
        assertFalse(tmcCli.defaultSettings().userDataExists());
    }

    /**
     * Clear ClientData and close server.
     *
     * @throws IOException if server closing fails
     */
    @After
    public void closeAll() throws IOException {
        tmcCli.stopServer();
    }
}
