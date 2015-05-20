package hy.tmc.cli.Configuration;

import hy.tmc.cli.domain.Course;

/**
 * This class will be initialized when Auth is successful. Use this to get data
 * of user
 */
public class ClientData {

    private static int PID;
    private static final int PORT = 1234;
    private static String USERNAME = "";
    private static String PASSWORD = "";
    private static Course currentCourse;

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

    public static String getFormattedUserData() {
        return USERNAME + ":" + PASSWORD;
    }

    public static void logOutCurrentUser() {
        USERNAME = "";
        PASSWORD = "";
    }

    public static int getPID() {
        return PID;
    }

    public static int getPORT() {
        return PORT;
    }

    public static void setPID(int PID) {
        ClientData.PID = PID;
    }

    public static String getUSERNAME() {
        return USERNAME;
    }

    public static String getPASSWORD() {
        return PASSWORD;
    }
}
