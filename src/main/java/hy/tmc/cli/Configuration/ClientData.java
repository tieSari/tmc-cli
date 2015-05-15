package hy.tmc.cli.Configuration;

/**
 * This class will be initialized when connection was successfull. Use this to get data
 */
public class ClientData {

    private static int PID;
    private static final int PORT = 1234; // change plz. This is the default port
    private static String USERNAME = "";
    private static String PASSWORD = "";

    public static void setUserData(String username, String password) {
        USERNAME = username;
        PASSWORD = password;
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
