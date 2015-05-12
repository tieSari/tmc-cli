/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hy.tmc.cli.frontend_communication.Commands;

import hy.tmc.cli.frontend_communication.Result;
import static hy.tmc.cli.frontend_communication.Result.*;

/**
 *
 * @author pihla
 */
public class Help implements Command {

    @Override
    public void execute() {
        System.out.println("");
    }

    @Override
    public Result getResult() {
        return OK;
    }
    
}
