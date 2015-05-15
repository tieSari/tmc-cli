/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hy.tmc.cli.frontend_communication.Commands;

import hy.tmc.cli.frontend_communication.Commands.CommandLineClientCommands.*;
import hy.tmc.cli.frontend_communication.FrontendListener;
import hy.tmc.cli.logic.Logic;

/**
 *
 * @author ilari
 */
public class CommandFactory {
    
    public static Command Help(FrontendListener front, Logic back) {
        return new Help(front, back);
    }
    
    public static Command Echo(FrontendListener front, Logic back) {
        return new Echo(front, back);
    }
    
    public static Command ReplyToPing(FrontendListener front, Logic back) {
        return new ReplyToPing(front, back);
    }
<<<<<<< HEAD
    
    public static Command Authenticate(FrontendListener front, Logic back) {
        return new Authenticate(front, back);
    }
=======

    public static Command Login(FrontendListener front, Logic back) { return new Login(front, back); }
>>>>>>> origin/clihelp
}
