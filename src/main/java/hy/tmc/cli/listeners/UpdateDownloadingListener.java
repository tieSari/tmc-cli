package hy.tmc.cli.listeners;

import com.google.common.base.Optional;
import com.google.common.util.concurrent.ListenableFuture;
import hy.tmc.core.domain.Exercise;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.List;


public class UpdateDownloadingListener extends ResultListener<List<Exercise>> {

    public UpdateDownloadingListener(ListenableFuture commandResult, DataOutputStream output, Socket socket) {
        super(commandResult, output, socket);
    }
    
    @Override
    protected Optional<String> parseData(List<Exercise> result) {
        return Optional.of(result.size() + " updates downloaded");
    }

    @Override
    protected void extraActions(List<Exercise> result) {
    }

}
