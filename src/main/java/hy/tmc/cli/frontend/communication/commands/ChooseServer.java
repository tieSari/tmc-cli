package hy.tmc.cli.frontend.communication.commands;

import com.google.common.base.Optional;
import com.google.common.base.Strings;
import hy.tmc.cli.TmcCli;
import hy.tmc.cli.configuration.ConfigHandler;
import hy.tmc.cli.frontend.communication.server.ProtocolException;
import java.io.IOException;
import java.util.regex.Pattern;

public class ChooseServer extends Command<Boolean> {

    private ConfigHandler handler;
    private TmcCli cli;

    /**
     *
     * @param handler
     * @param cli
     */
    public ChooseServer(ConfigHandler handler, TmcCli cli) {
        this.handler = handler;
        this.cli = cli;
    }

    public ChooseServer() {
        handler = new ConfigHandler();
    }

    public ChooseServer(String serverAddress) {
        handler = new ConfigHandler();
        this.setParameter("tmc-server", serverAddress);
    }

    private boolean isValidTmcUrl(String url) {
        String urlPattern = "(https?://)?([a-z]+\\.){2,}[a-z]+(/[a-z]+)*";
        Pattern tmcServerAddress = Pattern.compile(urlPattern);
        if (Strings.isNullOrEmpty(url)) {
            return false;
        }
        return tmcServerAddress.matcher(url).matches();
    }

    public Optional<String> parseData(Object data) {
        Boolean result = (Boolean) data;

        String address = this.data.get("tmc-server");
        if (result) {
            return Optional.of("Address changed to " + address);
        }
        return Optional.of("Failed to change address. Check logs for information.");
    }

    @Override
    public Boolean call() throws Exception {
        return null;
      
    }
}
