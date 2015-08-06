package hy.tmc.cli.listeners;

import com.google.common.base.Optional;
import com.google.common.util.concurrent.ListenableFuture;
import hy.tmc.cli.TmcCli;
import hy.tmc.core.domain.Exercise;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UpdateDownloadingListener extends ResultListener<List<Exercise>> {

    TmcCli cli;

    public UpdateDownloadingListener(TmcCli cli, ListenableFuture commandResult, DataOutputStream output, Socket socket) {
        super(commandResult, output, socket);
        this.cli = cli;
    }

    @Override
    protected Optional<String> parseData(List<Exercise> result) {
        return Optional.of(result.size() + " updates downloaded");
    }

    @Override
    protected void extraActions(List<Exercise> result) {
        if (result.isEmpty()) {
            return;
        }
        try {
            cli.refreshLastUpdate();
        } catch (IOException ex) {
            Logger.getLogger(UpdateDownloadingListener.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
