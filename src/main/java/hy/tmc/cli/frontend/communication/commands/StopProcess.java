package hy.tmc.cli.frontend.communication.commands;

import hy.tmc.cli.frontend.FrontendListener;
import hy.tmc.cli.frontend.communication.server.ProtocolException;


public class StopProcess extends Command {

    /**
     * StopProcess command. 
     * @param front 
     */
    public StopProcess(FrontendListener front) {
        super(front);
    }

    /**
     * Exit java virtual machine
     */
    @Override
    protected void functionality() {
        System.exit(0);
    }

    /**
     * Does nothing, this command does not require data.
     * @throws ProtocolException 
     */
    @Override
    public void checkData() throws ProtocolException {
        
    }
}
