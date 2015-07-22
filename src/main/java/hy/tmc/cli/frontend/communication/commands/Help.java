package hy.tmc.cli.frontend.communication.commands;

import com.google.common.base.Optional;

public class Help extends CommandResultParser<String> {


    @Override
    public Optional<String> parseData(Object data) {
        return Optional.of((String) data);
    }


    /**
     * Takes the command map and returns a set of command names.
     *
     * @return a set of all available command names.
     */
    public String helpMessage() {
        StringBuilder enterprise = new StringBuilder();

        enterprise.append("Available commands:\n")
                .append("help\n")
                .append("test\n")
                .append("submit\n")
                .append("paste\n")
                .append("list exercises\n")
                .append("list courses\n")
                .append("download <course ID>\n")
                .append("set server <tmc-server address>\n")
                .append("login\n")
                .append("logout");

        return enterprise.toString();
    }
}
