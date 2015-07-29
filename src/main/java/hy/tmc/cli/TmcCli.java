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
    private ConfigHandler config;
    private final String apiVersion = "7";

    public TmcCli(TmcCore core) throws IOException {
        this.core = core;
        this.config = new ConfigHandler("config.properties");
        this.session = new Session();
        server = new Server(this);
        serverThread = new Thread(server);
    }

    public TmcCli(TmcCore core, ConfigHandler config) throws IOException {
        this(core);
        this.config = config;
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

    /**
     * The default settings include credentials from current session, and a
     * server address from the config file.
     *
     * @return CliSettings with credentials and server address
     * @throws IllegalStateException if server address is not found in the
     * config file
     */
    public CliSettings defaultSettings() throws IllegalStateException {
        CliSettings settings = new CliSettings(apiVersion);
        settings.setUserData(session.getUsername(), session.getPassword());
        settings.setServerAddress(config.readServerAddress());

        return settings;
    }

    public TmcCore getCore() {
        return core;
    }
}
