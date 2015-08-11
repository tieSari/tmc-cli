package feature.setserver;

import static org.junit.Assert.assertThat;
import hy.tmc.cli.TmcCli;
import hy.tmc.cli.configuration.ConfigHandler;

import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import hy.tmc.cli.testhelpers.TestClient;
import fi.helsinki.cs.tmc.core.TmcCore;
import org.hamcrest.CoreMatchers;

import java.io.IOException;
import java.util.Date;

public class SetServerSteps {

    private TestClient testClient;
    private TmcCli tmcCli;

    @Before
    public void setUp() throws IOException {
        tmcCli = new TmcCli(new TmcCore(), false);
        tmcCli.startServer();
        testClient = new TestClient( new ConfigHandler().readPort());
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
    public void the_server_will_be(String expectedOutput) throws Throwable {
        String reply = testClient.reply();
        assertThat(reply, CoreMatchers.containsString(expectedOutput));
    }

}
