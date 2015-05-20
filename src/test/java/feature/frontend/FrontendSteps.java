package feature.frontend;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import hy.tmc.cli.testhelpers.Helper;
import hy.tmc.cli.testhelpers.buildhelpers.JarBuilder;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import static org.junit.Assert.assertTrue;

public class FrontendSteps {

    private final String port = "1234"; // change if necessary
    
    private Process jarP;
    private final Helper helper = new Helper();

    @Given("^a help command\\.$")
    public void a_help_command() throws Throwable {
        
        assertTrue(new JarBuilder().jarExists("scripts/tmc-client.jar")); // builds test-jar
        jarP = helper.createAndStartProcess("java", "-jar", "scripts/tmc-client.jar");
        
    }

    @Then("^output should contains commands\\.$")
    public void output_should_contains_commands() throws Throwable {
        
        Process nc = helper.createAndStartProcess("nc", "localhost", port);
        nc = helper.writeInputToProcess(nc, "help");
        String contents = helper.readOutputFromProcess(nc);
        
        nc.destroy();
        jarP.destroy();
        assertTrue(contents.contains("help"));
        
    }
}
