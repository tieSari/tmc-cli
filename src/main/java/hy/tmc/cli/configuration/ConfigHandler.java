package hy.tmc.cli.configuration;

import com.google.common.base.Strings;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import static com.google.common.base.Strings.isNullOrEmpty;

/**
 * Writes data to config file and reads from it.
 */
public class ConfigHandler {

    private final String portFieldName = "serverPort";
    private final String serverAddressFieldName = "serverAddress";
    private final String lastUpdate = "lastUpdate";
    private Path configFilePath;
    private EnvironmentWrapper environment;

    private SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy HH:mm:ss");

    /**
     * Creates new config handler with default filename and path in current directory.
     */
    public ConfigHandler() throws IOException {
        environment = new EnvironmentWrapper();
        this.configFilePath = this.getConfigDirectory().resolve("config.properties");
        createConfigFileIfMissing();
    }

    /**
     * Creates new config handler with specified path and name.
     *
     * @param path for config file
     */
    public ConfigHandler(Path path) {
        this.configFilePath = path;
    }

    /**
     * For testing.
     *
     * @param mock enables testing of multiple platforms.
     */
    public ConfigHandler(EnvironmentWrapper mock) {
        this.environment = mock;
        this.configFilePath = this.getConfigDirectory().resolve("config.properties");
    }

    public String getConfigFilePath() {
        return configFilePath.toString();
    }

    public void setConfigFilePath(String configFileName) {
        this.configFilePath = Paths.get(configFileName);
    }

    private Properties getProperties() {
        Properties prop = new Properties();
        File propertyFile = configFilePath.toFile();
        try {
            if (!propertyFile.exists()) {
                propertyFile.createNewFile();
                return prop;
            }
            InputStream inputStream = new FileInputStream(propertyFile);
            prop.load(inputStream);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        return prop;
    }

    private void writeData(String property, String data) throws IOException {
        Properties prop = getProperties();
        prop.setProperty(property, data);
        FileWriter writer = new FileWriter(configFilePath.toFile());
        prop.store(writer, "Updated properties");
        writer.close();
    }

    /**
     * Writes server address to config file, ex. "https://tmc.mooc.fi/fi.helsinki.cs".
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
        if (isNullOrEmpty(dateInString)) {
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

    private Path getConfigDirectory() {
        if (isUnixLikePlatform()) {
            return Paths.get(xdgConfigFolder(), "tmc");
        } else if (environment.getOsName().toLowerCase().contains("windows")) {
            return Paths.get(environment.getenv("APPDATA"), "tmc");
        }
        return Paths.get("");
    }

    private String xdgConfigFolder() {
        String xdgConf = environment.getenv("XDG_CONFIG_HOME");
        if (Strings.isNullOrEmpty(xdgConf)) {
            return environment.getHomeDirectory() + File.separatorChar + ".config";
        }
        return xdgConf;
    }

    private void createConfigFileIfMissing() throws IOException {
        if (!Files.exists(configFilePath.toAbsolutePath().getParent())) {
            Files.createDirectories(configFilePath.toAbsolutePath().getParent());
        }
        if (!Files.exists(configFilePath, LinkOption.NOFOLLOW_LINKS)) {
            Files.createFile(configFilePath);
        }
    }

    private boolean isUnixLikePlatform() {
        String osname = environment.getOsName().toLowerCase();
        return osname.equals("linux") || osname.contains("mac os x") || osname.contains("freebsd");
    }
}
