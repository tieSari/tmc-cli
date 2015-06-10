package hy.tmc.cli.frontend.communication.commands;

import com.google.common.base.Optional;
import hy.tmc.cli.backend.communication.ExerciseLister;
import hy.tmc.cli.configuration.ClientData;
import hy.tmc.cli.frontend.communication.server.ProtocolException;

public class ListExercises extends Command {

    private ExerciseLister lister;

    public ListExercises() {
        lister = new ExerciseLister();
    }

    /**
     * For dependency injection for tests.
     *
     * @param front
     * @param lister mocked lister object.
     */
    public ListExercises(ExerciseLister lister) {
        this.lister = lister;
    }

    /**
     * Get a list of the exercises of the course which the current directory belongs to.
     */
    @Override
    protected Optional<String> functionality() {
        return Optional.of(lister.listExercises(data.get("path")));
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
}
