package hy.tmc.cli.frontend.communication.commands;

import hy.tmc.cli.TmcCli;

public class Help extends Command<String> {

    private final String dataKey = "command";

    public Help(TmcCli cli) {
        super(cli);
    }
    
    public Help(TmcCli cli, String command) {
        super(cli);
        if (command != null) {
            setParameter(dataKey, command);
        }
    }

    public String mainHelpMessage() {
        StringBuilder enterprise = new StringBuilder();

        enterprise.append("Available commands:\n")
                .append("help [command name]\n")
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

    @Override
    public String call() throws Exception {
        if (!data.containsKey(dataKey)) {
            return mainHelpMessage();
        } else {
            return helpForCommand(data.get(dataKey));
        }
    }

    private String helpForCommand(String command) {
        switch (command) {
            case "help":
                return "'help' will list all commands, help <command-name> will "
                        + "display a helpmessage for that command";
            case "test":
                return "Runs the local tests for an exercise. You need to be in "
                        + "that exercise directory, or any subdirectory";
            case "submit":
                return "Submits an exercise to the tmc-server. You need to be in"
                        + " that exercise directory, or any subdirectory";
            case "paste":
                return "Creates a tmc-paste of an exercise. You need to be in"
                        + " that exercise directory, or any subdirectory";
            case "download":
                return "'Download <course ID>' downloads a course with the id from "
                        + "the tmc-server. Use 'list courses' to see all courses and their ids. "
                        + "The courses exercises will be downloaded under the current directory";
            case "list":
                return "'list courses' will list all courses on the tmc-server specified."
                        + "'list exercises' will list all exercises of the current course. "
                        + "You must be in the course directory, or any subdirectory.";
            case "set":
                return "set server <tmc-server address> will change your tmc-server to the one given";
            case "login":
                return "Log in to tmc. Username and password will be asked.";
            case "logout":
                return "Log out of tmc. Clears your userdata from program memory.";
            default:
                return "I cannot help you with " + command + " :(";
        }
    }
}
