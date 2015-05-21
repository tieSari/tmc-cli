package hy.tmc.cli.frontend_communication.Commands;

import hy.tmc.cli.Configuration.ConfigHandler;
import hy.tmc.cli.frontend_communication.FrontendListener;
import hy.tmc.cli.frontend_communication.Server.ProtocolException;
import hy.tmc.cli.logic.Logic;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;


public class ChooseServer extends Command {

    public ChooseServer(FrontendListener front, Logic backend) {
        super(front, backend);
    }

    @Override
    protected void functionality() {
        try {
            new ConfigHandler().writeServerAddress(data.get("tmc-server"));
        }
        catch (IOException ex) {
            Logger.getLogger(ChooseServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void checkData() throws ProtocolException {
        if (!this.data.containsKey("tmc-server")){
            throw new ProtocolException("must specify new server");
        }
        if (notValidURL(this.data.get("tms-server"))) {
            throw new ProtocolException("given URL is not valid");
        }
    }
    
    private boolean notValidURL(String url) {
        Pattern tmcServerAddress = Pattern.compile("http://.*");
        return tmcServerAddress.matcher(url).matches();
    }

}
