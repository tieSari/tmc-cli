package hy.tmc.cli.testhelpers;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TestClient {

    private Socket socket;
    private String reply;
    private PrintWriter output;
    private BufferedReader input;
    private int portnumber;

    public TestClient(int portnumber) throws IOException {
        this.portnumber = portnumber;
        this.init();
    }

    private void init() {
        try {
            this.socket = new Socket("localhost", portnumber);
            this.output = new PrintWriter(socket.getOutputStream(), true);
            this.input = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
    }

    public void sendMessage(String message) throws IOException {
        output.println(message);
    }

    public String reply() {
        try {
            return input.readLine();
        } catch (IOException ex) {
            Logger.getLogger(TestClient.class.getName()).log(Level.SEVERE, null, ex);
            return "fail";
        }
    }
}
