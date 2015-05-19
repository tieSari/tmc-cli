package hy.tmc.cli.frontend_communication.Commands;

import hy.tmc.cli.Configuration.ClientData;
import hy.tmc.cli.backendCommunication.JSONParser;
import hy.tmc.cli.frontend_communication.FrontendListener;
import hy.tmc.cli.frontend_communication.Server.ProtocolException;
import hy.tmc.cli.logic.Logic;


public class ListExercises extends Command {

    public ListExercises(FrontendListener front, Logic backend) {
        super(front, backend);
    }

    @Override
    protected void functionality() {
       this.frontend.printLine(JSONParser.getExerciseNames(data.get("courseUrl")));
    }

    @Override
    public void setParameter(String key, String value) {
        data.put(key, value);
    }

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