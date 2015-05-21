package hy.tmc.cli.frontend_communication.Commands;

import hy.tmc.cli.Configuration.ClientData;
import hy.tmc.cli.frontend_communication.FrontendListener;
import hy.tmc.cli.frontend_communication.Server.ProtocolException;
import hy.tmc.cli.logic.Logic;

/**
 * Allows the user to log out
 * 
 */

public class Logout extends Command {
    
    
    public Logout(FrontendListener front, Logic backend) {
        super(front, backend);
    }

    /**
     * Doesn't need any data. If the user is logged in, they are logged out.
     * If they aren't logged in nothing happens 
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
