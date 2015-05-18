/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hy.tmc.cli.frontend_communication.Server;

import hy.tmc.cli.frontend_communication.Commands.Command;
import hy.tmc.cli.frontend_communication.FrontendListener;
import hy.tmc.cli.logic.Logic;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author kristianw
 */
public class Server implements FrontendListener, Runnable {

    public final static String PROTOCOL_ERROR_MSG = "message not in accordance with protocol";
    private final int portNumber;
    private Socket clientSocket;
    private final ProtocolParser parser;
    private ServerSocket serverSocket;
    private Thread running;
    
    /**
     * Server constructor
     *
     * @param portNumber
     * @param logic
     */
    public Server(int portNumber, Logic logic) {
        this.portNumber = portNumber;
        try {
            serverSocket = new ServerSocket(portNumber);
        } catch (IOException ex) {
            System.out.println("Server creation failed");
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.parser = new ProtocolParser(this, logic);
    }
      /**
     * Start is general function to set up server listening for the frontend
     */
    public void start() {
        this.run();  
    }
    
    public void run() {
        while (true) {
            try {
                Socket clientSocket = serverSocket.accept();
                this.clientSocket = clientSocket;
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(clientSocket.getInputStream()));

                String inputLine;
                while (true) {
                    inputLine = in.readLine();
                    if (inputLine == null) {
                        break;
                    }
                    try {
                        Command command = parser.getCommand(inputLine);
                        command.execute();
                        clientSocket.close();
                        in.close();
                        break;
                    } catch (ProtocolException ex) {
                        Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                        printLine(Server.PROTOCOL_ERROR_MSG);
                    }
                }
            } catch (IOException ex) {
                System.out.println("Ei toimi");
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void close() throws IOException {
        this.serverSocket.close();
    }

    /**
     * Prints line to server output
     *
     * @param outputLine
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
}
