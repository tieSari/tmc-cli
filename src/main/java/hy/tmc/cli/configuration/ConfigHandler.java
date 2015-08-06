package hy.tmc.cli.configuration;

import static com.google.common.base.Strings.isNullOrEmpty;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

/**
 * Writes data to config file and reads from it.
 */
public class ConfigHandler {

    private String configFilePath;
    private String portFieldName = "serverPort";
    private String serverAddressFieldName = "serverAddress";
    private String lastUpdate = "lastUpdate";

    private SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");

    /**
     * Creates new config handler with default filename and path in current directory.
     */
    public ConfigHandler() {
        this.configFilePath = "config.properties";
    }

    /**
     * Creates new config handler with specified path and name.
     *
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
        File propertyFile = new File(configFilePath);
        try {
            if (!propertyFile.exists()) {
                propertyFile.createNewFile();
                return prop;
            }
            InputStream inputStream = new FileInputStream(propertyFile);
            prop.load(inputStream);
        }
        catch (IOException e) {
            System.err.println(e.getMessage());
        }
        return prop;
    }

    private void writeData(String property, String data) throws IOException {
        Properties prop = getProperties();
        prop.setProperty(property, data);
        FileWriter writer = new FileWriter(new File(configFilePath));
        prop.store(writer, "Updated properties");
        writer.close();
    }

    /**
     * Writes server address to config file, ex. "https://tmc.mooc.fi/hy".
     *
     * @param address for tmc server
     * @throws IOException if unable to write address
     */
    public void writeServerAddress(String address) throws IOException {
        writeData(serverAddressFieldName, address);
    }

    /**
     * Reads and returns the server address of the TMC-server.
     *
     * @return address of tmc server
     */
    public String readServerAddress() throws IllegalStateException {
        Properties prop = getProperties();
        String address = prop.getProperty(serverAddressFieldName);
        if (isNullOrEmpty(address)) {
            throw new IllegalStateException("tmc-server address not set");
        }
        return address;
    }

    /**
     * Reads port local server from config file.
     */
    public int readPort() {
        Properties prop = getProperties();
        return Integer.parseInt(prop.getProperty(portFieldName));
    }

    /**
     * Writes port to config file.
     *
     * @param port to write in config
     */
    public void writePort(int port) throws IOException {
        writeData(portFieldName, Integer.toString(port));
    }

    /**
     * Reads latest update time from config file.
     */
    public Date readLastUpdate() throws ParseException, IOException {
        Properties prop = getProperties();
        String dateInString = prop.getProperty(lastUpdate);
        if(isNullOrEmpty(dateInString)){
            Date d = new Date();
            this.writeLastUpdate(d);
            return d;
        }
        Date date;
        date = sdf.parse(dateInString);
        return date;
    }

    /**
     * Writes latest update time to config file.
     *
     * @param lastUpdate to write in config
     */
    public void writeLastUpdate(Date lastUpdate) throws IOException {
        String date = sdf.format(lastUpdate);
        writeData("lastUpdate", date);
    }
}
