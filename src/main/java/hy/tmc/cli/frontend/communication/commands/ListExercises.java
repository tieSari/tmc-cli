package hy.tmc.cli.frontend.communication.commands;

import hy.tmc.cli.backendcommunication.TmcJsonParser;
import hy.tmc.cli.configuration.ClientData;
import hy.tmc.cli.frontend.FrontendListener;
import hy.tmc.cli.frontend.communication.server.ProtocolException;
import hy.tmc.cli.logic.Logic;

public class ListExercises extends Command {

    public ListExercises(FrontendListener front, Logic backend) {
        super(front, backend);
    }

    /**
     * use JSONParser to get a list of exercises names, and print it.
     */
    @Override
    protected void functionality() {
        this.frontend.printLine(TmcJsonParser.getExerciseNames(data.get("courseUrl")));
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
