package hy.tmc.cli.Configuration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Class is used to write to config file and read from it
 */
public class ConfigHandler {

    private String configFilePath;
    public final String coursesExtension = "/courses.json?api_version=7";
    public final String authExtension = "/user";

    /**
     * Creates new config handler with default filename and path in current directory
     */
    public ConfigHandler() {
        this.configFilePath = "config.properties";
    }
    /**
     * Creates new config handler with specified path and name
     * @param path for config file
     */
    public ConfigHandler(String path) {
        this.configFilePath = path;
    }

    public String getConfigFilePath() {
        return configFilePath;
    }

    public void setConfigFilePath(String configFileName) {
        this.configFilePath = configFileName;
    }

    private Properties getProperties() {
        Properties prop = new Properties();
        try {
            InputStream inputStream = new FileInputStream(new File(configFilePath));
            prop.load(inputStream);
        }
        catch (IOException e) { 
        }
        return prop;
    }

    /**
     * Writes server address to config file, ex. "https://tmc.mooc.fi/hy"
     * @param address for tmc server
     * @throws IOException if unable to write address
     */
    
    public void writeServerAddress(String address) throws IOException {
        Properties prop = getProperties();
        prop.setProperty("serverAddress", address);
        prop.store(new FileWriter(new File(configFilePath)), "Updated properties");
    }

    /**
     * Reads server address from config 
     * @return address of tmc server
     */
    
    public String readServerAddress() {
        Properties prop = getProperties();
        return prop.getProperty("serverAddress");
    }
    
    /**
     * Reads address from which to list courses
     * @return String with tmc server address + courses path
     */
    
    public String readCoursesAddress() {
        String serverAddress = readServerAddress();
        if (serverAddress == null || serverAddress.isEmpty()) {
            return null;
        }
        return serverAddress + coursesExtension;
    }
    
    /**
     * Reads address to which auth GET can be sent
     * @return String with tmc server address + user path
     */
    
    public String readAuthAddress() {
        String serverAddress = readServerAddress();
        if (serverAddress == null || serverAddress.isEmpty()) {
            return null;
        }
        return serverAddress + authExtension;
    }
    
    public String getCourseUrl(int id) {
        return this.readServerAddress() + "/courses/" + id + ".json" + "?api_version=7";
    }
}
