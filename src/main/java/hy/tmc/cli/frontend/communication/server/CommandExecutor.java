package hy.tmc.cli.frontend.communication.server;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import hy.tmc.cli.TmcCli;

import hy.tmc.cli.frontend.communication.commands.Command;
import hy.tmc.cli.frontend.communication.commands.Help;
import hy.tmc.cli.frontend.communication.commands.SetServer;
import hy.tmc.cli.listeners.DefaultListener;
import hy.tmc.core.exceptions.TmcCoreException;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class CommandExecutor {

    private DataOutputStream stream;
    private Socket socket;
    private ListeningExecutorService pool;
    private TmcCli cli;
    private ProtocolParser parser;

    public CommandExecutor(DataOutputStream stream, Socket socket, ListeningExecutorService pool, TmcCli cli) {
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
    public void parseAndExecute(String inputLine) throws ProtocolException, TmcCoreException, IOException, InterruptedException, ExecutionException {
        String[] elements = parser.getElements(inputLine);
        String commandName = elements[0];
        HashMap<String, String> params = parser.giveData(elements, new HashMap<String, String>());
        HashMap<String, Command> commandMap = createCommandMap(params);
        executeCommand(commandMap, commandName, params);
    }

    public HashMap<String, Command> createCommandMap(HashMap<String, String> params) {
        HashMap<String, Command> map = new HashMap<>();
        map.put("help", new Help(this.cli, params.get("command")));
        map.put("setServer", new SetServer(this.cli, params.get("tmc-server")));
        return map;
    }

    private void executeCommand(HashMap<String, Command> commandMap, String commandName, HashMap<String, String> params) throws ProtocolException, IOException, TmcCoreException, InterruptedException, ExecutionException {
        CoreUser coreUser = new CoreUser(cli, stream, socket, pool);
        if (commandMap.containsKey(commandName)) {
            Command command = commandMap.get(commandName);
            ListenableFuture<String> result = MoreExecutors.sameThreadExecutor().submit(command);
            DefaultListener listener = new DefaultListener(result, stream, socket);
            result.addListener(listener, pool);
        } else {
            try {
                coreUser.findAndExecute(commandName, params);
            } catch (ProtocolException ex) {
                stream.write((ex.getMessage()+"\n").getBytes());
                stream.close();
                socket.close();
            }
        }
    }
}
