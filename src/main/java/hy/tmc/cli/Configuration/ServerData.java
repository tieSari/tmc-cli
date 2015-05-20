package hy.tmc.cli.Configuration;

public class ServerData {

    private static String serverUrl = "https://tmc.mooc.fi/staging/";

    public static String getServerUrl() {
        return serverUrl;
    }

    public static void setServerUrl(String serverUrl) {
        ServerData.serverUrl = serverUrl;
    }

    public static String getCoursesUrl() {
        return serverUrl + "courses.json?api_version=7";
    }

    public static String getAuthUrl() {
        return serverUrl + "user";
    }

}
