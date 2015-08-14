package hy.tmc.cli;

import com.google.common.base.Optional;

import fi.helsinki.cs.tmc.core.configuration.TmcSettings;
import fi.helsinki.cs.tmc.core.domain.Course;

import hy.tmc.cli.configuration.ConfigHandler;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;

public class CliSettings implements TmcSettings {

    private Course currentCourse;
    private String serverAddress;
    private String username;
    private String password;
    private String apiVersion;
    private int pid;
    private String mainDirectory;
    private String path;
    private String courseID;
    private ConfigHandler config;
    private Date lastUpdate;

    public CliSettings(String apiVersion) {
        this.apiVersion = apiVersion;
        try {
            this.config = new ConfigHandler();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public CliSettings(String mainDirectory, String apiVersion) {
        this(apiVersion);
        this.mainDirectory = mainDirectory;
    }

    public CliSettings() {
        this("7");
    }

    /**
     * Constructor for tests.
     *
     * @param handler Dependency injected configHandler (mock for exemple)
     */
    public CliSettings(ConfigHandler handler, String mainDirectory, String apiVersion) {
        this.mainDirectory = mainDirectory;
        this.apiVersion = apiVersion;
        this.config = handler;
    }

    @Override
    public String getServerAddress() {
        return this.serverAddress;
    }

    public void setServerAddress(String serverAddress) {
        this.serverAddress = serverAddress;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Date getLastUpdate() {
        try {
            return config.readLastUpdate();
        } catch (ParseException | IOException e) {
            e.printStackTrace();
            return new Date(0);
        }
    }

    public void setLastUpdate(Date time) {
        this.lastUpdate = time;
    }

    public String getPath() {
        return this.path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public boolean userDataExists() {
        return !(this.username == null || this.password == null) &&
            !this.username.isEmpty() && !this.password.isEmpty();
    }

    @Override
    public Optional<Course> getCurrentCourse() {
        if (this.currentCourse == null) {
            return Optional.absent();
        }
        return Optional.of(this.currentCourse);
    }

    public void setCurrentCourse(Course currentCourse) {
        this.currentCourse = currentCourse;
    }

    @Override
    public String apiVersion() {
        return apiVersion;
    }

    public void setApiVersion(String apiVersion) {
        this.apiVersion = apiVersion;
    }

    public void setMainDirectory(String mainDirectory) {
        this.mainDirectory = mainDirectory;
    }

    @Override
    public String getFormattedUserData() {
        if (!userDataExists()) {
            return "";
        }
        return this.username + ":" + this.password;
    }

    @Override
    public String getTmcMainDirectory() {
        return this.mainDirectory;
    }

    /**
     * Sets the data for current user.
     *
     * @param username of the current user
     * @param password of the current user
     */
    public void setUserData(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public void clear() {
        username = "";
        password = "";
        this.currentCourse = null;
        serverAddress = "";
    }


    @Override
    public String clientName() {
        return "tmc_cli";
    }

    @Override
    public String clientVersion() {
        return "0.6";
    }
}
