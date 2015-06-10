package hy.tmc.cli.frontend.communication.commands;

import com.google.common.base.Optional;
import fi.helsinki.cs.tmc.langs.NoLanguagePluginFoundException;
import fi.helsinki.cs.tmc.langs.RunResult;
import fi.helsinki.cs.tmc.langs.util.TaskExecutorImpl;
import hy.tmc.cli.backend.communication.StatusPoller;
import hy.tmc.cli.configuration.ClientData;
import hy.tmc.cli.frontend.FrontendListener;
import hy.tmc.cli.frontend.ResultInterpreter;
import hy.tmc.cli.frontend.communication.server.ProtocolException;

import hy.tmc.cli.frontend.formatters.CommandLineTestResultFormatter;
import hy.tmc.cli.synchronization.TmcServiceScheduler;
import hy.tmc.cli.zipping.DefaultRootDetector;
import hy.tmc.cli.zipping.ProjectRootFinder;

import java.nio.file.Path;
import java.nio.file.Paths;

public class RunTests extends MailCheckingCommand {

    public RunTests(FrontendListener front) {
        super(front);
    }

    @Override
    protected void functionality() {
        String path = this.data.get("filepath");
        ProjectRootFinder finder = new ProjectRootFinder(new DefaultRootDetector());
        Optional<Path> exercise = finder.getRootDirectory(Paths.get(path));
        if (!ClientData.isPolling()) {
            new TmcServiceScheduler().addService(new StatusPoller(data.get("filepath"))).start();
        }

        if (!exercise.isPresent()) {
            this.frontend.printLine("Not an exercise. (null)");
            return;
        }
        try {
            runTests(exercise.get());
        }
        catch (NoLanguagePluginFoundException ex) {
            this.frontend.printLine("Not an exercise.");
        }
    }

    /**
     * Runs tests for exercise.
     *
     * @param exercise Path object
     * @throws NoLanguagePluginFoundException if path doesn't contain exercise
     */
    public void runTests(Path exercise) throws NoLanguagePluginFoundException {
        TaskExecutorImpl taskExecutor = new TaskExecutorImpl();
        RunResult result = taskExecutor.runTests(exercise);

        boolean showStackTrace = this.data.containsKey("verbose");
        CommandLineTestResultFormatter formatter = new CommandLineTestResultFormatter();
        ResultInterpreter resInt = new ResultInterpreter(result, formatter);
        String res = resInt.interpret(showStackTrace);

        this.frontend.printLine(res);
    }

    @Override
    public void checkData() throws ProtocolException {
        if (!this.data.containsKey("filepath")) {
            throw new ProtocolException("File path to exercise required.");
        }
    }
}
