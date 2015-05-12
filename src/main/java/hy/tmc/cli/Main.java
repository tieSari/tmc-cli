package hy.tmc.cli;

import hy.tmc.cli.frontend_communication.Server.Server;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {

    public static void main(String[] args) throws IOException {
        // TODO code all application logic here
        Server newServer = new Server(1234);

    }

}
