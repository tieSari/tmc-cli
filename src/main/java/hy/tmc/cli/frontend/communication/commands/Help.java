package hy.tmc.cli.frontend.communication.commands;

import com.google.common.base.Joiner;

import com.google.common.base.Optional;

public class Help extends Command<String> {


    /**
     * List all available commands.
     */
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

    @Override
    public Optional<String> parseData(Object data) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String call() throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
