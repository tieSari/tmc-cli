package hy.tmc.cli.frontend.communication.server;

import com.google.common.util.concurrent.ListenableFuture;
import hy.tmc.cli.backend.TmcCore;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SocketThread extends Thread {

    protected Socket socket;
    private TmcCore core;

    public SocketThread(Socket clientSocket, TmcCore core) {
        this.socket = clientSocket;
        this.core = core;
    }

    public void run() {
        InputStream inp = null;
        BufferedReader brinp = null;
        DataOutputStream out = null;
        try {
            inp = socket.getInputStream();
            brinp = new BufferedReader(new InputStreamReader(inp));
            out = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            return;
        }
        String line;
        while (true) {
            try {
                line = brinp.readLine();
                final ListenableFuture<String> result = core.runCommand(line);
                final DataOutputStream output = out;
                result.addListener(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            output.writeUTF(result.get() + "/n");
                            output.flush();
                            socket.close();
                        } catch (InterruptedException | ExecutionException | IOException ex) {
                            System.out.println(ex.getMessage());
                        }
                    }
                }, core.getPool());
            } catch (ProtocolException | IOException ex) {
                System.err.println(ex.getMessage());
            }
        }
    }
}
