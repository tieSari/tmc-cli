package hy.tmc.cli.frontend.communication.server;

import com.google.common.util.concurrent.ListenableFuture;
import hy.tmc.cli.frontend.communication.commands.Command;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * ProtocolParser parses user input to executable command.
 */
public class ProtocolParser {

    private Server server;


    /**
     * Constructor for Protocol Parser.
     *
     * @param server frontend server
     */
    public ProtocolParser(Server server) {
        this.server = server;
        this.coreUser = new CoreUser(new ClientTmcSettings());
    }

    public ProtocolParser() {
    }

    /**
     * Constructor for Protocol Parser.
     *
     * @param server frontend server
     */
    public ProtocolParser(Server server, HashMap<String, Command> availableCommands) {
        this.server = server;
    }

    /**
     * Search for command by inputline.
     *
     * @param inputLine input String
     * @return Command that matches input
     * @throws ProtocolException if bad command name
     */
    public ListenableFuture<?> getCommand(String inputLine) throws ProtocolException {
        String[] elements = getElements(inputLine);
        String commandName = elements[0];
        HashMap<String, String> params = giveData(elements, new HashMap<String, String>());
        return coreUser.findAndExecute(commandName, params);
    }

    private String[] getElements(String userInput) {
        List<String> items = new ArrayList<>();
        boolean parsingLongValue = false;
        String multiWordItem = "";
        for (String word : userInput.split(" ")) {
            if (parsingLongValue) {
                if (word.contains("}")) {
                    parsingLongValue = false;
                    items.add(multiWordItem.trim());
                    multiWordItem = "";
                } else {
                    multiWordItem += " " + word;
                }
            } else {
                if (word.contains("{")) {
                    parsingLongValue = true;
                } else {
                    items.add(word);
                }
            }
        }
        String[] array = new String[items.size()];
        array = items.toArray(array);
        return array;
    }

    private HashMap<String, String> giveData(String[] userInput, HashMap<String, String> params) {
        int index = 1;
        while (index < userInput.length) {
            String key = userInput[index];
            if (userInput[index].charAt(0) == '-') {
                params.put(key, "");
                index++;
            } else {
                String value = userInput[index + 1].replace("<newline>", "\n");
                params.put(key, value);
                index += 2;
            }
        }
        return params;
    }
}
