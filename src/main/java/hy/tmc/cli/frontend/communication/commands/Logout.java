package hy.tmc.cli.frontend.communication.commands;

import com.google.common.base.Optional;
import hy.tmc.cli.configuration.ClientData;
import hy.tmc.cli.frontend.communication.server.ProtocolException;

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
            message = "User logged out. User data cleared.";
        } else {
            message = "Nobody is logged in!";
        }
        return Optional.of(message);
    }

    @Override
    public Boolean call() throws ProtocolException {
        if (ClientData.userDataExists()) {
            ClientData.clearUserData();
            this.cleanData();
            return true;
        } else {
            this.cleanData();
            return false;
        }
    }
}
