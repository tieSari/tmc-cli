package hy.tmc.cli.frontend.communication.commands;

import com.google.common.base.Optional;
import hy.tmc.cli.configuration.ClientData;
import hy.tmc.cli.frontend.communication.server.ProtocolException;

/**
 * Allows the user to log out.
 */
public class Logout extends Command<String> {

    /**
     * Doesn't need any data. If the user is logged in, they are logged out.
     * Otherwise nothing happens.
     */
    protected Optional<String> functionality() {
        String message = "";
        if (ClientData.userDataExists()) {
            ClientData.clearUserData();
            message = "User logged out. User data cleared.";
        } else {
            message = "Nobody is logged in!";
        }
        return Optional.of(message);
    }

    @Override
    public void checkData() throws ProtocolException {
    }

    @Override
    public Optional<String> parseData(Object data) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String call() throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
