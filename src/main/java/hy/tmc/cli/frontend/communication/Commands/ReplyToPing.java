
package hy.tmc.cli.frontend.communication.commands;

import hy.tmc.cli.frontend.communication.FrontendListener;
import hy.tmc.cli.logic.Logic;

public class ReplyToPing extends Command {
    
    private final String answer = "pong";

    public ReplyToPing(FrontendListener front, Logic backend) {
        super(front, backend);
    }

    /** 
     * Print pong to the frontend.
     */
    @Override
    protected void functionality() {
        this.frontend.printLine(answer);
    }

    /**
     * Does nothing, this command requires no data.
     */
    @Override
    public void checkData(){
        
    }
    
}
