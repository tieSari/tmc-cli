package feature.setServer;

import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import hy.tmc.cli.configuration.ConfigHandler;
import hy.tmc.cli.frontend.communication.commands.ChooseServer;
import hy.tmc.cli.frontend.communication.server.ProtocolException;

import hy.tmc.cli.testhelpers.FrontendStub;
import java.io.IOException;
import static org.junit.Assert.assertEquals;

public class SetServerSteps {
    
    private ConfigHandler handler;
    private FrontendStub front;
    private ChooseServer command;
    private String origServer;
    
    @Before
    public void setup() {
        handler = new ConfigHandler("testResources/test.properties");
        front = new FrontendStub();
        command = new ChooseServer(handler, front);
    }

    @Given ("^the server is \"(.*)\"$")
    public void stagingServerSelected(String server) throws IOException{
        origServer = server;
        handler.writeServerAddress(origServer);
    }
    
    @When ("^the user changes the server to \"(.*)\"$")
    public void serverChanged(String serverName) {
        try {
            command.setParameter("tmc-server", serverName);
            command.execute();
        }
        catch (ProtocolException ex) {
        }
    }
    
    @When ("^the user uses the command without parameters$")
    public void noParamsGiven(){
        try {
            command.execute();
        }
        catch (ProtocolException ex) {
        }
    }
    
    @Then ("^the server will be \"(.*)\"$")
    public void correctChanges(String server) throws IOException {
        String addressInConf = handler.readServerAddress();
        assertEquals(server, addressInConf);
    }
    
    @Then ("^the server is unchanged$")
    public void noChanges() throws IOException{
        String addressInConf = handler.readServerAddress();
        assertEquals(origServer, addressInConf);
    }
}
