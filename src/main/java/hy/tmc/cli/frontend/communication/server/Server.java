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
            System.out.println("Server creation failed");
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
                System.out.println(e.getMessage());
            }
            // new thread for a client
            new SocketThread(clientSocket, tmcCore).start();
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

    /**
     * Prints line to server output.
     *
     * @param outputLine string to print in server out
     */
    @Override
    public void printLine(String outputLine) {
        if (clientSocket == null) {
            return;
        }
        PrintWriter out;
        try {
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            out.println(outputLine);
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
        System.out.println(outputLine);
    }

    public void printLine(String outputLine, Socket socket) {
        if (clientSocket == null) {
            return;
        }
        PrintWriter out;
        try {
            out = new PrintWriter(socket.getOutputStream(), true);
            out.println(outputLine);
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
        System.out.println(outputLine);
    }
}
