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
        @SuppressWarnings("unchecked")
        ListenableFuture<String> stringListenableFuture = (ListenableFuture<String>) runCommand("login " + " username " + username + " password " + password);
        return stringListenableFuture;
    }

    public ListenableFuture<String> logout() throws ProtocolException {
        @SuppressWarnings("unchecked")
        ListenableFuture<String> logout = (ListenableFuture<String>) runCommand("logout");
        return logout;
    }

    public ListenableFuture<String> selectServer(String serverAddress) throws ProtocolException {
        @SuppressWarnings("unchecked")
        ListenableFuture<String> stringListenableFuture = (ListenableFuture<String>) runCommand("setServer " + " tmc-server " + serverAddress);
        return stringListenableFuture;
    }

    public ListenableFuture<String> downloadExercises(String pwd, String courseId) throws ProtocolException {
        @SuppressWarnings("unchecked")
        ListenableFuture<String> stringListenableFuture = (ListenableFuture<String>) runCommand("downloadExercises pwd " + pwd + " courseID " + courseId);
        return stringListenableFuture;
    }

    public ListenableFuture<String> help() throws ProtocolException {
        @SuppressWarnings("unchecked")
        ListenableFuture<String> help = (ListenableFuture<String>) runCommand("help");
        return help;
    }

    public ListenableFuture<String> listCourses() throws ProtocolException {
        @SuppressWarnings("unchecked")
        ListenableFuture<String> listCourses = (ListenableFuture<String>) runCommand("listCourses");
        return listCourses;
    }

    public ListenableFuture<String> listExercises() throws ProtocolException {
        @SuppressWarnings("unchecked")
        ListenableFuture<String> listExercises = (ListenableFuture<String>) runCommand("listExercises");
        return listExercises;
    }
    
    public ListenableFuture<SubmissionResult> submit(String path) throws ProtocolException {
        @SuppressWarnings("unchecked")
        ListenableFuture<SubmissionResult> submissionResultListenableFuture = (ListenableFuture<SubmissionResult>) runCommand("submit path " + path);
        return submissionResultListenableFuture;
    }
    
    public ListenableFuture<RunResult> test(String path) throws ProtocolException {
        @SuppressWarnings("unchecked")
        ListenableFuture<RunResult> runResultListenableFuture = (ListenableFuture<RunResult>) runCommand("runTests path " + path);
        return runResultListenableFuture;
    }
    
    public ListenableFuture<String> paste(String path) throws ProtocolException {
        @SuppressWarnings("unchecked")
        ListenableFuture<String> stringListenableFuture = (ListenableFuture<String>) runCommand("paste path " + path);
        return stringListenableFuture;
    }

    public ListenableFuture<?> runCommand(String inputLine) throws ProtocolException {
        @SuppressWarnings("unchecked")
        ListenableFuture submit = pool.submit(parser.getCommand(inputLine));
        return submit;
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
