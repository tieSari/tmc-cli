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
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ilari
 */
public class TestClient {

    private final static String ERROR = "testClient failed to connect";
    private final int port;

    public TestClient(int port) {
        this.port = port;
    }

    public String send(String data) {
        System.out.println("send ping");
        try {
            Socket kkSocket = new Socket("127.0.0.1", port);
            PrintWriter out = new PrintWriter(kkSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(kkSocket.getInputStream()));

            out.write(data);
            out.flush();
            if (in.ready()) {
                return in.readLine();
            } else {
                return ERROR;
            }
        } catch (IOException ex) {
            Logger.getLogger(TestClient.class.getName()).log(Level.SEVERE, null, ex);
            return ERROR;
        }

    }
}
