/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hy.tmc.cli.frontend_communication.Commands;

import hy.tmc.cli.Configuration.ClientData;
import hy.tmc.cli.backendCommunication.JSONParser;
import hy.tmc.cli.frontend_communication.FrontendListener;
import hy.tmc.cli.frontend_communication.Server.ProtocolException;
import hy.tmc.cli.logic.Logic;

public class ListCourses extends Command {

    public ListCourses(FrontendListener front, Logic backend) {
        super(front, backend);
    }

    @Override
    protected void functionality() {
    }

    @Override
    public void execute() {
        if (!ClientData.userDataExists()) {
            this.frontend.printLine("Please authorize first.");
            return;
        }
        this.frontend.printLine(JSONParser.getCourseNames());
    }
    
    @Override
    public void setParameter(String key, String value) {
    }

    @Override
    public void checkData() throws ProtocolException {
    }
    
}
