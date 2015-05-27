/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hy.tmc.cli.frontend.communication.commands;

import fi.helsinki.cs.tmc.langs.NoLanguagePluginFoundException;
import fi.helsinki.cs.tmc.langs.RunResult;
import fi.helsinki.cs.tmc.langs.TestResult;
import fi.helsinki.cs.tmc.langs.util.TaskExecutorImpl;
import fi.helsinki.cs.tmc.stylerunner.validation.ValidationResult;
import hy.tmc.cli.frontend.FrontendListener;
import hy.tmc.cli.frontend.ResultInterpreter;
import hy.tmc.cli.frontend.communication.server.ProtocolException;
import hy.tmc.cli.logic.Logic;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RunTests extends Command {

    public RunTests(FrontendListener front, Logic backend) {
        super(front, backend);
    }

    @Override
    protected void functionality() {
        String path = this.data.get("filepath");
        Path p = Paths.get(path);
        try {
            runTests(p);
        } catch (NoLanguagePluginFoundException ex) {
            this.frontend.printLine("Not an exercise.");
            return;
        }
    }

    public void runTests(Path p) throws NoLanguagePluginFoundException {
        TaskExecutorImpl taskExecutor = new TaskExecutorImpl();
        RunResult result = taskExecutor.runTests(p);
        ValidationResult validationResult = taskExecutor;
        
        ResultInterpreter resInt = new ResultInterpreter();
        String res = resInt.interpret(result);
        
        this.frontend.printLine(res);
    }

    @Override
    public void checkData() throws ProtocolException {
        if (!this.data.containsKey("filepath")) {
            throw new ProtocolException("File path to exercise required.");
        }
    }

}
