package hy.tmc.cli.frontend_communication.commands;

import hy.tmc.cli.Configuration.ClientData;
import hy.tmc.cli.backendCommunication.JSONParser;
import hy.tmc.cli.frontend_communication.FrontendListener;
import hy.tmc.cli.frontend_communication.Server.ProtocolException;
import hy.tmc.cli.logic.Logic;

public class ListCourses extends Command {

    public ListCourses(FrontendListener front, Logic backend) {
        super(front, backend);
    }

    /**
     * use JSONParser to get a list of course names, and print it
     */
    @Override
    protected void functionality() {
        this.frontend.printLine(JSONParser.getCourseNames());
    }
    
    /**
     * Checks that the user has authenticated, by verifying ClientData
     * @throws ProtocolException if ClientData is empty
     */
    @Override
    public void checkData() throws ProtocolException {
        if (!ClientData.userDataExists()) {
            throw new ProtocolException("User must be authorized first");
        }
    }
    
}
