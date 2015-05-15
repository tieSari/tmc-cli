/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hy.tmc.cli.frontend_communication.Server;

import hy.tmc.cli.frontend_communication.Commands.Command;
<<<<<<< HEAD
=======
import hy.tmc.cli.frontend_communication.Commands.CommandLineClientCommands.ReplyToPing;
import hy.tmc.cli.frontend_communication.Commands.Echo;
import hy.tmc.cli.frontend_communication.Commands.Help;
import hy.tmc.cli.frontend_communication.Commands.Login;
>>>>>>> 1084b84e89c3c1e8181ccdf556bba46b6b18262b
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


    public void createCommandMap() {
        commandsByName.put("help", new Help(this.server, this.logic));
        commandsByName.put("login", new Login(this.server, this.logic));
        commandsByName.put("ping", new ReplyToPing(this.server, this.logic));
        commandsByName.put("echo", new Echo(this.server, this.logic));
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
