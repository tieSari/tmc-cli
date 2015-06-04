package hy.tmc.cli.backend;

import hy.tmc.cli.frontend.FrontendListener;
import hy.tmc.cli.frontend.communication.commands.Command;
import hy.tmc.cli.frontend.communication.server.ProtocolException;


public class BadCommandStub extends Command{

    public BadCommandStub(FrontendListener front) {
        super(front);
    }

    @Override
    protected void functionality() {     
    }

    @Override
    public void checkData() throws ProtocolException {   
        throw new ProtocolException("I'm a bad command-stub :(");
    }    
}
