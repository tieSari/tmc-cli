package hy.tmc.cli.configuration;

import hy.tmc.cli.domain.Course;

/**
 * This class will be initialized when Auth is successful. Use this to get data
 * of user
 */
public final class ClientData {

    private static int PID;
    private static String USERNAME = "";
    private static String PASSWORD = "";
    private static Course currentCourse;


    private ClientData() {
    }
    /**
     * Sets the data for current user.
     * @param username Username of the current user
     * @param password Password of the current user
     */
    public static void setUserData(String username, String password) {
        USERNAME = username;
        PASSWORD = password;
    }

    public static Course getCurrentCourse() {
        return currentCourse;
    }

    public static void setCurrentCourse(Course currentCourse) {
        ClientData.currentCourse = currentCourse;
    }

    public static boolean userDataExists() {
        return !(USERNAME.isEmpty() || PASSWORD.isEmpty());
    }
    
    public static void clearUserData() {
        USERNAME = "";
        PASSWORD = "";
    }

    public static String getFormattedUserData() {
        return USERNAME + ":" + PASSWORD;
    }

    public static void logOutCurrentUser() {
        USERNAME = "";
        PASSWORD = "";
    }

    public static int getPid() {
        return PID;
    }

    public static void setPid(int pid) {
        ClientData.PID = pid;
    }

    public static String getUsername() {
        return USERNAME;
    }

    public static String getPassword() {
        return PASSWORD;
    }
}
