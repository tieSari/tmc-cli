/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hy.tmc.cli.frontend_communication.Commands;

import hy.tmc.cli.frontend_communication.FrontendListener;
import hy.tmc.cli.logic.Logic;

/**
 *
 * @author ilari
 */
public class ReplyToPing extends Command {
    
    private final String answer = "pong";

    public ReplyToPing(FrontendListener front, Logic backend) {
        super(front, backend);
    }

    @Override
    protected void functionality() {
        this.frontend.printLine(answer);
    }

    @Override
    public void setParameter(String key, String value) {
    }
    
    @Override
    public void checkData(){
        
    }
    
}
