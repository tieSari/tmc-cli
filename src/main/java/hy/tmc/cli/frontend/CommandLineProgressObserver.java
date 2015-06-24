package hy.tmc.cli.frontend;

import java.io.DataOutputStream;
import java.io.IOException;


public class CommandLineProgressObserver implements ProgressObserver{

    private DataOutputStream output;
    
    public CommandLineProgressObserver(DataOutputStream output) {
        this.output = output;
    }
    
    @Override
    public void progress(double completionPercentage, String message) {
        try {
            output.write((message + completionPercentage + "%").getBytes());
            output.flush();
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
    }

}
