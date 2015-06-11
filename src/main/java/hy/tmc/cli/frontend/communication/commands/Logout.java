package hy.tmc.cli.frontend.communication.commands;

import hy.tmc.cli.backend.Mailbox;
import hy.tmc.cli.configuration.ClientData;
import hy.tmc.cli.frontend.FrontendListener;
import hy.tmc.cli.frontend.communication.server.ProtocolException;
import hy.tmc.cli.synchronization.TmcServiceScheduler;

/**
 * Allows the user to log out.
 */
public class Logout extends Command {

    public Logout(FrontendListener front) {
        super(front);
    }

    /**
     * Doesn't need any data. If the user is logged in, they are logged out.
     * Otherwise nothing happens.
     */
    @Override
    protected void functionality() {
        if (ClientData.userDataExists()) {
            ClientData.clearUserData();
            TmcServiceScheduler.getScheduler().stop();
            Mailbox.destroy();
            this.frontend.printLine("User logged out. User data cleared.");
        } else {
            this.frontend.printLine("Nobody is logged in!");
        }
    }

    @Override
    public void checkData() throws ProtocolException {
    }
}
