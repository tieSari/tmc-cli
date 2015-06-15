package hy.tmc.cli.testhelpers;

import com.google.common.base.Optional;
import hy.tmc.cli.frontend.communication.commands.Command;
import hy.tmc.cli.frontend.communication.server.ProtocolException;

public class CommandStub extends Command {

    /**
     * A stub command for tests. Allows access to commands params
     */
    @Override
    public void checkData() throws ProtocolException {

    }

    public String getValue(String param) {
        return this.data.get(param);
    }

    @Override
    protected Optional<String> functionality() {
        return Optional.absent();
    }
}
