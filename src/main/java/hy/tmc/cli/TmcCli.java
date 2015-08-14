package hy.tmc.cli;

import com.google.common.base.Optional;

import fi.helsinki.cs.tmc.core.TmcCore;
import fi.helsinki.cs.tmc.core.domain.Course;

import fi.helsinki.cs.tmc.core.exceptions.TmcCoreException;
import hy.tmc.cli.configuration.ConfigHandler;
import hy.tmc.cli.frontend.communication.server.Server;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.Date;

public class TmcCli {

    private final Server server;
    private final Thread serverThread;
    private final String apiVersion = "7";

    private TmcCore core;
    private ConfigHandler config;
    private boolean makeUpdate = true;
    private CliSettings settings;

    public TmcCli(boolean makeUpdate) throws IOException, TmcCoreException {
        this();
        this.makeUpdate = makeUpdate;
    }

    public TmcCli(ConfigHandler config) throws IOException, TmcCoreException {
        this();
        this.config = config;
    }

    public TmcCli() throws IOException, TmcCoreException {
        Path cacheFile;
        if (System.getenv("XDG_CACHE_HOME") != null) {
            cacheFile = Paths.get(System.getenv("XDG_CACHE_HOME"), "tmc", "cache");
        } else {
            cacheFile = Paths.get(System.getenv("HOME"), ".cache", "tmc", "cache");
        }
        Files.createDirectories(cacheFile.getParent());
        if (!Files.exists(cacheFile)) {
            Files.createFile(cacheFile);
        }
        settings = new CliSettings(apiVersion);
        core = new TmcCore(settings);
        core.setCacheFile(cacheFile.toFile());
        this.config = new ConfigHandler();
        server = new Server(this);
        serverThread = new Thread(server);
    }

    public TmcCli(TmcCore core, ConfigHandler config) throws IOException, TmcCoreException {
        this(config);
        this.core = core;
    }

    public TmcCli(TmcCore core) throws IOException, TmcCoreException {
        this();
        this.core = core;
    }

    public TmcCli(TmcCore coreMock, boolean makeUpdate) throws IOException, TmcCoreException {
        this(coreMock);
        this.makeUpdate = makeUpdate;
    }

    public boolean makeUpdate() {
        return this.makeUpdate;
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
        settings.setUserData(username, password);
    }

    public void logout() {
        settings.clear();
    }

    public boolean setServer(String serverAddress) {
        settings.setServerAddress(serverAddress);
        return true;
    }

    public Optional<Course> getCurrentCourse() {
        return settings.getCurrentCourse();
    }

    public void setCurrentCourse(Course course) {
        settings.setCurrentCourse(course);
    }

    /**
     * The default settings include credentials from current session, and a
     * server address from the config file.
     *
     * @return CliSettings with credentials and server address
     * @throws IllegalStateException if server address is not found in the
     *                               config file
     */
    public CliSettings defaultSettings() throws IllegalStateException, ParseException, IOException {
//        CliSettings settings = new CliSettings(apiVersion);
//        settings.setUserData(session.getUsername(), session.getPassword());
//        settings.setCurrentCourse(session.getCurrentCourse());
//        settings.setServerAddress(config.readServerAddress());
//        settings.setLastUpdate(config.readLastUpdate());
        if (settings.getServerAddress() == null) throw new IllegalStateException("Server address not set");
        return settings;
    }

    public TmcCore getCore() {
        return core;
    }

    public void refreshLastUpdate() throws IOException {
        this.config.writeLastUpdate(new Date());
    }
}
