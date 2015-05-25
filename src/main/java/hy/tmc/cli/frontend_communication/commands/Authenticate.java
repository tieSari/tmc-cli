package hy.tmc.cli.frontend_communication.commands;

import hy.tmc.cli.Configuration.ClientData;
import hy.tmc.cli.Configuration.ConfigHandler;
import static hy.tmc.cli.backendCommunication.URLCommunicator.createClient;
import static hy.tmc.cli.backendCommunication.URLCommunicator.makeGetRequest;
import hy.tmc.cli.frontend_communication.FrontendListener;
import hy.tmc.cli.frontend_communication.Server.ProtocolException;
import hy.tmc.cli.logic.Logic;

public class Authenticate extends Command {
    /**
     * Regex for HTTP OK codes.
     */
    private final String httpOk = "2..";

    public Authenticate(FrontendListener front, Logic backend) {
        super(front, backend);
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
        int code = makeGetRequest(createClient(), new ConfigHandler().readAuthAddress(), auth).getStatusCode();
        this.frontend.printLine(returnResponse(code));
    }

}
