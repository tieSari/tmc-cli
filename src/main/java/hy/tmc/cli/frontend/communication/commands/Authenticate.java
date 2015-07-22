package hy.tmc.cli.frontend.communication.commands;

import hy.tmc.cli.mail.Mailbox;
import com.google.common.base.Optional;
import hy.tmc.core.TmcCore;

public class Authenticate extends CommandResultParser<Boolean> {

    /**
     * Regex for HTTP OK codes.
     */
    private final String httpOk = "2..";
    private TmcCore core;

    public Authenticate(String username, String password) {
        this.setParameter("username", username);
        this.setParameter("password", password);
        this.core = new TmcCore();
    }
    
    public Authenticate(){
        
    }

    @Override
    public final void setParameter(String key, String value) {
        getData().put(key, value);
    }

    public Optional<String> parseData(Object data) {
        Boolean result = (Boolean) data;
        if (result) {
            return Optional.of("Auth successful. Saved userdata in session");
        }
        return Optional.of("Auth unsuccessful. Check your connection and/or credentials");
    }

    private boolean isOk(int code) {
        return Integer.toString(code).matches(httpOk);
    }
}
