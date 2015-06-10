package hy.tmc.cli.frontend.communication.commands;

import hy.tmc.cli.backend.communication.ExerciseLister;
import hy.tmc.cli.backend.communication.StatusPoller;
import hy.tmc.cli.configuration.ClientData;
import hy.tmc.cli.frontend.FrontendListener;
import hy.tmc.cli.frontend.communication.server.ProtocolException;
import hy.tmc.cli.synchronization.TmcServiceScheduler;

public class ListExercises extends MailCheckingCommand {

    private ExerciseLister lister;

    public ListExercises(FrontendListener front) {
        super(front);
        lister = new ExerciseLister();
    }

    /**
     * For dependency injection for tests.
     *
     * @param front
     * @param lister mocked lister object.
     */
    public ListExercises(FrontendListener front, ExerciseLister lister) {
        super(front);
        this.lister = lister;
    }

    /**
     * Get a list of the exercises of the course which the current directory
     * belongs to.
     */
    @Override
    protected void functionality() {
        if (!ClientData.isPolling()) {
            this.frontend.printLine("Started polling. <devmsg>");
            new TmcServiceScheduler().addService(new StatusPoller(data.get("path"))).start();
        } else {
            this.frontend.printLine("Polling in progress. <devmsg>");
        }
        this.frontend.printLine(lister.listExercises(data.get("path")));
        
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
