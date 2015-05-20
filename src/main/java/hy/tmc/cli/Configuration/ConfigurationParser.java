package hy.tmc.cli.Configuration;

public class ConfigurationParser {

    private String[] arguments;

    /**
     * This class will configure the URL of TMC-server.
     */
    public ConfigurationParser(String[] arguments) {
        this.arguments = arguments;
        this.configureServerData();
    }

    private void configureServerData() {
        if (arguments.length < 1) {
            return;
        }
        String authenticationURL = arguments[0];
        ServerData.setAuthUrl(authenticationURL);
        if (arguments.length < 2) {
            return;
        }
        String coursesURL = arguments[1];
        ServerData.setCoursesUrl(coursesURL);
    }
}
