package hy.tmc.cli.frontend.communication.commands;

<<<<<<< HEAD
import hy.tmc.cli.backend.Mailbox;
=======
import com.google.common.base.Optional;
>>>>>>> 7061d626a3951db33faf53d915810654bf6c1720
import hy.tmc.cli.configuration.ClientData;
import hy.tmc.cli.frontend.communication.server.ProtocolException;
import hy.tmc.cli.synchronization.TmcServiceScheduler;

/**
 * Allows the user to log out.
 */
public class Logout extends Command<Boolean> {

    @Override
    public void checkData() throws ProtocolException {}

    @Override
    public Optional<String> parseData(Object data) {
        Boolean result = (Boolean) data;
        String message;
        if (result) {
            ClientData.clearUserData();
<<<<<<< HEAD
            TmcServiceScheduler.getScheduler().stop();
            Mailbox.destroy();
            this.frontend.printLine("User logged out. User data cleared.");
=======
            message = "User logged out. User data cleared.";
>>>>>>> 7061d626a3951db33faf53d915810654bf6c1720
        } else {
            message = "Nobody is logged in!";
        }
        return Optional.of(message);
    }

    @Override
    public Boolean call() throws ProtocolException {
        if (ClientData.userDataExists()) {
            ClientData.clearUserData();
            return true;
        } else {
            return false;
        }
    }
}
