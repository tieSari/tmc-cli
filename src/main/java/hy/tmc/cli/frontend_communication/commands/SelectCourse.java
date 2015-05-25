package hy.tmc.cli.frontend_communication.commands;

import hy.tmc.cli.frontend_communication.FrontendListener;
import hy.tmc.cli.frontend_communication.Server.ProtocolException;
import hy.tmc.cli.logic.Logic;



public class SelectCourse extends Command {

    public SelectCourse(FrontendListener front, Logic backend) {
        super(front, backend);
    }

    @Override
    protected void functionality() {
        int id = Integer.parseInt(data.get("id"));
        
        
        
    }

    @Override
    public void setParameter(String key, String value) {
        data.put(key, value);
    }

    @Override
    public void checkData() throws ProtocolException {
        
    }

}
