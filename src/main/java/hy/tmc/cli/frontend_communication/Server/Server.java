/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hy.tmc.cli.frontend_communication.Server;

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
public class Server {

    private int portNumber;

    public Server(int portNumber) {
        this.portNumber = portNumber;
    }

    public void listen() throws IOException {
        ServerSocket serverSocket = new ServerSocket(portNumber);
        Socket clientSocket = serverSocket.accept();
        PrintWriter out
                = new PrintWriter(clientSocket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(
                new InputStreamReader(clientSocket.getInputStream()));

        String inputLine=null;
        String outputLine;

        while (true) {
            inputLine = in.readLine();
            if (inputLine == null){
                break;
            }
            if (inputLine.equals("q")){
                break;
            }
            outputLine = "server says hi!";
            out.println(outputLine);
        }
    }
}
