package hy.tmc.cli.Configuration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigHandler {

    private String configFilePath;

    public ConfigHandler() {
        this.configFilePath = "config.properties";
    }

    public String getConfigFilePath() {
        return configFilePath;
    }

    public void setConfigFilePath(String configFileName) {
        this.configFilePath = configFileName;
    }

    private Properties getProperties() {
        Properties prop = null;
        try {
            prop = new Properties();
            InputStream inputStream = new FileInputStream(new File(configFilePath));
            prop.load(inputStream);
        }
        catch (IOException e) {
            //TODO LOTS OF STUFF
        }
        return prop;
    }

    public void writeServerAddresss(String address) throws IOException {
        Properties prop = getProperties();
        prop.setProperty("serverAddress", address);
        prop.store(new FileWriter(new File(configFilePath)), "Updated properties");
    }

    public String readServerAddress() {
        Properties prop = getProperties();
        return prop.getProperty("serverAddress");
    }
    
    public String readCoursesAddress() {
        return readServerAddress() + "/courses.json?api_version=7";
    }
    
    public String readAuthAddress() {
        return readServerAddress() + "/user";
    }
}
