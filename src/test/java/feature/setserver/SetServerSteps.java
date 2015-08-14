package feature.setserver;

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

import static org.junit.Assert.assertThat;

public class SetServerSteps {

    private TestClient testClient;
    private TmcCli tmcCli;

    @Before
    public void setUp() throws IOException, TmcCoreException {
        tmcCli = new TmcCli(false);
        tmcCli.startServer();
        testClient = new TestClient(new ConfigHandler().readPort());
        new ConfigHandler().writeLastUpdate(new Date());

    }

    @Given("^the server is \"(.*?)\"$")
    public void the_server_is(String address) throws Throwable {
        tmcCli.setServer(address);
    }

    @When("^the user changes the server to \"(.*?)\"$")
    public void the_user_changes_the_server_to(String newAddress) throws Throwable {
        testClient.sendMessage("setServer tmc-server " + newAddress);
    }

    @Then("^the server will be \"(.*?)\"$")
    public void the_server_will_be(String expectedOutput)
        throws Throwable {
        String reply = testClient.reply();
        assertThat(reply, CoreMatchers.containsString(expectedOutput));
    }

    @After
    public void clean() throws IOException {
        new File(new ConfigHandler().getConfigFilePath()).delete();
    }

}
