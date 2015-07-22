package hy.tmc.cli.frontend.communication.server;

import com.google.common.util.concurrent.ListenableFuture;
import hy.tmc.cli.frontend.CommandLineProgressObserver;
import hy.tmc.cli.frontend.communication.commands.CommandResultParser;
import hy.tmc.core.TmcCore;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SocketRunnable implements Runnable {

    protected Socket socket;
    private TmcCore core;

    public SocketRunnable(Socket clientSocket, TmcCore core) {
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
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        } catch (ProtocolException ex) {
            Logger.getLogger(SocketRunnable.class.getName()).log(Level.SEVERE, null, ex);
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
        CoreUser user = new CoreUser();
        ProtocolParser parser = new ProtocolParser();
        // IMPL THIS!!!!! command.setObserver(new CommandLineProgressObserver(outputStream));
        String input = inputReader.readLine();
        if(input == null){
            throw new ProtocolException("Input was invalid: empty");
        }
        final ListenableFuture<?> commandFuture = parser.getCommand(input);
        /* IMPL THIS!!!! if (commandFuture != null) {
            final DataOutputStream output = outputStream;
            addListenerToFuture(commandFuture, output, command);
        }*/
    }

    /**
     * Reads input and starts corresponding command-object.
     */
    /*private Command parseCommand(BufferedReader inputReader, DataOutputStream stream)
            throws IOException {
        String input = inputReader.readLine();
        if (input == null) {
            return null;
        }
        try {
            ListenableFuture<?>result = new Pro.getCommand(input);
            if(command==null) return null;
            command.checkData();
            return command;
        }
        catch (ProtocolException ex) {
            stream.write((ex.getMessage() + "\n").getBytes());
            socket.close();
            return null;
        }
    }*/

    /**
     * When command-future is ready, the listener will execute run-method; it will write the output
     * of command back to calling socket.
     *
     * @param commandResult Command-object that has been started.
     * @param output stream where to write result.
     */
    private void addListenerToFuture(ListenableFuture<?> commandResult,
                                     final DataOutputStream output, CommandResultParser command) {
       // commandResult.addListener(new SocketListener(commandResult, output, socket, command), core.getThreadPool());
    }
}