package hy.tmc.cli.frontend;

import hy.tmc.core.domain.ProgressObserver;

import java.io.DataOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public class CommandLineProgressObserver implements ProgressObserver {

    private DataOutputStream output;

    public CommandLineProgressObserver(DataOutputStream output) {
        this.output = output;
    }

    @Override
    public void progress(String message) {
        try {
            output.write(message.getBytes());
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    public void progress(Double completionPercentage, String progressMessage) {
            NumberFormat formatter = new DecimalFormat("#0.0");
            String percentage = formatter.format(completionPercentage);
            String message = progressMessage + " (" + percentage + "% done)\n";
            this.progress(message);
    }
}
