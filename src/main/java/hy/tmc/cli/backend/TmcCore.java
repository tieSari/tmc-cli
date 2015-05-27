package hy.tmc.cli.backend;

import hy.tmc.cli.frontend.FrontendListener;
import hy.tmc.cli.frontend.communication.commands.Command;
import hy.tmc.cli.frontend.communication.commands.CommandFactory;
import hy.tmc.cli.frontend.communication.server.ProtocolException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TmcCore {
    
    private HashMap<String, Command> commands;
    
    /**
     * The TmcCore that can be used as a standalone businesslogic for any tmc
     * client application. The TmcCore provides all the essential backend 
     * functionalities as public methods. 
     * 
     * @param frontend the client frontend, that the core will communicate with.
     */
    public TmcCore(FrontendListener frontend) {
        commands = CommandFactory.createCommandMap(frontend);
    }
    
    public boolean login() {
        return run("login");
    }
    
    public boolean logout() {
        return run("logout");
    }
    
    public boolean selectServer() {
        return run("setServer");
    }
    
    public boolean downloadExercises() {
        return run("downloadExercises");
    }
    
    public boolean help() {
        return run("help");
    }
    
    public boolean listCourses() {
        return run("listCourses");
    }
    
    public boolean listExercises() {
        return run("listExercises");
    }
    
    private boolean run(String commandName) {
        try {
            commands.get(commandName).execute();
        } catch (ProtocolException ex) {
            Logger.getLogger(TmcCore.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }
}
