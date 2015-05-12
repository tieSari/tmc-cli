/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hy.tmc.cli.frontend_communication.Commands;

import hy.tmc.cli.frontend_communication.FrontendListener;
import hy.tmc.cli.logic.Logic;


public abstract class Command {
    
    protected final FrontendListener frontend;
    protected final Logic backend;
    
    public Command(FrontendListener front, Logic backend){
        this.frontend = front;
        this.backend = backend;
    }
    
    
    public abstract void execute();
    public abstract void setParameter(String key, String value);
    // public Result getResult();
}
