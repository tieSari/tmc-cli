/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hy.tmc.cli.frontend_communication.Commands;

import hy.tmc.cli.frontend_communication.FrontendListener;
import hy.tmc.cli.frontend_communication.Server.ProtocolException;
import hy.tmc.cli.logic.Logic;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author pihla
 */
public class Echo extends Command {

    private HashMap<String, String> data;

    /**
     *
     * @param front
     * @param backend
     */
    public Echo(FrontendListener front, Logic backend) {
        super(front, backend);
        data = new HashMap<>();
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
