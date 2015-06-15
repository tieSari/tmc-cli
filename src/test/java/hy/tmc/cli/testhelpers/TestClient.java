package hy.tmc.cli.testhelpers;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class TestClient {

    private Socket socket;
    private PrintWriter output;
    private BufferedReader input;
    private final int portnumber;

    public TestClient(int portnumber) throws IOException {
        this.portnumber = portnumber;
        this.init();
    }

    public void init() throws IOException {
        this.socket = new Socket("localhost", portnumber);
        this.output = new PrintWriter(socket.getOutputStream(), true);
        this.input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    public boolean isClosedFromServer() {
        try {
            input.read();
        }
        catch (IOException ex) {
            return false;
        }
        return true;
    }

    public Socket getSocket() {
        return socket;
    }

    public void sendMessage(String message) throws IOException {
        output.println(message);
    }

    public boolean hasNewMessages() throws IOException {
        return input.ready();
    }

    public String reply() {
        try {
            return input.readLine();
        }
        catch (IOException ex) {
            System.err.println(ex.getMessage());
            return "fail";
        }
    }
    
    public boolean isReadyToBeRead() throws IOException {
        return input.ready();
    }
    
}
