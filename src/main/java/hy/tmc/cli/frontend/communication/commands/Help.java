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

        enterprise.append("Commands:\n")
                .append("    help [command name] \tShow help for a command\n")
                .append("    test \t\t\tRun local tests\n")
                .append("    submit \t\t\tSubmit an exercise\n")
                .append("    paste \t\t\tCreate a tmc-paste\n")
                .append("    list exercises \t\tList all exercises\n")
                .append("    list courses \t\tList all courses\n")
                .append("    download <course ID> \tDownload a course\n")
                .append("    set server <address> \tChange your tmc-server\n")
                .append("    login \t\t\tLog in to tmc\n")
                .append("    logout \t\t\tLog out of tmc\n")
                .append("    update \t\t\tGet updates to your current course\n")
                .append("    set course <coursename> \tSet current course");

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
                        + "display a help message for that command";
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
