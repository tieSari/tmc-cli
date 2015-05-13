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
 * @author pihla
 */
public class Echo extends Command {

    private String data;
    public Echo(FrontendListener front, Logic backend) {
        super(front, backend);
    }

    @Override
    public void execute() {
        frontend.printLine(data);
    }

    @Override
    public void setParameter(String key, String value) {
        this.data = value;
    }
    
}
