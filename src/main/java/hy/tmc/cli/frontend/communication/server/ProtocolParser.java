package hy.tmc.cli.frontend.communication.server;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import hy.tmc.cli.TmcCli;

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
    private TmcCli cli;

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
    
    public ProtocolParser(DataOutputStream stream, Socket socket, ListeningExecutorService pool, TmcCli cli){
        this.stream = stream;
        this.socket = socket;
        this.pool = pool;
        this.cli = cli;
    }
    
    public HashMap<String, Command> createCommandMap(){
        HashMap<String, Command> map = new HashMap<String, Command>();
        map.put("help", new Help(this.cli));
        //map.put("setServer", new ChooseServer());
        return map;
    }

    /**
     * Search for command by inputline.
     *
     * @param inputLine input String
     * @return Command that matches input
     * @throws ProtocolException if bad command name
     */
    public void getCommand(String inputLine) throws ProtocolException, TmcCoreException, IOException {
        String[] elements = getElements(inputLine);
        String commandName = elements[0];
        HashMap<String, String> params = giveData(elements, new HashMap<String, String>());
        HashMap<String, Command> commandMap = createCommandMap();
        executeCommand(commandMap, commandName, params);
        socket.close();
    }

    private void executeCommand(HashMap<String, Command> commandMap, String commandName, HashMap<String, String> params) throws ProtocolException, IOException, TmcCoreException {
        ListenableFuture<?> result;
        CoreUser coreUser = new CoreUser(cli, stream, socket, pool);
        if(commandMap.containsKey(commandName)){
            System.err.println("Ei kuulu corelle.");
            Command command = commandMap.get(commandName);
            result = MoreExecutors.sameThreadExecutor().submit(command);
        } else {
            System.err.println("kuuluu corelle");
            coreUser.findAndExecute(commandName, params);
        }
    }
    
    
    private String[] getElements(String userInput) {
        List<String> items = new ArrayList<>();
        boolean parsingLongValue = false;
        String multiWordItem = "";
        for (String word : userInput.split(" ")) {
            handleSingleWord(parsingLongValue, word, items, multiWordItem);
        }
        String[] array = new String[items.size()];
        array = items.toArray(array);
        return array;
    }

    private void handleSingleWord(boolean parsingLongValue, String word, List<String> items, String multiWordItem) {
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
