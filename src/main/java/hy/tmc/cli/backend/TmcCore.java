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
    
    public boolean login(String username, String password) {
        return run("login", "username", username, "password", password);
    }
    
    public boolean logout() {
        return run("logout");
    }
    
    public boolean selectServer(String serverAddress) {
        return run("setServer", "tmc-server", serverAddress);
    }
    
    public boolean downloadExercises(String pwd, String courseId) {
        return run("downloadExercises", "pwd", pwd, "courseID", courseId);
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
    
    private boolean run(String commandName, String... args) {
        Command command = commands.get(commandName);
        if (command == null) {
            return false;
        }
        setParams(command, args);
        try {
            command.execute();
        } catch (ProtocolException ex) {
            Logger.getLogger(TmcCore.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }
    
    private void setParams(Command command, String... args) {
        for (int i=0, j=1; j < args.length; i+=2, j+=2) {
            command.setParameter(args[i], args[j]);
        }
    }
}