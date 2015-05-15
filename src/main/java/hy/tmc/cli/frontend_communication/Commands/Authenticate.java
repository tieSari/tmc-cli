/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hy.tmc.cli.frontend_communication.Commands;

import hy.tmc.cli.Configuration.ClientData;
import static hy.tmc.cli.backendCommunication.URLCommunicator.*;
import hy.tmc.cli.frontend_communication.Commands.Command;
import hy.tmc.cli.frontend_communication.FrontendListener;
import hy.tmc.cli.frontend_communication.Server.ProtocolException;
import hy.tmc.cli.logic.Logic;
import java.util.HashMap;

/**
 *
 * @author chang
 */
public class Authenticate extends Command {

    HashMap<String, String> data;

    public Authenticate(FrontendListener front, Logic backend) {
        super(front, backend);
        data = new HashMap<>();
    }

    private String returnResponse(int statusCode) {
        if (statusCode >= 200 && statusCode < 300) {
            ClientData.setUserData(data.get("username"), data.get("password"));
            return "Auth successful. Saved userdata in session";
        } 
        return "Auth unsuccessful. Check your connection and/or credentials";
    }
    
    @Override
    public void execute() {
       String auth = data.get("username") + ":" + data.get("password");
       int code = makeGetRequest("http://tmc.mooc.fi/staging/user", auth).getStatusCode();
       this.frontend.printLine(returnResponse(code));
    }

    @Override
    public void setParameter(String key, String value) {
        data.put(key, value);
    }

    @Override
    public void checkData() throws ProtocolException {
        
    }

    @Override
    protected void functionality() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}