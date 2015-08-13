package hy.tmc.cli.listeners;

import com.google.common.base.Optional;
import com.google.common.util.concurrent.ListenableFuture;

import fi.helsinki.cs.tmc.core.domain.Course;

import hy.tmc.cli.backend.communication.ExerciseLister;

import java.io.DataOutputStream;
import java.net.Socket;

public class ListExercisesListener extends ResultListener<Course> {

    private ExerciseLister lister;
    private Course current;

    public ListExercisesListener(ListenableFuture<Course> commandResult, DataOutputStream output,
        Socket socket) {
        this(commandResult, output, socket, new ExerciseLister());
    }

    public ListExercisesListener(ListenableFuture<Course> commandResult, DataOutputStream output,
        Socket socket, ExerciseLister lister) {
        super(commandResult, output, socket);
        this.lister = lister;
    }

    @Override
    public Optional<String> parseData(Course result) {
        return Optional.of(lister.buildExercisesInfo(result.getExercises()));
    }

    @Override
    public void extraActions(Course exercises) {
    }
}
