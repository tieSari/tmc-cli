package hy.tmc.cli.Configuration;

public class ServerData {

    private static String serverUrl = "https://tmc.mooc.fi/staging/";
    private static int apiVersion = 7;

    public static String getServerUrl() {
        return serverUrl;
    }

    public static String getApiVersion() {
        return "?api_version=" + apiVersion;
    }

    public static void setServerUrl(String serverUrl) {
        ServerData.serverUrl = serverUrl;
    }

    public static String getCoursesUrl() {
        return serverUrl + "courses.json" + getApiVersion();
    }

    public static String getCourseUrl(int id) {
        return serverUrl + "/courses/" + id + ".json" + getApiVersion();
    }

    public static String getAuthUrl() {
        return serverUrl + "user";
    }

}
