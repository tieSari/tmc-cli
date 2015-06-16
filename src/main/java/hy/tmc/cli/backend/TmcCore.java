package hy.tmc.cli.backend;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import fi.helsinki.cs.tmc.langs.RunResult;
import hy.tmc.cli.domain.submission.SubmissionResult;
import hy.tmc.cli.frontend.communication.commands.Command;
import hy.tmc.cli.frontend.communication.commands.CommandFactory;
import hy.tmc.cli.frontend.communication.server.ProtocolException;
import hy.tmc.cli.frontend.communication.server.ProtocolParser;
import java.util.Map;
import java.util.concurrent.Callable;
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

    public ListenableFuture<String> login(String username, String password) throws ProtocolException {
        return (ListenableFuture<String>) runCommand("login " + " username " + username + " password " + password);
    }

    public ListenableFuture<String> logout() throws ProtocolException {
        return (ListenableFuture<String>) runCommand("logout");
    }

    public ListenableFuture<String> selectServer(String serverAddress) throws ProtocolException {
        return (ListenableFuture<String>) runCommand("setServer " + " tmc-server " + serverAddress);
    }

    public ListenableFuture<String> downloadExercises(String pwd, String courseId) throws ProtocolException {
        return (ListenableFuture<String>) runCommand("downloadExercises pwd "+ pwd+ " courseID " + courseId);
    }

    public ListenableFuture<String> help() throws ProtocolException {
        return (ListenableFuture<String>) runCommand("help");
    }

    public ListenableFuture<String> listCourses() throws ProtocolException {
        return (ListenableFuture<String>) runCommand("listCourses");
    }

    public ListenableFuture<String> listExercises() throws ProtocolException {
        return (ListenableFuture<String>) runCommand("listExercises");
    }
    
    public ListenableFuture<SubmissionResult> submit(String path) throws ProtocolException {
        return (ListenableFuture<SubmissionResult>) runCommand("submit path " + path);
    }
    
    public ListenableFuture<RunResult> test(String path) throws ProtocolException {
        return (ListenableFuture<RunResult>) runCommand("runTests path " + path);
    }
    
    public ListenableFuture<String> paste(String path) throws ProtocolException {
        return (ListenableFuture<String>) runCommand("paste path " + path);
    }

    public ListenableFuture<?> runCommand(String inputLine) throws ProtocolException {
        return pool.submit(parser.getCommand(inputLine));
    }

//    private ListenableFuture<?> run(String commandName, String... args) {
//        if (!commands.containsKey(commandName)) {
//            return null;
//        }
//        Command command = commands.get(commandName);
//        setParams(command, args);
//        return pool.submit(command);
//    }
    
    public Command getCommand(String inputLine) throws ProtocolException {
        return parser.getCommand(inputLine);
    }
    
    public ListenableFuture<?> submitTask(Callable<?> callable) {
        return pool.submit(callable);
    }

    private void setParams(Command command, String... args) {
        for (int i = 0, j = 1; j < args.length; i += 2, j += 2) {
            command.setParameter(args[i], args[j]);
        }
    }
}
