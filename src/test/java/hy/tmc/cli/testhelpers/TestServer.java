package hy.tmc.cli.testhelpers;

import hy.tmc.cli.configuration.ConfigHandler;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import static org.junit.Assert.fail;

public class TestServer implements Runnable {

    private ServerSocket serverSocket;
    private boolean isRunning;
    private BufferedReader in;
    private Socket clientSocket;
    private int port;
    private final Map<String, String> mocks;

    public TestServer() throws IOException {
        initServerSocket();
        mocks = new HashMap<>();
    }

    private void initServerSocket() {
        try {
            serverSocket = new ServerSocket(0);
            port = serverSocket.getLocalPort();
            new ConfigHandler().writePort(port);
        }
        catch (IOException ex) {
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
                    clientSocket = serverSocket.accept();
                    BufferedReader inputReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    DataOutputStream outputStream = new DataOutputStream(clientSocket.getOutputStream());
                    handleInput(inputReader, outputStream);
                }
            }
            catch (IOException e) {
                System.err.println(e.getMessage());
            }
        }
    }
    
    public void mock(String input, String willReturn) {
        this.mocks.put(input, willReturn);
    }

    private void handleInput(BufferedReader inputReader, DataOutputStream outputStream)
            throws IOException {
        String input = inputReader.readLine();
        if (input == null) {
            writeToOutput(outputStream, "received null");
        }
               
        if (mocks.containsKey(input)) {
            writeToOutput(outputStream, mocks.get(input));
        } else {
            writeToOutput(outputStream, "did not expect message " + input + " from script");
        }
    }

    private void writeToOutput(DataOutputStream outputStream, String message) throws IOException {
        outputStream.write((message).getBytes());
        clientSocket.close();
    }
    
    /**
     * Closes serverSocket.
     *
     * @throws IOException if failed to close socket
     */
    public void close() throws IOException {
        isRunning = false;
        serverSocket.close();
    }

    public int port() {
        return this.port;
    }

}
