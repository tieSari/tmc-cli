package hy.tmc.cli.frontend.communication.server;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import hy.tmc.cli.backend.communication.HttpResult;
import hy.tmc.cli.backend.communication.UrlCommunicator;
import hy.tmc.cli.configuration.ConfigHandler;
import hy.tmc.cli.domain.submission.FeedbackQuestion;
import hy.tmc.cli.frontend.FrontendListener;
import hy.tmc.cli.frontend.RangeFeedbackHandler;
import hy.tmc.cli.frontend.TextFeedbackHandler;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.PrintWriter;

import java.net.ServerSocket;
import java.net.Socket;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server implements FrontendListener, Runnable {

    private Socket clientSocket;
    private final ProtocolParser parser;
    private ServerSocket serverSocket;
    private boolean isRunning;
    private BufferedReader in;
    private JsonArray feedbackAnswers = new JsonArray();
    private RangeFeedbackHandler rangeFeedbackHandler;
    private TextFeedbackHandler textFeedbackHandler;

    /**
     * Constructor for server.
     *
     * @throws IOException if failed to write port to config file
     */
    public Server() throws IOException {
        try {
            serverSocket = new ServerSocket(0);
            int serverPort = serverSocket.getLocalPort();
            new ConfigHandler().writePort(serverPort);
            System.out.println("Listening on port " + serverPort);
        } catch (IOException ex) {
            System.out.println("Server creation failed");
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.parser = new ProtocolParser(this);
        this.rangeFeedbackHandler = new RangeFeedbackHandler(this);
        this.textFeedbackHandler = new TextFeedbackHandler(this);
    }

    /**
     * Dependency injection for tests.
     */
    public Server(RangeFeedbackHandler handler) throws IOException {
        this();
        this.rangeFeedbackHandler = handler;
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
        if (feedbackQuestions.isEmpty()) {
            return;
        }


        List<FeedbackQuestion> rangeQuestions = new ArrayList<>();
        List<FeedbackQuestion> textQuestions = new ArrayList<>();
        for (FeedbackQuestion feedbackQuestion : feedbackQuestions) {
            if (feedbackQuestion.getKind().equals("text")) {
                textQuestions.add(feedbackQuestion);
            } else {
                rangeQuestions.add(feedbackQuestion);
            }
        }

        this.rangeFeedbackHandler.feedback(rangeQuestions, feedbackUrl);
        this.textFeedbackHandler.feedback(textQuestions, feedbackUrl);

        if (!rangeQuestions.isEmpty()) {
            rangeFeedbackHandler.askQuestion(); // ask first questions
        } else {
            textFeedbackHandler.askQuestion();
        }
    }

    /**
     * Takes the answer from a range feedback question.
     */
    public void rangeFeedbackAnswer(String answer) {
        JsonObject jsonAnswer = new JsonObject();
        jsonAnswer.addProperty("question_id", rangeFeedbackHandler.getLastId());
        String validAnswer = rangeFeedbackHandler.validateAnswer(answer);
        jsonAnswer.addProperty("answer", validAnswer);
        feedbackAnswers.add(jsonAnswer);

        if (this.rangeFeedbackHandler.allQuestionsAsked()) {
            if (textFeedbackHandler.allQuestionsAsked()) {
                printLine("end");
                sendToTmcServer();
                this.feedbackAnswers = new JsonArray();
            } else {
                textFeedbackHandler.askQuestion(); // start asking text questions
            }
        } else {
            rangeFeedbackHandler.askQuestion();
        }
    }

    /**
     * Takes the answer from a text feedback question.
     */
    public void textFeedbackAnswer(String answer) {
        JsonObject jsonAnswer = new JsonObject();
        jsonAnswer.addProperty("question_id", textFeedbackHandler.getLastId());
        jsonAnswer.addProperty("answer", answer);
        feedbackAnswers.add(jsonAnswer);

        if (this.textFeedbackHandler.allQuestionsAsked()) {
            printLine("end");
            sendToTmcServer();
            this.feedbackAnswers = new JsonArray();
        } else {
            textFeedbackHandler.askQuestion();
        }
    }

    protected void sendToTmcServer() {
        JsonObject req = getAnswersJson();
        try {
            HttpResult httpResult = UrlCommunicator.makePostWithJson(req, getFeedbackUrl());
            printLine(httpResult.getData());
        } catch (IOException e) {
            printLine(e.getMessage());
        }
    }

    private String getFeedbackUrl() {
        return rangeFeedbackHandler.getFeedbackUrl() + "?" + new ConfigHandler().apiParam;
    }

    private JsonObject getAnswersJson() {
        JsonObject req = new JsonObject();
        req.add("answers", feedbackAnswers);
        return req;
    }
}
