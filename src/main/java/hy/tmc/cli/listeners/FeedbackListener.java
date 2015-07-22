package hy.tmc.cli.listeners;

import com.google.common.base.Optional;
import com.google.common.util.concurrent.ListenableFuture;
import hy.tmc.core.communication.HttpResult;
import java.io.DataOutputStream;
import java.net.Socket;

public class FeedbackListener extends ResultListener<HttpResult> {

    public FeedbackListener(ListenableFuture<HttpResult> commandResult, DataOutputStream output, Socket socket) {
        super(commandResult, output, socket);
    }

    @Override
    protected Optional<String> parseData(HttpResult result) {
        if (result.isSuccess()) {
            if (result.getData().contains("status:ok")) {
                return Optional.of("Your feedback was received succesfully. Thank you for your answers.");
            } else {
                return Optional.of("Sending the feedback failed: " + result.getData());
            }
        }
        return Optional.of("Error " + result.getStatusCode() + " Sending the feedback failed.");
    }

    @Override
    protected void extraActions(HttpResult result) {
    }

}
