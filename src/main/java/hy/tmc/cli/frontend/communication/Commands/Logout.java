package hy.tmc.cli.frontend.communication.commands;

import hy.tmc.cli.configuration.ClientData;
import hy.tmc.cli.frontend.communication.FrontendListener;
import hy.tmc.cli.frontend.communication.server.ProtocolException;
import hy.tmc.cli.logic.Logic;
/**
 * Allows the user to log out.
 */
public class Logout extends Command {
    
    public Logout(FrontendListener front, Logic backend) {
        super(front, backend);
    }
    /**
     * Doesn't need any data. If the user is logged in, they are logged out.
     * Otherwise nothing happens 
     */
    @Override
    protected void functionality() {
        if (ClientData.userDataExists()) {
            ClientData.clearUserData();
            this.frontend.printLine("User logged out. User data cleared.");
        } else {
            this.frontend.printLine("Nobody is logged in!");
        }
    }
    
    @Override
    public void checkData() throws ProtocolException {
    }
}
