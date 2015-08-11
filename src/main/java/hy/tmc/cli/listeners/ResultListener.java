package hy.tmc.cli.listeners;

import com.google.common.base.Optional;
import com.google.common.util.concurrent.ListenableFuture;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;

public abstract class ResultListener<T> implements Runnable {

    private ListenableFuture<T> commandResult;
    private DataOutputStream output;
    private Socket socket;
    private boolean leaveSocketOpen;

    public ResultListener(ListenableFuture<T> commandResult, DataOutputStream output, Socket socket, boolean leaveSocketOpen) {
        this.commandResult = commandResult;
        this.output = output;
        this.socket = socket;
        this.leaveSocketOpen = leaveSocketOpen;
    }

    public ResultListener(ListenableFuture<T> commandResult, DataOutputStream output, Socket socket) {
        this(commandResult, output, socket, false);
    }

    /**
     * Creates an output message for the user based on the result from tmc-core. Each action will
     * have it's own listener that parser the result.
     *
     * @param result the result of running an action e.g. submit
     * @return output to be shown to the user.
     */
    protected abstract Optional<String> parseData(T result);

    /**
     * Perform any actions needed after tmc-core is finished, other than showing output to the user.
     *
     * @param result result running an action e.g. submit
     */
    protected abstract void extraActions(T result);

    @Override
    public void run() {
        try {
            T result = commandResult.get();
            extraActions(result);
            Optional<String> outputToUser = this.parseData(result);
            if (outputToUser.isPresent()) {
                System.err.println("Output for user is present.");
                writeToOutput(outputToUser.get());
            } else {
                System.err.println("Output for user is not present");
            }
        }
        catch (InterruptedException | ExecutionException ex) {
            System.err.println(Arrays.toString(ex.getStackTrace()));
            if (ex.getCause().getClass() == UnknownHostException.class) {
                writeToOutput("Unable to reach server: ");
            }
            writeToOutput(ex.getCause().getMessage());
            printLog(ex);
        }
    }

    /**
     * printLog prints exception to scripts/log.txt
     *
     * @param ex exception which can be any exception
     */
    private void printLog(Exception ex) {
        System.err.println(ex.getMessage() + "\n" + Arrays.toString(ex.getCause().getStackTrace()) + "\n" + Arrays.toString(ex.getStackTrace()));
    }

    private void writeToOutput(final String commandOutput) {
        try {
            byte[] bytes = (commandOutput + "\n").getBytes();
            output.write(bytes);
            if (!leaveSocketOpen) {
                socket.close();
            }
        }
        catch (IOException ex) {
            System.err.println("Failed to print error message: ");
            System.err.println(ex.getMessage());
        }
    }
}
