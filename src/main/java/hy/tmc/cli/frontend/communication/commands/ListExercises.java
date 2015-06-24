package hy.tmc.cli.frontend.communication.commands;

import com.google.common.base.Optional;
import hy.tmc.cli.backend.communication.ExerciseLister;
import hy.tmc.cli.configuration.ClientData;
import hy.tmc.cli.domain.Exercise;
import hy.tmc.cli.frontend.communication.server.ProtocolException;
import java.util.List;

public class ListExercises extends Command<List<Exercise>> {

    private ExerciseLister lister;

    public ListExercises() {
        lister = new ExerciseLister();
    }

    /**
     * For dependency injection for tests.
     *
     * @param lister mocked lister object.
     */
    public ListExercises(ExerciseLister lister) {
        this.lister = lister;
    }

    /**
     * Check the path and ClientData.
     *
     * @throws ProtocolException if some data not specified
     */
    @Override
    public void checkData() throws ProtocolException {
        if (!data.containsKey("path")) {
            throw new ProtocolException("Path not recieved");
        }
        if (!ClientData.userDataExists()) {
            throw new ProtocolException("Please authorize first.");
        }
    }

    @Override
    public Optional<String> parseData(Object data) {
        @SuppressWarnings("unchecked")
        List<Exercise> result = (List<Exercise>) data;

        return Optional.of(lister.buildExercisesInfo(result));
    }

    @Override
    public List<Exercise> call() throws ProtocolException {
        checkData();
        return lister.listExercises(data.get("path"));
    }
}
