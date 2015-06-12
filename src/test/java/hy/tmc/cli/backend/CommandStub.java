package hy.tmc.cli.backend;

import hy.tmc.cli.frontend.FrontendListener;
import hy.tmc.cli.frontend.communication.commands.Command;
import hy.tmc.cli.frontend.communication.server.ProtocolException;


public class CommandStub extends Command{

    public CommandStub(FrontendListener front) {
        super(front);
    }

    @Override
    protected void functionality() {     
    }

    @Override
    public void checkData() throws ProtocolException {        
    }    
}
