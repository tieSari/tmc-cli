package hy.tmc.cli;

import com.google.common.base.Optional;
import hy.tmc.cli.configuration.ConfigHandler;
import hy.tmc.core.configuration.TmcSettings;
import hy.tmc.core.domain.Course;
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
    }
    
    public CliSettings(String mainDirectory, String apiVersion) {
        this(apiVersion);
        this.mainDirectory = mainDirectory;
    }

    public CliSettings() {
        this.apiVersion = "7";
    }

    /**
     * Constructor for tests.
     * @param handler Dependency injected configHandler (mock for exemple)
     */
    public CliSettings(ConfigHandler handler, String mainDirectory, String apiVersion){
        this.mainDirectory = mainDirectory;
        this.apiVersion = apiVersion;
        this.config = handler;
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
    
    public Date getLastUpdate(){
        return lastUpdate;
    }
    
    public void setLastUpdate(Date time){
        this.lastUpdate = time;
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

    public void setServerAddress(String serverAddress) {
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
    
    @Override
    public String toString(){
        String value = "";
        if(serverAddress != null){
            value += "Serveraddress: " + this.getServerAddress() + " ";
        }
        if(username != null){
            value += "Username: " + this.getUsername() + " ";
        }
        if(password != null){
            value += "Password: " + this.getPassword() + " ";
        }
        if(courseID != null){
            value += "CourseID: " + this.getCourseID() + " ";
        }
        if(path != null){
            value += "Path: " + this.getPath() + " ";
        }
        if(currentCourse != null){
            value += "CurrentCourse is not null ";
        } else {
            value += "CurrentCourse is null ";
        }
        return value;
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
