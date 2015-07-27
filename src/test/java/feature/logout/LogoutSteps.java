package feature.logout;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import hy.tmc.cli.TmcCli;
import hy.tmc.cli.configuration.ClientData;
import hy.tmc.cli.configuration.ConfigHandler;
import hy.tmc.cli.frontend.communication.server.Server;
import hy.tmc.cli.testhelpers.TestClient;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import hy.tmc.core.TmcCore;
import org.hamcrest.CoreMatchers;
import org.junit.Rule;

import java.io.IOException;

public class LogoutSteps {

    private TestClient testClient;
    private TmcCli tmcCli;


    @Before
    public void initializeServer() throws IOException {
        tmcCli = new TmcCli(new TmcCore());
        tmcCli.startServer();
        testClient = new TestClient( new ConfigHandler().readPort());
    }

    @Given("^logout command\\.$")
    public void logout_command() throws Throwable {
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
     * @throws IOException if server closing fails
     */
    @After
    public void closeAll() throws IOException {
        tmcCli.stopServer();
    }
}
