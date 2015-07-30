package hy.tmc.cli.listeners;

import com.google.common.base.Optional;
import com.google.common.util.concurrent.ListenableFuture;
import fi.helsinki.cs.tmc.langs.domain.RunResult;
import hy.tmc.cli.frontend.ResultInterpreter;
import hy.tmc.cli.frontend.formatters.DefaultTestResultFormatter;
import hy.tmc.cli.frontend.formatters.TestResultFormatter;
import java.io.DataOutputStream;
import java.net.Socket;

public class TestsListener extends ResultListener<RunResult> {

    private TestResultFormatter formatter;
    private boolean showStackStrace;

    public TestsListener(ListenableFuture<RunResult> commandResult,
            DataOutputStream output, Socket socket) {
        this(commandResult, output, socket, new DefaultTestResultFormatter(), false, false);
    }

    public TestsListener(ListenableFuture<RunResult> commandResult,
            DataOutputStream output, Socket socket, boolean verbose) {
        this(commandResult, output, socket, new DefaultTestResultFormatter(), verbose, false);
    }

    public TestsListener(ListenableFuture<RunResult> commandResult,
            DataOutputStream output, Socket socket, TestResultFormatter formatter) {
        this(commandResult, output, socket, formatter, false, false);
    }
    
    public TestsListener(ListenableFuture<RunResult> commandResult,
            DataOutputStream output, Socket socket,
            TestResultFormatter formatter, boolean verbose, boolean leaveSocketOpen) {
        super(commandResult, output, socket, leaveSocketOpen);
        this.formatter = formatter;
        this.showStackStrace = verbose;
    }

    @Override
    public Optional<String> parseData(RunResult result) {
        ResultInterpreter resInt = new ResultInterpreter(result, formatter);
        return Optional.of(resInt.interpret(showStackStrace));
    }

    @Override
    public void extraActions(RunResult result) {
    }
}
