package hy.tmc.cli.frontend.communication.commands;

import com.google.common.base.Joiner;

import com.google.common.base.Optional;

public class Help extends Command {


    /**
     * List all available commands.
     */
    @Override
    protected Optional<String> functionality() {
        String commands = "Available commands: \n";
        commands += Joiner.on(", ").join(CommandFactory.allCommandNames());
        return Optional.of(commands);
    }

    /**
     * Does nothing, this command does not require data.
     */
    @Override
    public void checkData() {
    }
}
