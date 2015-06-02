package hy.tmc.cli.frontend.communication.commands;

import hy.tmc.cli.frontend.FrontendListener;
import hy.tmc.cli.frontend.communication.server.ProtocolException;


public class StopProcess extends Command {

    public StopProcess(FrontendListener front) {
        super(front);
    }

    @Override
    protected void functionality() {
        System.exit(0);
    }

    @Override
    public void checkData() throws ProtocolException {
        
    }

}
