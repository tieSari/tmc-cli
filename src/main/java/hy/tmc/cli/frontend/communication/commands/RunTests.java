package hy.tmc.cli.frontend.communication.commands;

import com.google.common.base.Optional;
import fi.helsinki.cs.tmc.langs.domain.NoLanguagePluginFoundException;
import fi.helsinki.cs.tmc.langs.domain.RunResult;
import fi.helsinki.cs.tmc.langs.util.TaskExecutorImpl;
import hy.tmc.cli.frontend.ResultInterpreter;
import hy.tmc.cli.frontend.communication.server.ProtocolException;
import hy.tmc.cli.frontend.formatters.CommandLineTestResultFormatter;
import hy.tmc.cli.frontend.formatters.TestResultFormatter;
import hy.tmc.cli.frontend.formatters.VimTestResultFormatter;
import java.nio.file.Path;
import java.nio.file.Paths;

public class RunTests extends Command<RunResult> {

    private TestResultFormatter formatter;

    public RunTests(String path) {
        this.setParameter("path", path);
    }
    
    public RunTests(){
        
    }

    private TestResultFormatter getFormatter() {
        if (data.containsKey("--vim")) {
            return new VimTestResultFormatter();
        } else {
            return new CommandLineTestResultFormatter();
        }
    }

    @Override
    public Optional<String> parseData(Object data) {
        RunResult result = (RunResult) data;
        boolean showStackTrace = this.data.containsKey("verbose");
        ResultInterpreter resInt = new ResultInterpreter(result, formatter);
        return Optional.of(resInt.interpret(showStackTrace));

    }
}
