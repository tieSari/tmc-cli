package hy.tmc.cli.frontend_communication.commands;

import com.google.common.base.Joiner;
import hy.tmc.cli.frontend_communication.FrontendListener;
import hy.tmc.cli.logic.Logic;

public class Help extends Command {

    public Help(FrontendListener front, Logic backend) {
        super(front, backend);
    }

    /**
     * List all available commands
     */
    @Override
    protected void functionality() {
        String commands  = "Available commands: \n";
        commands += Joiner.on(", ").join(CommandFactory.allCommandNames());
        this.frontend.printLine(commands);
    }

    /**
     * Does nothing, this command does not require data.
     */
    @Override
    public void checkData(){
        
    }
    
    
    
}
