package hy.tmc.cli.frontend_communication.Commands;

import hy.tmc.cli.frontend_communication.FrontendListener;
import hy.tmc.cli.frontend_communication.Server.ProtocolException;
import hy.tmc.cli.logic.Logic;
import java.util.HashMap;
import java.util.Map;

public abstract class Command {

    /**
     * The frontend that this command responds to
     */
    protected final FrontendListener frontend;

    /**
     * The backend logic this command calls 
     */
    protected final Logic backend;

    
    protected Map<String, String> data;
    
    
    /**
     * Constructor sets frontend and backend
     * @param front
     * @param backend
     */
    public Command(FrontendListener front, Logic backend) {
        this.frontend = front;
        this.backend = backend;
        data = new HashMap();
    }

    /**
     * First uses checkData() to verify that the command has been given sufficient information.
     * Then runs functionality() to perform this command. This method should not be overriden,
     * the functionality should be written in the functionality-method
     * @throws ProtocolException if the command has insufficient data to run
     */
    public void execute() throws ProtocolException {
        checkData();
        functionality();
    }

    /**
     * The functionality of the command. This method defines what the command does.
     */
    protected abstract void functionality();

    /**
     * setParameter sets parameter data for command
     *
     * @param key name of the datum
     * @param value value of the datum
     */
    public abstract void setParameter(String key, String value);

    /**
     * Command must have checkData method which throws ProtocolException if it
     * doesn't have all data needed
     *
     * @throws ProtocolException if the command lacks some necessary data
     */
    public abstract void checkData() throws ProtocolException;
}
