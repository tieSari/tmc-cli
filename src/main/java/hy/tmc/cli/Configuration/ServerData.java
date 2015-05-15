package hy.tmc.cli.Configuration;

public class ServerData {
    
    private static String coursesUrl = "https://tmc.mooc.fi/staging/courses.json?api_version=7";
    private static String authUrl = "https://tmc.mooc.fi/staging/user";

    public static String getCoursesUrl() {
        return coursesUrl;
    }

    public static void setCoursesUrl(String coursesUrl) {
        ServerData.coursesUrl = coursesUrl;
    }

    public static String getAuthUrl() {
        return authUrl;
    }

    public static void setAuthUrl(String authUrl) {
        ServerData.authUrl = authUrl;
    }
}
