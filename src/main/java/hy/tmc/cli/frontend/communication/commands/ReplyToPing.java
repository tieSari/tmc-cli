package hy.tmc.cli.frontend.communication.commands;

import com.google.common.base.Optional;
import java.util.concurrent.Callable;

public class ReplyToPing extends Command implements Callable<String> {

    private final String answer = "pong";

    /**
     * print pong to the frontend.
     */
    protected Optional<String> functionality() {
        return Optional.of(answer);
    }

    /**
     * Does nothing, this command requires no data.
     */
    @Override
    public void checkData() {
    }

    @Override
    public String call() throws Exception {
        return functionality().or("ping failed");
    }

    @Override
    public Optional<String> parseData(Object data) {
        String response = (String) data;
        return Optional.of(response);
    }
}
