package hy.tmc.cli.frontend.communication.server;

import com.google.common.util.concurrent.ListenableFuture;
import hy.tmc.cli.backend.TmcCore;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.concurrent.ExecutionException;

public class SocketThread extends Thread {

    protected Socket socket;
    private TmcCore core;

    public SocketThread(Socket clientSocket, TmcCore core) {
        this.socket = clientSocket;
        this.core = core;
    }

    /**
     * Reads the input from socket, starts command-object and when command is ready, prints the
     * result back to socket.
     */
    @Override
    public void run() {
        try {
            if (!socket.isClosed()) {
                BufferedReader inputReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
                handleInput(inputReader, outputStream);
            }
        }
        catch (ProtocolException | IOException ex) {
            System.err.println(ex.getMessage());
        }
    }

    /**
     * Reads input and starts corresponding command-object.
     */
    private ListenableFuture<String> parseCommand(BufferedReader inputReader, DataOutputStream stream)
            throws IOException {
        String input = inputReader.readLine();
        try {
            return core.runCommand(input);
        }
        catch (ProtocolException ex) {
            stream.writeUTF(ex.getMessage() + "\n");
            socket.close();
            this.interrupt();
            return null;
        }
    }

    /**
     * Finds command-future and attach listener to it.
     *
     * @param inputReader
     * @param outputStream
     */
    private void handleInput(BufferedReader inputReader, DataOutputStream outputStream)
            throws IOException, ProtocolException {
        final ListenableFuture<String> commandFuture = parseCommand(inputReader, outputStream);
        final DataOutputStream output = outputStream;
        addListenerToFuture(commandFuture, output);
        this.interrupt();
    }

    /**
     * When command-future is ready, the listener will execute run-method; it will write the output
     * of command back to calling socket.
     *
     * @param commandResult Command-object that has been started.
     * @param output stream where to write result.
     */
    private void addListenerToFuture(final ListenableFuture<String> commandResult,
            final DataOutputStream output) {
        commandResult.addListener(new Runnable() {
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
        }, core.getPool());
    }
}
