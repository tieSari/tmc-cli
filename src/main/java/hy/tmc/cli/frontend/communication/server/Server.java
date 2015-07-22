package hy.tmc.cli.frontend.communication.server;

import com.google.gson.JsonArray;
import hy.tmc.cli.configuration.ConfigHandler;
import hy.tmc.cli.frontend.RangeFeedbackHandler;
import hy.tmc.cli.frontend.TextFeedbackHandler;
import hy.tmc.core.TmcCore;
import java.io.BufferedReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server implements Runnable {

    private ServerSocket serverSocket;
    private boolean isRunning;
    private TmcCore tmcCore;
    private ExecutorService socketThreadPool;
    private BufferedReader in;

    /**
     * Constructor for server. It finds a free port to be listened to.
     *
     * @throws IOException if failed to write port to configuration file
     */
    public Server() throws IOException {
        this(new TmcCore(), Executors.newCachedThreadPool());
    }
    
    public Server(TmcCore tmcCore, ExecutorService socketThreadPool) throws IOException {
        this.tmcCore = tmcCore;
        this.socketThreadPool = socketThreadPool;
        initServerSocket();
    }

    private void initServerSocket() {
        try {
            serverSocket = new ServerSocket(0);
            new ConfigHandler().writePort(serverSocket.getLocalPort());
        } catch (IOException ex) {
            System.err.println("Server creation failed");
            System.err.println(ex.getMessage());
        }
    }

    public int getCurrentPort() {
        return this.serverSocket.getLocalPort();
    }

    public boolean isRunning() {
        return isRunning;
    }

    /**
     * Run is loop that accepts new client connection and handles it. Submits the new socket task
     * into a thread pool that executes is with a thread that is free.
     */
    @Override
    public final void run() {
        isRunning = true;
        while (true) {
            try {
                if (!serverSocket.isClosed()) {
                    Socket clientSocket = serverSocket.accept();
                    socketThreadPool.submit(new SocketRunnable(clientSocket, tmcCore));
                }
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        }
    }

    /**
     * Closes serverSocket. Destroys the Socket pool.
     *
     * @throws IOException if failed to close socket
     */
    public void close() throws IOException {
        isRunning = false;
        serverSocket.close();
        socketThreadPool.shutdown();
    }
}
