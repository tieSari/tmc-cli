package hy.tmc.cli.frontend.communication.server;

import com.google.common.util.concurrent.ListenableFuture;
import hy.tmc.cli.backend.TmcCore;
import hy.tmc.cli.configuration.ConfigHandler;
import hy.tmc.cli.frontend.FrontendListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server implements FrontendListener, Runnable {

    private Socket clientSocket;
    private final ProtocolParser parser;
    private ServerSocket serverSocket;
    private boolean isRunning;
    private TmcCore tmcCore;

    /**
     * Constructor for server.
     *
     * @throws IOException if failed to write port to config file
     */
    public Server() throws IOException {
        try {
            serverSocket = new ServerSocket(0);
            new ConfigHandler().writePort(serverSocket.getLocalPort());
        } catch (IOException ex) {
            System.err.println("Server creation failed");
            System.err.println(ex.getMessage());
        }
        this.parser = new ProtocolParser(this);
        tmcCore = new TmcCore();
    }

    public int getCurrentPort() {
        return this.serverSocket.getLocalPort();
    }

    /**
     * Start is general function to set up server listening for the frontend.
     */
    @Override
    public void start() {
        this.run();
    }

    /**
     * Run is loop that accepts new client connection and handles it.
     */
    @Override
    public final void run() {
        isRunning = true;
         while (true) {
            try {
                clientSocket = serverSocket.accept();
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
            // new thread for a client
            new SocketThread(clientSocket, tmcCore).start();
            try {
                Thread.sleep(200);
            }
            catch (InterruptedException ex) {
               System.err.println(ex.getMessage());
            }
        }
    }

    /**
     * Closes serverSocket.
     *
     * @throws IOException if failed to close socket
     */
    public void close() throws IOException {
        isRunning = false;
        this.serverSocket.close();
    }
}
