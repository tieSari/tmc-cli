package hy.tmc.cli;

import fi.helsinki.cs.tmc.core.domain.Course;

public class Session {

    private String username;
    private String password;
    private Course currentCourse;

    public Session() {
        this.username = "";
        this.password = "";
        this.currentCourse = null;
    }

    public Session(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setCredentials(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public void clear() {
        username = "";
        password = "";
        currentCourse = null;
    }

    public Course getCurrentCourse() {
        return currentCourse;
    }

    public void setCurrentCourse(Course currentCourse) {
        this.currentCourse = currentCourse;
    }
}
