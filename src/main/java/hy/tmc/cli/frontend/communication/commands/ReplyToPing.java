package hy.tmc.cli.frontend.communication.commands;

import com.google.common.base.Optional;

public class ReplyToPing extends Command {

    private final String answer = "pong/n";

    /**
     * print pong to the frontend.
     */
    @Override
    protected Optional<String> functionality() {
        return Optional.of(answer);
    }

    /**
     * Does nothing, this command requires no data.
     */
    @Override
    public void checkData() {
    }
}
