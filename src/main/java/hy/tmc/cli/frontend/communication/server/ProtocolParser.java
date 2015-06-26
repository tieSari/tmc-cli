package hy.tmc.cli.frontend.communication.server;

import static hy.tmc.cli.frontend.communication.commands.CommandFactory.createCommandMap;

import hy.tmc.cli.frontend.FrontendListener;
import hy.tmc.cli.frontend.communication.commands.Command;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ProtocolParser parses user input to executable command.
 */
public class ProtocolParser {

    private FrontendListener server;
    private Map<String, Command> commandsByName = new HashMap<>();

    /**
     * Constructor for Protocol Parser.
     *
     * @param server frontend server
     */
    public ProtocolParser(FrontendListener server) {
        this.server = server;
        this.commandsByName = createCommandMap();
    }

    public ProtocolParser() {
        this.commandsByName = createCommandMap();
    }

    /**
     * Constructor for Protocol Parser.
     *
     * @param server frontend server
     */
    public ProtocolParser(FrontendListener server, HashMap<String, Command> availableCommands) {
        this.server = server;
        this.commandsByName = availableCommands;
    }

    /**
     * Search for command by inputline.
     *
     * @param inputLine input String
     * @return Command that matches input
     * @throws ProtocolException if bad command name
     */
    public Command getCommand(String inputLine) throws ProtocolException {
        String[] elements = getElements(inputLine);
        String commandName = elements[0];
        if (!commandsByName.containsKey(commandName)) {
            throw new ProtocolException("Invalid command name");
        }
        Command command = commandsByName.get(commandName);
        command = giveData(elements, command);
        return command;
    }

    private String[] getElements(String userInput) {
        List<String> items = new ArrayList<String>();
        for (String word : userInput.split(" ")) {
            items.add(word.trim());
        }
        String[] array = new String[items.size()];
        array = items.toArray(array);
        return array;
    }

    private Command giveData(String[] userInput, Command command) {
        int index = 1;
        while (index < userInput.length) {
            String key = userInput[index];
            if (userInput[index].charAt(0) == '-') {
                command.setParameter(key, "");
                index++;
            } else {
                String value = userInput[index + 1];
                command.setParameter(key, value);
                index += 2;
            }
        }
        return command;
    }
}
