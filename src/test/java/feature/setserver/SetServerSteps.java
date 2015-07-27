package feature.setserver;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import cucumber.api.PendingException;
import hy.tmc.cli.TmcCli;
import hy.tmc.cli.configuration.ConfigHandler;
import hy.tmc.cli.frontend.communication.commands.SetServer;
import hy.tmc.cli.frontend.communication.server.ProtocolException;

import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import hy.tmc.cli.testhelpers.TestClient;
import hy.tmc.core.TmcCore;

import java.io.IOException;

public class SetServerSteps {

    private TestClient testClient;
    private TmcCli tmcCli;


    @Before
    public void setUp() throws IOException {
        tmcCli = new TmcCli(new TmcCore());
        tmcCli.startServer();
        testClient = new TestClient( new ConfigHandler().readPort());
    }

    @Given("^the server is \"(.*?)\"$")
    public void the_server_is(String address) throws Throwable {
        
    }

    @When("^the user changes the server to \"(.*?)\"$")
    public void the_user_changes_the_server_to(String arg1) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @Then("^the server will be \"(.*?)\"$")
    public void the_server_will_be(String arg1) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @Then("^the server is unchanged$")
    public void the_server_is_unchanged() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

}
