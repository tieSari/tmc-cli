package hy.tmc.cli.listeners;

import com.google.common.base.Optional;
import com.google.common.util.concurrent.ListenableFuture;
import hy.tmc.cli.CliSettings;
import hy.tmc.cli.TmcCli;

import java.io.DataOutputStream;
import java.net.Socket;

public class LoginListener extends ResultListener<Boolean> {

    private TmcCli tmcCli;
    private CliSettings settings;

    public LoginListener(ListenableFuture<Boolean> commandResult,
                         DataOutputStream output, Socket socket, TmcCli tmcCli,
                         CliSettings settings) {
        super(commandResult, output, socket);
        this.tmcCli = tmcCli;
        this.settings = settings;
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
        this.tmcCli.login(settings.getUsername(), settings.getPassword());
    }
}
