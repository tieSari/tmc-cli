package hy.tmc.cli.backend;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import hy.tmc.cli.frontend.communication.commands.Command;
import hy.tmc.cli.frontend.communication.commands.CommandFactory;
import hy.tmc.cli.frontend.communication.server.ProtocolException;
import hy.tmc.cli.frontend.communication.server.ProtocolParser;
import java.util.Map;
import java.util.concurrent.Executors;

public class TmcCore {

    private Map<String, Command> commands;
    private ListeningExecutorService pool;
    private ProtocolParser parser = new ProtocolParser();

    /**
     * The TmcCore that can be used as a standalone businesslogic for any tmc client application.
     * The TmcCore provides all the essential backend functionalities as public methods.
     */
    public TmcCore() {
        commands = CommandFactory.createCommandMap();
        pool = MoreExecutors.listeningDecorator(Executors.newCachedThreadPool());
    }

    /**
     * For dependency injection of pool.
     *
     * @param pool
     */
    public TmcCore(ListeningExecutorService pool) {
        commands = CommandFactory.createCommandMap();
        this.pool = pool;
    }
    
    public ListeningExecutorService getPool() {
        return pool;
    }

    public ListenableFuture<String> login(String username, String password) {
        return run("login", "username", username, "password", password);
    }

    public ListenableFuture<String> logout() {
        return run("logout");
    }

    public ListenableFuture<String> selectServer(String serverAddress) {
        return run("setServer", "tmc-server", serverAddress);
    }

    public ListenableFuture<String> downloadExercises(String pwd, String courseId) {
        return run("downloadExercises", "pwd", pwd, "courseID", courseId);
    }

    public ListenableFuture<String> help() {
        return run("help");
    }

    public ListenableFuture<String> listCourses() {
        return run("listCourses");
    }

    public ListenableFuture<String> listExercises() {
        return run("listExercises");
    }

    public ListenableFuture<String> runCommand(String inputLine) throws ProtocolException {
        return pool.submit(parser.getCommand(inputLine));
    }

    private ListenableFuture<String> run(String commandName, String... args) {
        if (!commands.containsKey(commandName)) {
            return null;
        }
        Command command = commands.get(commandName);
        setParams(command, args);
        return pool.submit(command);
    }

    private void setParams(Command command, String... args) {
        for (int i = 0, j = 1; j < args.length; i += 2, j += 2) {
            command.setParameter(args[i], args[j]);
        }
    }
}
