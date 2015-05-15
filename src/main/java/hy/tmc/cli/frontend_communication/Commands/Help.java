/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hy.tmc.cli.frontend_communication.Commands;

import hy.tmc.cli.frontend_communication.FrontendListener;
import hy.tmc.cli.frontend_communication.Result;
import static hy.tmc.cli.frontend_communication.Result.*;
import hy.tmc.cli.logic.Logic;

/**
 *
 * @author pihla
 */
public class Help extends Command {

    public Help(FrontendListener front, Logic backend) {
        super(front, backend);
    }

    
    @Override
    protected void functionality() {
        this.frontend.printLine("Commands: login, help, ping");
    }
    
    @Override
    public void setParameter(String key, String value) {
    }
    
    @Override
    public void checkData(){
        
    }
    
    
    
}
