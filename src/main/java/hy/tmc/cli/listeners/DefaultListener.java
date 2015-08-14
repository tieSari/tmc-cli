package hy.tmc.cli.listeners;

import com.google.common.base.Optional;
import com.google.common.util.concurrent.ListenableFuture;

import java.io.DataOutputStream;
import java.net.Socket;

public class DefaultListener extends ResultListener<String> {

    public DefaultListener(ListenableFuture<String> commandResult, DataOutputStream output,
        Socket socket) {
        super(commandResult, output, socket);
    }

    @Override
    protected Optional<String> parseData(String result) {
        if (result == null || result.isEmpty()) {
            return Optional.absent();
        }
        return Optional.of(result);
    }

    @Override
    protected void extraActions(String result) {
    }
}
