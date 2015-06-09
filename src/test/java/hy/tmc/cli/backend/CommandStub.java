package hy.tmc.cli.backend;

import com.google.common.base.Optional;
import hy.tmc.cli.frontend.communication.commands.Command;
import hy.tmc.cli.frontend.communication.server.ProtocolException;


public class CommandStub extends Command{

    @Override
    protected Optional<String> functionality() {     
        return Optional.absent();
    }

    @Override
    public void checkData() throws ProtocolException {        
    }    
}
