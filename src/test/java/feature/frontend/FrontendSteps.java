package feature.frontend;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import hy.tmc.cli.testhelpers.Helper;
import static org.junit.Assert.assertTrue;

public class FrontendSteps {
    
    @Given(".+user writes help '(.+)'")
    public void userCanSeeAvailableCommands() {
        Helper helper = new Helper();
        String output = helper.printOutput("help", "scripts/frontend.sh");
    }
    @Then(".+ All commands are listed.")
    public void commandAreListed(final String output) {
        assertTrue(output.contains("Commands: "));
    }
}
