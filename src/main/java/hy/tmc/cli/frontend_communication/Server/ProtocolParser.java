/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hy.tmc.cli.frontend_communication.Server;

import hy.tmc.cli.frontend_communication.Commands.Command;
import static hy.tmc.cli.frontend_communication.Commands.CommandFactory.*;
import hy.tmc.cli.logic.Logic;
import java.util.Arrays;
import java.util.HashMap;

/**
 *
 * @author pihla
 */
public class ProtocolParser { 
    
    private Server server;
    private Logic logic;
    private final HashMap<String, Command> commandsByName = new HashMap<>();
    
    /**
     * Constructor for Protocol Parser
     * @param server
     * @param logic
     */
    public ProtocolParser(Server server, Logic logic){
        this.server = server;
        this.logic = logic;
        this.createCommandMap();
    }


    private void createCommandMap() {
        commandsByName.put("help", Help(this.server, this.logic));
        commandsByName.put("ping", ReplyToPing(this.server, this.logic));
        commandsByName.put("echo", Echo(this.server, this.logic));
        commandsByName.put("login", Login(this.server, this.logic));
        //commandsByName.put("listcourses", null);
    }
    
    /**
     * Search for command by inputline 
     * @param inputLine
     * @return
     * @throws ProtocolException
     */
    public Command getCommand(String inputLine) throws ProtocolException{
       String[] elements = inputLine.split(" ");
       String commandName = elements[0];
       if(!commandsByName.containsKey(commandName)){
           throw new ProtocolException("Invalid command name");
       }
       Command command = commandsByName.get(commandName);
       command = giveData(elements, command);
       return command;
    }
    
    
    private Command giveData(String[] userInput, Command command){
        for (int i=1; i+1 < userInput.length; i+=2){
            String key = userInput[i];
            String value = userInput[i+1];
            command.setParameter(key, value);
        }
        return command;
    }
}
