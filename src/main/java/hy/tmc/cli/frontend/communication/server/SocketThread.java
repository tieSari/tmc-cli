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
     * Reads the input from socket, starts command-object and when command is ready,
     * prints the result back to socket.
     */
    @Override
    public void run() {
        BufferedReader inputReader = null;
        DataOutputStream outputStream = null;
        try {
            inputReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            outputStream = new DataOutputStream(socket.getOutputStream());
            handleInput(inputReader, outputStream);
        } catch (ProtocolException | IOException ex) {
            System.err.println(ex.getMessage());
        }
    }

    /**
     * Reads input and starts corresponding command-object.
     */
    private ListenableFuture<String> parseCommand(BufferedReader inputReader)
            throws ProtocolException, IOException {
        String input = inputReader.readLine();
        return core.runCommand(input);
    }

    /**
     * Finds command-future and attach listener to it.
     *
     * @param inputReader
     * @param outputStream
     */
    private void handleInput(BufferedReader inputReader, DataOutputStream outputStream)
            throws IOException, ProtocolException {
        final ListenableFuture<String> commandFuture = parseCommand(inputReader);
        final DataOutputStream output = outputStream;
        addListenerToFuture(commandFuture, output);
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
                catch (InterruptedException | ExecutionException | IOException ex) {
                    System.out.println(ex.getMessage());
                }
            }
            private void writeToOutput(final String commandOutput) throws IOException {
                output.writeUTF(commandOutput);
                output.flush();
                socket.close();
            }
        }, core.getPool());
    }
}
