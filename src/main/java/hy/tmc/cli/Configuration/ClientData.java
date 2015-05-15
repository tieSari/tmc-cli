package hy.tmc.cli.Configuration;

/**
 * This class will be initialized when connection was successfull. Use this to get data
 */
public class ClientData {

    private static int PID = 0;
    private static int PORT = 1234; // change plz. This is the default port
    private static String USERNAME = "";
    private static String PASSWORD = "";

    public static void logIn(String username, String password) {
        USERNAME = username;
        PASSWORD = password;
    }

    public static void logIn(String username, String password, int pid) {
        PID = pid;
        USERNAME = username;
        PASSWORD = password;
    }

    public static void logIn(String username, String password, int pid, int port) {
        PID = pid;
        USERNAME = username;
        PASSWORD = password;
        PORT = port;
    }

    public static void logOutCurrentUser() {
        PID = 0;
        PORT = 1234;
        USERNAME = "";
        PASSWORD = "";
    }

    public static int getPID() {
        return PID;
    }

    public static int getPORT() {
        return PORT;
    }

    public static String getUSERNAME() {
        return USERNAME;
    }

    public static String getPASSWORD() {
        return PASSWORD;
    }

}
