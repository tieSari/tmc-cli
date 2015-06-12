package hy.tmc.cli.frontend.communication.server;

import com.google.common.util.concurrent.ListenableFuture;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ExecutionException;

public class SocketListener implements Runnable {

    private ListenableFuture<String> commandResult;
    private DataOutputStream output;
    private Socket socket;

    public SocketListener(ListenableFuture<String> commandResult, DataOutputStream output, Socket socket) {
        this.commandResult = commandResult;
        this.output = output;
        this.socket = socket;
    }
    
    @Override
    public void run() {
        try {
            final String commandOutput = commandResult.get();
            writeToOutput(commandOutput);
        }
        catch (InterruptedException | ExecutionException ex) {
            writeToOutput(ex.getCause().getMessage());
        }
    }

    private void writeToOutput(final String commandOutput) {
        try {
            output.writeUTF(commandOutput + "\n");
            socket.close();
        }
        catch (IOException ex) {
            System.err.println("Failed to print error message: ");
            System.err.println(ex.getMessage());
        }
    }
}
