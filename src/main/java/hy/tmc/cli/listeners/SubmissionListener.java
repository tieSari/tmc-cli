package hy.tmc.cli.listeners;

import com.google.common.base.Optional;
import com.google.common.util.concurrent.ListenableFuture;

import fi.helsinki.cs.tmc.core.domain.submission.SubmissionResult;

import hy.tmc.cli.backend.communication.SubmissionInterpreter;
import hy.tmc.cli.frontend.formatters.SubmissionResultFormatter;

import java.io.DataOutputStream;
import java.net.Socket;

/**
 * Submit command for submitting exercises to TMC.
 */
public class SubmissionListener extends ResultListener<SubmissionResult> {

    private SubmissionInterpreter interpreter;
    private boolean detailed;

    public SubmissionListener(ListenableFuture<SubmissionResult> commandResult,
        DataOutputStream output, Socket socket, SubmissionResultFormatter formatter) {
        this(commandResult, output, socket, new SubmissionInterpreter(formatter));
    }

    public SubmissionListener(ListenableFuture<SubmissionResult> commandResult,
        DataOutputStream output, Socket socket, boolean detailed,
        SubmissionResultFormatter formatter) {
        this(commandResult, output, socket, formatter);
        this.detailed = detailed;
    }

    public SubmissionListener(ListenableFuture<SubmissionResult> commandResult,
        DataOutputStream output, Socket socket, SubmissionInterpreter interpreter) {
        this(commandResult, output, socket, interpreter, false);
    }

    public SubmissionListener(ListenableFuture<SubmissionResult> commandResult,
        DataOutputStream output, Socket socket, SubmissionInterpreter interpreter,
        boolean detailed) {
        super(commandResult, output, socket);
        this.detailed = detailed;
        this.interpreter = interpreter;
    }

    @Override
    public Optional<String> parseData(SubmissionResult result) {
        return Optional.of(interpreter.summary(result, detailed));
    }

    @Override
    public void extraActions(SubmissionResult result) {
    }
}
