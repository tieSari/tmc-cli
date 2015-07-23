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


public class CommandExecutor {

    private DataOutputStream stream;
    private Socket socket;
    private ListeningExecutorService pool;
    private TmcCli cli;
    private ProtocolParser parser;
    
    public CommandExecutor(DataOutputStream stream, Socket socket, ListeningExecutorService pool, TmcCli cli){
        this.stream = stream;
        this.socket = socket;
        this.pool = pool;
        this.cli = cli;
        this.parser = new ProtocolParser();
    }

    /**
     * Search for command by inputline.
     *
     * @param inputLine input String
     */
    public void parseAndExecute(String inputLine) throws ProtocolException, TmcCoreException, IOException {
        String[] elements = parser.getElements(inputLine);
        String commandName = elements[0];
        HashMap<String, String> params = parser.giveData(elements, new HashMap<String, String>());
        HashMap<String, Command> commandMap = createCommandMap();
        executeCommand(commandMap, commandName, params);
    }

    public HashMap<String, Command> createCommandMap(){
        HashMap<String, Command> map = new HashMap<String, Command>();
        map.put("help", new Help(this.cli));
        //map.put("setServer", new ChooseServer());
        return map;
    }

    private void executeCommand(HashMap<String, Command> commandMap, String commandName, HashMap<String, String> params) throws ProtocolException, IOException, TmcCoreException {
        CoreUser coreUser = new CoreUser(cli, stream, socket, pool);
        if(commandMap.containsKey(commandName)){
            Command command = commandMap.get(commandName);
            ListenableFuture<?> result = MoreExecutors.sameThreadExecutor().submit(command);
        } else {
            coreUser.findAndExecute(commandName, params);
        }
    }
}
