package hy.tmc.cli.listeners;

import com.google.common.base.Optional;
import com.google.common.util.concurrent.ListenableFuture;
import java.io.DataOutputStream;
import java.net.Socket;

public class LoginListener extends ResultListener<Boolean> {

    public LoginListener(ListenableFuture<Boolean> commandResult, DataOutputStream output, Socket socket) {
        super(commandResult, output, socket);
    }

    @Override
    public Optional<String> parseData(Boolean result) {
        if (result) {
            return Optional.of("Auth successful. Saved userdata in session");
        }
        return Optional.of("Auth unsuccessful. Check your connection and/or credentials");
    }
    
    @Override
    public void extraActions(Boolean result) {
        if (result) {
            saveCredentials();
        }
    }

    private void saveCredentials() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
