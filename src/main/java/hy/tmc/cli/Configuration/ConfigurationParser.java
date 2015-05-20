package hy.tmc.cli.Configuration;

public class ConfigurationParser {
    
    private String[] arguments;

    public ConfigurationParser(String[] arguments) {
        this.arguments = arguments;
        this.configureServerData();
    }

    private void configureServerData() {
        String authenticationURL = arguments[0];
        String coursesURL = arguments[1];
    }
}
