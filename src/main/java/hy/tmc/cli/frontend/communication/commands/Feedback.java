package hy.tmc.cli.frontend.communication.commands;

import com.google.common.base.Optional;
import hy.tmc.cli.frontend.communication.server.ProtocolException;

public class Feedback extends Command {

    @Override
    protected Optional<String> functionality() {
        return null;
    }

    @Override
    public void checkData() throws ProtocolException {
        if (!data.containsKey("question")) {
            throw new ProtocolException("Question missing");
        }
    }

}
