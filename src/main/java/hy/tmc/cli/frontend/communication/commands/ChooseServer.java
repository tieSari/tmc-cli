package hy.tmc.cli.frontend.communication.commands;

import com.google.common.base.Strings;
import hy.tmc.cli.configuration.ConfigHandler;
import hy.tmc.cli.frontend.FrontendListener;
import hy.tmc.cli.frontend.communication.server.ProtocolException;
import java.io.IOException;
import java.util.regex.Pattern;

public class ChooseServer extends Command {

    private ConfigHandler handler;

    public ChooseServer(FrontendListener front) {
        super(front);
        this.handler = new ConfigHandler();
    }

    public ChooseServer(ConfigHandler handler, FrontendListener front) {
        super(front);
        this.handler = handler;
    }

    @Override
    protected void functionality() {
        try {
            handler.writeServerAddress(data.get("tmc-server"));            
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
    }

    @Override
    public void checkData() throws ProtocolException {
        if (!this.data.containsKey("tmc-server")) {
            throw new ProtocolException("must specify new server");
        }
        if (!isValidTmcUrl(this.data.get("tmc-server"))) {
            throw new ProtocolException("given URL is not valid");
        }
    }

    private boolean isValidTmcUrl(String url) {
        String urlPattern = "(http://)?([a-z]+\\.){2,}[a-z]+(/[a-z]+)*";
        Pattern tmcServerAddress = Pattern.compile(urlPattern);
        if (Strings.isNullOrEmpty(url)) {
            return false;
        }
        return tmcServerAddress.matcher(url).matches();
    }
}
