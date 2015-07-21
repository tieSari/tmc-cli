package hy.tmc.cli.frontend.communication.commands;

import com.google.common.base.Optional;

public class Authenticate extends Command<Boolean> {

    /**
     * Regex for HTTP OK codes.
     */
    private final String httpOk = "2..";

    public Authenticate(String username, String password) {
        this.setParameter("username", username);
        this.setParameter("password", password);
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
