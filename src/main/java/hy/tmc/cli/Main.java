package hy.tmc.cli;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {

    public static void main(String[] args) throws IOException {
        // TODO code all application logic here
        System.out.println("Hello world!");
        int portNumber = 3576;

        ServerSocket serverSocket = new ServerSocket(portNumber);
        Socket clientSocket = serverSocket.accept();
        PrintWriter out
                = new PrintWriter(clientSocket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(
                new InputStreamReader(clientSocket.getInputStream()));

        
        String inputLine, outputLine;
        
        while ((inputLine = in.readLine()) != null) {
            outputLine = inputLine + " Recieved";
            out.println(outputLine);
            if (outputLine.equals("Bye Recieved")) {
                break;
            }
        }

    }

}
