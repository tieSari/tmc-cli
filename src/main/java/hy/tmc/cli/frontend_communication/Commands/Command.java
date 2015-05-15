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
import java.util.Map;

/**
 *
 * @author pihla
 */
public abstract class Command implements Runnable {

    /**
     *
     */
    protected final FrontendListener frontend;

    /**
     *
     */
    protected final Logic backend;

    
    protected Map<String, String> data;
    
    
    /**
     *
     * @param front
     * @param backend
     */
    public Command(FrontendListener front, Logic backend) {
        this.frontend = front;
        this.backend = backend;
        data = new HashMap();
    }

    @Override
    public void run() {
        this.functionality();
    }

    public void execute() throws ProtocolException {
        checkData();
        functionality();
    }

    /**
     * General function to execute command
     */
    protected abstract void functionality();

    /**
     * setParameter sets parameter data for command
     *
     * @param key
     * @param value
     */
    public abstract void setParameter(String key, String value);

    /**
     * Command must have checkData method which throws ProtocolException if it
     * doesn't have all data needed
     *
     * @throws ProtocolException
     */
    public abstract void checkData() throws ProtocolException;
    // public Result getResult();
}
