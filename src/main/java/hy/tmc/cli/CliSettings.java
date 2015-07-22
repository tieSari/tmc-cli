package hy.tmc.cli;

import com.google.common.base.Optional;
import hy.tmc.cli.configuration.ConfigHandler;
import hy.tmc.core.TmcCore;
import hy.tmc.core.configuration.TmcSettings;
import hy.tmc.core.domain.Course;
import java.io.IOException;

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

    public CliSettings(String apiVersion) {
        this.apiVersion = apiVersion;
    }
    
    public CliSettings(String mainDirectory, String apiVersion) {
        this(apiVersion);
        this.mainDirectory = mainDirectory;
    }
    
    @Override
    public String getServerAddress() {
        return this.serverAddress;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }
    
    public void setPath(String path){
        this.path = path;
    }
    
    public String getPath(){
        return this.path;
    }

    @Override
    public boolean userDataExists() {
        if (this.username == null || this.password == null) {
            return false;
        }
        return !this.username.isEmpty() && !this.password.isEmpty();
    }

    @Override
    public Optional<Course> getCurrentCourse() {
        if (this.currentCourse == null) {
            return Optional.absent();
        }
        return Optional.of(this.currentCourse);
    }

    @Override
    public String apiVersion() {
        return apiVersion;
    }

    public void setCurrentCourse(Course currentCourse) {
        this.currentCourse = currentCourse;
    }

    public void setServerAddress(String serverAddress) throws IOException {
        this.serverAddress = serverAddress;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public void logOutCurrentUser() {
        username = "";
        password = "";
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }
    
    public void setCourseID(String courseID){
        this.courseID = courseID;
    }
    
    public String getCourseID(){
        return this.courseID;
    }
}
