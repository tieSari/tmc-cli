package hy.tmc.cli.frontend.communication.server;

import static hy.tmc.cli.frontend.communication.commands.CommandFactory.createCommandMap;

import hy.tmc.cli.frontend.FrontendListener;
import hy.tmc.cli.frontend.communication.commands.Command;

import java.util.HashMap;
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
        this.commandsByName = createCommandMap(this.server);
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
        int i = 1;
        while(i < userInput.length){
            String key = userInput[i];
            System.out.println("key: " + key);
            if(userInput[i].charAt(0) == '-'){
                command.setParameter(key, "");
                i++;
            } else {
                String value = userInput[i + 1];
                command.setParameter(key, value);
                System.out.println("Asetetaan: " + key + " arvoon " + value);
                i+= 2;
            }
        }
        return command;
    }
}
