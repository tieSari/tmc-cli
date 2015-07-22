package hy.tmc.cli.frontend.communication.commands;

import hy.tmc.cli.mail.Mailbox;
import com.google.common.base.Optional;
import hy.tmc.cli.frontend.communication.server.ProtocolException;
import hy.tmc.cli.synchronization.TmcServiceScheduler;

/**
 * Allows the user to log out.
 */
public class Logout extends Command<Boolean> {

    public Optional<String> parseData(Object data) {
        Boolean result = (Boolean) data;
        String message;
        if (result) {
            //ClientData.clearUserData();
            message = "User logged out. User data cleared.";
        } else {
            message = "Nobody is logged in!";
        }
        return Optional.of(message);
    }

    public Boolean call() throws ProtocolException {
//        if (ClientData.userDataExists()) {
//            TmcServiceScheduler.getScheduler().stop();
//            Mailbox.destroy();
//            ClientData.clearUserData();
//            return true;
//        } else {
//            return false;
//        }
        return true;
    }
}
