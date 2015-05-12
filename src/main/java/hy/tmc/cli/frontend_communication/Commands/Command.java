/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hy.tmc.cli.frontend_communication.Commands;

import hy.tmc.cli.frontend_communication.Result;

/**
 *
 * @author kristianw
 */
public interface Command {
    public void execute();
    public Result getResult();
}
