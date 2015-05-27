package hy.tmc.cli.frontend.communication.commands;

import hy.tmc.cli.configuration.ConfigHandler;
import hy.tmc.cli.frontend.FrontendListener;
import hy.tmc.cli.frontend.communication.server.ProtocolException;
import hy.tmc.cli.logic.Logic;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

public class ChooseServer extends Command {

    private ConfigHandler handler;

    public ChooseServer(FrontendListener front, Logic backend) {
        super(front, backend);
        this.handler = new ConfigHandler();
    }

    public ChooseServer(ConfigHandler handler, FrontendListener front, Logic backend) {
        super(front, backend);
        this.handler = handler;
    }

    @Override
    protected void functionality() {
        try {
            handler.writeServerAddress(data.get("tmc-server"));            
        } catch (IOException ex) {
            Logger.getLogger(ChooseServer.class.getName()).log(Level.SEVERE, null, ex);
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
        Pattern tmcServerAddress = Pattern.compile("http://.*");
        if (url == null) {
            return false;
        }
        return tmcServerAddress.matcher(url).matches();
    }

}
