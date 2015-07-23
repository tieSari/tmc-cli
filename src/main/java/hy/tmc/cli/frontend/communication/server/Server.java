package hy.tmc.cli.frontend.communication.server;

import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import hy.tmc.cli.TmcCli;
import hy.tmc.cli.configuration.ConfigHandler;
import hy.tmc.core.TmcCore;
import hy.tmc.core.communication.UrlCommunicator;

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
    private ListeningExecutorService socketThreadPool;
    private BufferedReader in;
    private TmcCli cli;

    /**
     * Constructor for server. It finds a free port to be listened to.
     *
     * @throws IOException if failed to write port to configuration file
     */
    public Server(TmcCli cli) throws IOException {
        this(cli, MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(100)));
    }

    public Server(TmcCli cli, ListeningExecutorService socketThreadPool) throws IOException {
        this.tmcCore = cli.getCore();
        this.socketThreadPool = socketThreadPool;
        this.cli = cli;
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
                    System.err.println("New socketRunnable");
                    socketThreadPool.submit(new SocketRunnable(clientSocket, tmcCore, socketThreadPool, cli));
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