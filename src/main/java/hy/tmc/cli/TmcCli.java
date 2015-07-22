package hy.tmc.cli;

import hy.tmc.cli.frontend.communication.server.Server;
import hy.tmc.core.TmcCore;
import java.io.IOException;

public class TmcCli {

    private TmcCore core;
    private Server server;
    private Thread serverThread;

    public TmcCli(TmcCore core) throws IOException {
        this.core = core;
        server = new Server();
        serverThread = new Thread(server);
    }

    public void startServer() {
        if (serverThread.isAlive() || server.isRunning()) {
            return;
        }
        serverThread.start();
    }

    public void stopServer() throws IOException {
        server.close();
        serverThread.interrupt();
    }
}
