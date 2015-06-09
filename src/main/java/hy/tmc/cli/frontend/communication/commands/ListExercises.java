package hy.tmc.cli.frontend.communication.commands;

import com.google.common.base.Optional;
import hy.tmc.cli.backend.communication.TmcJsonParser;
import hy.tmc.cli.configuration.ClientData;
import hy.tmc.cli.frontend.FrontendListener;
import hy.tmc.cli.frontend.communication.server.ProtocolException;

public class ListExercises extends Command {

    /**
     * use JSONParser to get a list of exercises names, and print it.
     */
    @Override
    protected Optional<String> functionality() {
        return Optional.of(TmcJsonParser.getExerciseNames(data.get("courseUrl")));
    }

    /**
     * Check the courseUrl and ClientData.
     *
     * @throws ProtocolException if some data not specified
     */
    @Override
    public void checkData() throws ProtocolException {
        if (!data.containsKey("courseUrl")) {
            throw new ProtocolException("Specify course url");
        }
        if (!ClientData.userDataExists()) {
            throw new ProtocolException("Please authorize first.");
        }
    }
}
