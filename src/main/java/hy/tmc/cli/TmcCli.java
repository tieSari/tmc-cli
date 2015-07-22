package hy.tmc.cli;

import hy.tmc.cli.configuration.ConfigHandler;
import hy.tmc.cli.frontend.communication.server.Server;
import hy.tmc.core.TmcCore;
import java.io.IOException;

public class TmcCli {

    private final TmcCore core;
    private final Server server;
    private final Thread serverThread;
    private Session session;
    private final ConfigHandler config;
    private final String apiVersion = "7";

    public TmcCli(TmcCore core) throws IOException {
        this.core = core;
        this.config = new ConfigHandler("config.properties");
        this.session = new Session();
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

    public void login(String username, String password) {
        this.session.setCredentials(username, password);
    }

    public void logout() {
        this.session.clear();
    }

    public boolean setServer(String serverAddress) {
        try {
            config.writeServerAddress(serverAddress);
            return true;
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
            return false;
        }
    }

    public CliSettings defaultSettings() throws IOException {
        CliSettings settings = new CliSettings(apiVersion);
        settings.setUserData(session.getUsername(), session.getPassword());
        settings.setServerAddress(config.readServerAddress());

        return settings;
    }
}
