package hy.tmc.cli.frontend.communication.commands;

import com.google.common.base.Joiner;

import com.google.common.base.Optional;

public class Help extends Command<String> {

    /**
     * Does nothing, this command does not require data.
     */
    @Override
    public void checkData() {
    }

    @Override
    public Optional<String> parseData(Object data) {
        return Optional.of((String)data);
    }

    @Override
    public String call() throws Exception {
        checkData();
        String commands = "Available commands: \n";
        commands += Joiner.on(", ").join(CommandFactory.allCommandNames());
        return commands;
    }
}
