package hy.tmc.cli.frontend.communication.server;

import com.google.common.base.Optional;
import com.google.common.util.concurrent.ListenableFuture;
import hy.tmc.cli.frontend.communication.commands.Command;
import hy.tmc.cli.frontend.communication.commands.Submit;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ExecutionException;

public class SocketListener implements Runnable {

    private ListenableFuture<?> commandResult;
    private DataOutputStream output;
    private Socket socket;
    private Command command;

    public SocketListener(ListenableFuture<?> commandResult, DataOutputStream output, Socket socket, Command command) {
        this.commandResult = commandResult;
        this.output = output;
        this.socket = socket;
        this.command = command;
    }
    
    @Override
    public void run() {
        try {
            Object get = commandResult.get();
            Optional<String> output = this.command.parseData(get);
            if (output.isPresent()) {
                writeToOutput(output.get());
            }
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
