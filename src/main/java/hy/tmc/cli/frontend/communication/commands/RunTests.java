package hy.tmc.cli.frontend.communication.commands;

import fi.helsinki.cs.tmc.langs.NoLanguagePluginFoundException;
import fi.helsinki.cs.tmc.langs.RunResult;
import fi.helsinki.cs.tmc.langs.util.TaskExecutorImpl;

import hy.tmc.cli.frontend.FrontendListener;
import hy.tmc.cli.frontend.ResultInterpreter;
import hy.tmc.cli.frontend.communication.server.ProtocolException;


import java.nio.file.Path;
import java.nio.file.Paths;

public class RunTests extends Command {

    public RunTests(FrontendListener front) {
        super(front);
    }

    @Override
    protected void functionality() {
        String path = this.data.get("filepath");
        Path exercise = Paths.get(path);
        try {
            runTests(exercise);
        } catch (NoLanguagePluginFoundException ex) {
            this.frontend.printLine("Not an exercise.");
        }
    }

    /**
     * Runs tests for exercise.
     * @param exercise Path object
     * @throws NoLanguagePluginFoundException if path doesn't contain exercise
     */
    public void runTests(Path exercise) throws NoLanguagePluginFoundException {
        TaskExecutorImpl taskExecutor = new TaskExecutorImpl();
        RunResult result = taskExecutor.runTests(exercise);
        
        ResultInterpreter resInt = new ResultInterpreter(result);
        String res = resInt.interpret();
        
        this.frontend.printLine(res);
    }

    @Override
    public void checkData() throws ProtocolException {
        if (!this.data.containsKey("filepath")) {
            throw new ProtocolException("File path to exercise required.");
        }
    }

}
