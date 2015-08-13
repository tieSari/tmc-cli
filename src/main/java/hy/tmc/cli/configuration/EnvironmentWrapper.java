package hy.tmc.cli.configuration;

public class EnvironmentWrapper {

    public String getenv(String s) {
        return System.getenv(s);
    }

    public String getOsName() {
        return System.getProperty("os.name");
    }

    public String getHomeDirectory() {
        return System.getProperty("user.home");
    }
}
