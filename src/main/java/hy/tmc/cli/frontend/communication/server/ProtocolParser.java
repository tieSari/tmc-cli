package hy.tmc.cli.frontend.communication.server;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import hy.tmc.cli.frontend.communication.commands.ChooseServer;

import hy.tmc.cli.frontend.communication.commands.Command;
import hy.tmc.cli.frontend.communication.commands.Help;
import hy.tmc.core.exceptions.TmcCoreException;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * ProtocolParser parses user input to executable command.
 */
public class ProtocolParser {

    private Server server;
    private DataOutputStream stream;
    private Socket socket;
    private ListeningExecutorService pool;


    /**
     * Constructor for Protocol Parser.
     *
     * @param server frontend server
     */
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
    
    public ProtocolParser(DataOutputStream stream, Socket socket, ListeningExecutorService pool){
        this.stream = stream;
        this.socket = socket;
        this.pool = pool;
    }
    
    public HashMap<String, Command> createCommandMap(){
        HashMap<String, Command> map = new HashMap<String, Command>();
        map.put("help", new Help());
        map.put("setServer", new ChooseServer());
        return map;
    }

    /**
     * Search for command by inputline.
     *
     * @param inputLine input String
     * @return Command that matches input
     * @throws ProtocolException if bad command name
     */
    public ListenableFuture<?> getCommand(String inputLine) throws ProtocolException, TmcCoreException, IOException {
        String[] elements = getElements(inputLine);
        String commandName = elements[0];
        HashMap<String, String> params = giveData(elements, new HashMap<String, String>());
        HashMap<String, Command> commandMap = createCommandMap();
        ListenableFuture<?> result;
        CoreUser coreUser = new CoreUser(stream, socket, pool);
        if(commandMap.containsKey(commandName)){
            Command command = commandMap.get(commandName);
            result = MoreExecutors.sameThreadExecutor().submit(command);
        } else {
            result = coreUser.findAndExecute(commandName, params);
        }
        return result;
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
