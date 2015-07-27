package hy.tmc.cli.frontend.communication.server;

import com.google.common.util.concurrent.ListeningExecutorService;
import hy.tmc.cli.TmcCli;
import hy.tmc.core.TmcCore;
import hy.tmc.core.exceptions.TmcCoreException;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SocketRunnable implements Runnable {

    protected Socket socket;
    private TmcCore core;
    private ListeningExecutorService pool;
    private TmcCli cli;

    public SocketRunnable(Socket clientSocket, TmcCore core, ListeningExecutorService pool, TmcCli cli) {
        this.socket = clientSocket;
        this.core = core;
        this.pool = pool;
        this.cli = cli;
    }

    /**
     * Reads the input from socket and starts executing features.
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
        } catch (ProtocolException | TmcCoreException | InterruptedException | ExecutionException ex) {
            Logger.getLogger(SocketRunnable.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void handleInput(BufferedReader inputReader, DataOutputStream outputStream)
            throws IOException, ProtocolException, TmcCoreException, InterruptedException, ExecutionException {
        CommandExecutor executor = new CommandExecutor(outputStream, this.socket, this.pool, this.cli);
        // IMPL THIS!!!!! command.setObserver(new CommandLineProgressObserver(outputStream));
        String input = inputReader.readLine();
        System.err.println("Input: " + input);
        if(input == null){
            throw new ProtocolException("Input was invalid: empty");
        }
        executor.parseAndExecute(input);
    }
}