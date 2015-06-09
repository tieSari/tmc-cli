package hy.tmc.cli.frontend.communication.commands;

import static hy.tmc.cli.backend.communication.UrlCommunicator.makeGetRequest;

import hy.tmc.cli.configuration.ClientData;
import hy.tmc.cli.configuration.ConfigHandler;
import hy.tmc.cli.frontend.FrontendListener;
import hy.tmc.cli.frontend.communication.server.ProtocolException;

public class Authenticate extends Command {

    /**
     * Regex for HTTP OK codes.
     */
    private final String httpOk = "2..";

    public Authenticate(FrontendListener front) {
        super(front);
    }

    private String returnResponse(int statusCode) {
        if (Integer.toString(statusCode).matches(httpOk)) {
            ClientData.setUserData(data.get("username"), data.get("password"));
            return "Auth successful. Saved userdata in session";
        }
        return "Auth unsuccessful. Check your connection and/or credentials";
    }

    @Override
    public final void setParameter(String key, String value) {
        data.put(key, value);
    }

    @Override
    public void checkData() throws ProtocolException {
        if (!this.data.containsKey("username")) {
            throw new ProtocolException("username must be set!");
        }
        if (!this.data.containsKey("password")) {
            throw new ProtocolException("password must be set!");
        }
    }

    @Override
    protected void functionality() {
        String auth = data.get("username") + ":" + data.get("password");
        System.out.println(new ConfigHandler().readAuthAddress());
        int code = makeGetRequest(
                new ConfigHandler().readAuthAddress(),
                auth
        ).getStatusCode();
        this.frontend.printLine(returnResponse(code));
    }
}
