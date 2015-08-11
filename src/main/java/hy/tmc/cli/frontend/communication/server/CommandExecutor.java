package hy.tmc.cli.frontend.communication.server;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import hy.tmc.cli.CliSettings;
import hy.tmc.cli.TmcCli;

import hy.tmc.cli.frontend.communication.commands.*;
import hy.tmc.cli.listeners.DefaultListener;
import fi.helsinki.cs.tmc.core.domain.Course;
import fi.helsinki.cs.tmc.core.domain.Exercise;
import fi.helsinki.cs.tmc.core.exceptions.TmcCoreException;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.text.ParseException;
import java.util.Date;

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

    public CommandExecutor(TmcCli cli) {
        this.cli = cli;
    }

    /**
     * Search for command by inputline.
     *
     * @param inputLine input String
     */
    public void parseAndExecute(String inputLine) throws ProtocolException, TmcCoreException, IOException, InterruptedException, ExecutionException, IllegalStateException, ParseException {
        if (this.cli.makeUpdate()) {
            String msg;
            try {
                CliSettings settings = this.cli.defaultSettings();
                if (settings.getCurrentCourse().isPresent()) {
                    msg = checkUpdates(settings);
                } else {
                    msg = "Set your current course to get updates, type tmc set course <course id>\n";
                }
            }
            catch (IllegalStateException ex) {
                msg = "Could not check for updates, server address not set\n";
            }
            this.stream.write(msg.getBytes());
        }
        String[] elements = parser.getElements(inputLine);
        String commandName = elements[0];
        HashMap<String, String> params = parser.giveData(elements, new HashMap<String, String>());
        HashMap<String, Command> commandMap = createCommandMap(params);
        executeCommand(commandMap, commandName, params);
    }

    public String checkUpdates(CliSettings settings) throws TmcCoreException, IOException, InterruptedException, ParseException, ExecutionException {
        int pollInterval = 30;
        Date current = new Date();
        Date lastUpdate = settings.getLastUpdate();
        double mins = (current.getTime() - lastUpdate.getTime()) / (60 * 1000);
        String value = "";
        if (mins > pollInterval) {
            ListenableFuture<List<Exercise>> updates = this.cli.getCore().getNewAndUpdatedExercises(settings.getCurrentCourse().or(new Course()), settings);
            List<Exercise> exercises = updates.get();
            if (exercises.isEmpty()) {
                return "No updates available.\n";
            } else {
                return "Updates available. Type tmc update to download exercises.\n";
            }
        } else {
            return "";
        }
    }

    public HashMap<String, Command> createCommandMap(HashMap<String, String> params) {
        HashMap<String, Command> map = new HashMap<>();
        map.put("help", new Help(this.cli, params.get("command")));
        map.put("setServer", new SetServer(this.cli, params.get("tmc-server")));
        map.put("setCourse", new SetCourse(this.cli, params.get("course")));
        map.put("showSettings", new ShowSettings(this.cli));
        return map;
    }

    private void executeCommand(HashMap<String, Command> commandMap, String commandName, HashMap<String, String> params) throws ProtocolException, IOException, TmcCoreException, InterruptedException, ExecutionException, IllegalStateException {
        CoreUser coreUser = new CoreUser(cli, stream, socket, pool);
        if (commandMap.containsKey(commandName)) {
            Command command = commandMap.get(commandName);
            ListenableFuture<String> result = MoreExecutors.sameThreadExecutor().submit(command);
            DefaultListener listener = new DefaultListener(result, stream, socket);
            result.addListener(listener, pool);
        } else {
            try {
                coreUser.findAndExecute(commandName, params);
            }
            catch (ProtocolException | ParseException | TmcCoreException ex) {
                stream.write((ex.getMessage() + "\n").getBytes());
                stream.close();
                socket.close();
            }
        }
    }
}
