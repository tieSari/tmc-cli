package hy.tmc.cli.frontend.communication.server;

import static hy.tmc.cli.frontend.communication.commands.CommandFactory.createCommandMap;

import hy.tmc.cli.frontend.FrontendListener;
import hy.tmc.cli.frontend.communication.commands.Command;
import hy.tmc.cli.logic.Logic;

import java.util.HashMap;
/**
 * ProtocolParser parses user input to executable command.
 */
public class ProtocolParser {

    private FrontendListener server;
    private Logic logic;
    private HashMap<String, Command> commandsByName = new HashMap<>();

    /**
     * Constructor for Protocol Parser.
     *
     * @param server frontend server
     * @param logic backend logic
     */
    public ProtocolParser(FrontendListener server, Logic logic) {
        this.server = server;
        this.logic = logic;
        this.commandsByName = createCommandMap(this.server, this.logic);
    }

    /**
     * Search for command by inputline.
     *
     * @param inputLine input String
     * @return Command that matches input
     * @throws ProtocolException if bad command name
     */
    public Command getCommand(String inputLine) throws ProtocolException {
        String[] elements = inputLine.split(" ");
        String commandName = elements[0];
        if (!commandsByName.containsKey(commandName)) {
            throw new ProtocolException("Invalid command name");
        }
        Command command = commandsByName.get(commandName);
        command = giveData(elements, command);
        return command;
    }

    private Command giveData(String[] userInput, Command command) {
        for (int i = 1; i + 1 < userInput.length; i += 2) {
            String key = userInput[i];
            String value = userInput[i + 1];
            command.setParameter(key, value);
        }
        return command;
    }
}
