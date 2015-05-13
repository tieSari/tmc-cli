/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hy.tmc.cli.frontend_communication.Commands.CommandLineClientCommands;

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

    @Override
    public void execute() {
       String url = data.get("url");
       this.frontend.printLine(makePostRequest(url, "").getData());
    }

    @Override
    public void setParameter(String key, String value) {
        data.put(key, value);
    }

    @Override
    public void checkData() throws ProtocolException {
        throw new ProtocolException("Not enough data, username needed");
    }

}
