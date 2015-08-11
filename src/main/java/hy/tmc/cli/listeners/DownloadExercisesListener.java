package hy.tmc.cli.listeners;

import com.google.common.base.Optional;
import com.google.common.util.concurrent.ListenableFuture;
import fi.helsinki.cs.tmc.core.domain.Exercise;

import java.io.DataOutputStream;
import java.net.Socket;
import java.util.List;

public class DownloadExercisesListener extends ResultListener<List<Exercise>> {
    public DownloadExercisesListener(ListenableFuture<List<Exercise>> exercisesFuture, DataOutputStream output, Socket socket) {
        super(exercisesFuture, output, socket);
    }

    @Override
    protected Optional<String> parseData(List<Exercise> result) {
        return Optional.of("Downloaded " + result.size() + " exercises.");
    }

    @Override
    protected void extraActions(List<Exercise> result) {
    }
}
