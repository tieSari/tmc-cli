/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hy.tmc.cli.frontend_communication.Server;

import hy.tmc.cli.frontend_communication.Commands.Command;
import hy.tmc.cli.frontend_communication.Commands.Help;
import java.util.HashMap;

/**
 *
 * @author pihla
 */
public class ProtocolParser { 
    
    static final HashMap<String, Command> commandsByName = new HashMap<String, Command>();
    static {
        commandsByName.put("help", new Help());
        commandsByName.put("listcourses", null);
    }
    
    static Command getCommand(String inputLine) throws ProtocolException{
       String[] elements = inputLine.split(";");
       String commandName = elements[0];
       if(!commandsByName.containsKey(commandName)){
           throw new ProtocolException("Invalid command name");
       }
       Command command = commandsByName.get(commandName);
       command = giveData(elements, command);
       return command;
    }
    
    private static Command giveData(String[] data, Command command){
        return command;
    }
}
