package hy.tmc.cli.testhelpers;

import hy.tmc.cli.frontend.FrontendListener;
import hy.tmc.cli.frontend.communication.commands.Command;
import hy.tmc.cli.frontend.communication.server.ProtocolException;

public class CommandStub extends Command {


    /**
     * A stub command for tests.
     * Allows access to commands params
     */
    public CommandStub(FrontendListener front) {
        super(front);
    }

    @Override
    protected void functionality() {

    }

    @Override
    public void checkData() throws ProtocolException {

    }

    public String getValue(String param) {
        return this.data.get(param);
    }
}
