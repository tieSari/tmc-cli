package hy.tmc.cli.frontend.communication.server;

import hy.tmc.cli.configuration.ConfigHandler;
import hy.tmc.cli.domain.submission.FeedbackQuestion;
import hy.tmc.cli.frontend.FrontendListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import java.net.ServerSocket;
import java.net.Socket;

import java.util.ArrayDeque;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;



public class Server implements FrontendListener, Runnable {

    private Socket clientSocket;
    private final ProtocolParser parser;
    private ServerSocket serverSocket;
    private boolean isRunning;
    private BufferedReader in;
    private ArrayDeque<FeedbackQuestion> feedbackQueue;

    /**
     * Constructor for server.
     * @throws IOException if failed to write port to config file
     */
    



    public Server() throws IOException {
        this.feedbackQueue = new ArrayDeque<>();
        try {
            serverSocket = new ServerSocket(0);
            new ConfigHandler().writePort(serverSocket.getLocalPort());
        } catch (IOException ex) {
            System.out.println("Server creation failed");
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.parser = new ProtocolParser(this);
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
        while (isRunning) {
            if (!startClientProcess()) {
                break;
            }
        }
    }

    private boolean startClientProcess() {
        try (final Socket cs = serverSocket.accept()) {
            if (!introduceClientSuccessful(cs)) {
                return false;
            }
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
            isRunning = false;
        }
        return true;
    }

    private boolean introduceClientSuccessful(Socket client) throws IOException {
        this.clientSocket = client;
        return canListenClient(clientSocket);
    }

    private boolean canListenClient(Socket clientSocket) throws IOException {
        in = new BufferedReader(
                new InputStreamReader(clientSocket.getInputStream()));
        String inputLine = readCommandFromClient(clientSocket);

        if (inputLine == null) {
            return false;
        }

        try {
            parseAndExecuteCommand(inputLine);
        } catch (ProtocolException ex) {
            printLine(ex.getMessage());
        }
        return true;
    }

    private String readCommandFromClient(Socket clientSocket) throws IOException {
        // BufferedReader in = ;
        return in.readLine();
    }

    private void parseAndExecuteCommand(String inputLine) throws ProtocolException {
        parser.getCommand(inputLine).execute();
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
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Printlinessa");
        }
        System.out.println(outputLine);
    }

    @Override
    public void feedback(List<FeedbackQuestion> feedbackQuestions, String feedbackUrl) {
        this.feedbackQueue.addAll(feedbackQuestions);

        /*for (FeedbackQuestion question : feedbackQuestions) {
            printLine(question.getQuestion() + ": ");

            String answer;
            try {
                answer = null;
                while (answer == null) {
                    answer = readCommandFromClient(clientSocket);
                }
                printLine("vastaus: " + answer);
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }


        }
        printLine("end");
        */
    }
}
