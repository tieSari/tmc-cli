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
public class Server implements FrontendListener {

    private int portNumber;
    private Socket clientSocket;
    private ProtocolParser parser;
    private ServerSocket serverSocket;

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
    @Override
    public void start() {

        while (true) {
            try {
                clientSocket = serverSocket.accept();
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(clientSocket.getInputStream()));

                String inputLine = null;
                String outputLine;

                while (true) {
                    inputLine = in.readLine();
                    if (inputLine == null) {
                        break;
                    }
                    try {
                        Command command = parser.getCommand(inputLine);
                        command.execute();
                    } catch (ProtocolException ex) {
                        Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    if (inputLine.equals("q")) {
                        break;
                    }
                    printLine("server says hi!");
                }
            } catch (IOException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
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
        }

    }

    private void findFreePort() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
