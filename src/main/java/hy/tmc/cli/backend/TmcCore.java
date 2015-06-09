package hy.tmc.cli.backend;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import hy.tmc.cli.frontend.FrontendListener;
import hy.tmc.cli.frontend.communication.commands.Command;
import hy.tmc.cli.frontend.communication.commands.CommandFactory;
import hy.tmc.cli.synchronization.TmcServiceScheduler;
import java.util.Map;
import java.util.concurrent.Executors;

public class TmcCore {

    private Map<String, Command> commands;
    private TmcServiceScheduler scheduler;
    private ListeningExecutorService pool;

    /**
     * The TmcCore that can be used as a standalone businesslogic for any tmc client application.
     * The TmcCore provides all the essential backend functionalities as public methods.
     *
     * @param frontend the client frontend, that the core will communicate with.
     */
    public TmcCore(FrontendListener frontend) {
        scheduler = new TmcServiceScheduler();
        commands = CommandFactory.createCommandMap(frontend);
        pool = MoreExecutors.listeningDecorator(Executors.newWorkStealingPool());

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

    private ListenableFuture<String> run(String commandName, String... args) {
        if (!commands.containsKey(commandName)) {
            return null;
        }
        Command command = commands.get(commandName);
        setParams(command, args);
        //return pool.submit(command);
        return null;
    }

    private void setParams(Command command, String... args) {
        for (int i = 0, j = 1; j < args.length; i += 2, j += 2) {
            command.setParameter(args[i], args[j]);
        }
    }
}
