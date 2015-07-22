package hy.tmc.cli.listeners;

import com.google.common.base.Optional;
import com.google.common.util.concurrent.ListenableFuture;
import hy.tmc.cli.backend.communication.ExerciseLister;
import hy.tmc.core.domain.Course;
import hy.tmc.core.domain.Exercise;
import java.io.DataOutputStream;
import java.net.Socket;

import java.util.List;

public class ListExercisesListener extends ResultListener<List<Exercise>> {

    private ExerciseLister lister;
    private Course current;

    public ListExercisesListener(ListenableFuture<List<Exercise>> commandResult, DataOutputStream output, Socket socket) {
        this(commandResult, output, socket, new ExerciseLister());
    }
    
    public ListExercisesListener(ListenableFuture<List<Exercise>> commandResult, DataOutputStream output, Socket socket, ExerciseLister lister) {
        super(commandResult, output, socket);
        this.lister = lister;
    }

    @Override
    public Optional<String> parseData(List<Exercise> result) {
        return Optional.of(lister.buildExercisesInfo(result));
    }

    @Override
    public void extraActions(List<Exercise> exercises) {
    }
}
