/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hy.tmc.cli.frontend_communication.Server;

import hy.tmc.cli.frontend_communication.Commands.Command;
import hy.tmc.cli.frontend_communication.IOinterface;
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
public class Server implements IOinterface {

    private int portNumber;

    public Server(int portNumber) {
        this.portNumber = portNumber;
    }

    @Override
    public void start() throws IOException {
        ServerSocket serverSocket = new ServerSocket(portNumber);
        Socket clientSocket = serverSocket.accept();
        BufferedReader in = new BufferedReader(
                new InputStreamReader(clientSocket.getInputStream()));

        String inputLine = null;
        String outputLine;

        while (true) {
            inputLine = in.readLine();
            if (inputLine == null) {
                break;
            }
            
            Command c;
            try {
                c = ProtocolParser.getCommand(inputLine);
                c.execute();
            } catch (ProtocolException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            if (inputLine.equals("q")) {
                break;
            }
            printLine("server says hi!", clientSocket);
        }
    }

    public void printLine(String outputLine, Socket clientSocket) throws IOException{
        PrintWriter out
                = new PrintWriter(clientSocket.getOutputStream(), true);
        out.println(outputLine);
    }
}
