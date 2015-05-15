package hy.tmc.cli.frontend_communication.Commands;

import hy.tmc.cli.frontend_communication.FrontendListener;
import hy.tmc.cli.frontend_communication.Server.ProtocolException;
import hy.tmc.cli.logic.Logic;

public class Echo extends Command {

    /**
     *
     * @param front
     * @param backend
     */
    public Echo(FrontendListener front, Logic backend) {
        super(front, backend);
    }

    
    @Override
    protected void functionality() {
        frontend.printLine(data.get("data"));
    }

    @Override
    public void setParameter(String key, String value) {
        data.put("data", value);
    }

    @Override 
    public void checkData() throws ProtocolException {
        if (data.get("data") == null) {
            throw new ProtocolException("Not enough data");
        }
    }

}
