package hy.tmc.cli.frontend.communication.commands;

import com.google.common.base.Optional;
import static hy.tmc.cli.backend.communication.UrlCommunicator.makeGetRequest;

import hy.tmc.cli.configuration.ClientData;
import hy.tmc.cli.configuration.ConfigHandler;
import hy.tmc.cli.frontend.communication.server.ProtocolException;

public class Authenticate extends Command<String> {

    /**
     * Regex for HTTP OK codes.
     */
    private final String httpOk = "2..";

    private String returnResponse(int statusCode) {
        if (Integer.toString(statusCode).matches(httpOk)) {
            ClientData.setUserData((String)data.get("username"), (String)data.get("password"));
            return "Auth successful. Saved userdata in session";
        }
        return "Auth unsuccessful. Check your connection and/or credentials";
    }

    @Override
    public final void setParameter(String key, String value) {
        getData().put(key, value);
    }

    @Override
    public void checkData() throws ProtocolException {
        String username = this.data.get("username");
        if (username == null || username.isEmpty()) {
            throw new ProtocolException("username must be set!");
        }
        String password = this.data.get("password");
        if (password == null || password.isEmpty()) {
            throw new ProtocolException("password must be set!");
        }
    }

    protected Optional<String> functionality() {
        String auth = data.get("username") + ":" + data.get("password");
        int code = makeGetRequest(
                new ConfigHandler().readAuthAddress(),
                auth
        ).getStatusCode();
        return Optional.of(returnResponse(code));
    }

    @Override
    public Optional<String> parseData(Object data) {
        String result = (String) data;
        return Optional.of(result);
    }

    @Override
    public String call() throws ProtocolException {
        System.out.println("mua kutsutaan!");
        checkData();
        return functionality().get();
    }
}
