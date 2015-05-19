package hy.tmc.cli.frontend_communication.Commands;

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
        StringBuilder builder = new StringBuilder();
        builder.append("Available commands: ");
        boolean first = true;
        for (String name : CommandFactory.allCommandNames()){
            if (!first){
                builder.append(" ,");
            }
            first = false;
            builder.append(name);
            
        }
        this.frontend.printLine(builder.toString());
    }
    
    /**
     * Does nothing, this command does not require data.
     * @param key
     * @param value 
     */
    @Override
    public void setParameter(String key, String value) {
    }
    
    /**
     * Does nothing, this command does not require data.
     */
    @Override
    public void checkData(){
        
    }
    
    
    
}
