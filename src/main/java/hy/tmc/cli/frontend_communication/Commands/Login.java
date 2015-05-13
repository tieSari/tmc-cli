package hy.tmc.cli.frontend_communication.Commands;

import hy.tmc.cli.frontend_communication.FrontendListener;
import hy.tmc.cli.frontend_communication.Server.ProtocolException;
import hy.tmc.cli.frontend_communication.Server.Server;
import hy.tmc.cli.logic.Logic;

import java.util.HashMap;

/**
 * Created by samutamm on 13/05/15.
 */
public class Login extends Command {
    public Login(FrontendListener front, Logic backend) {
        super(front, backend);
    }
    private HashMap<String, String> data = new HashMap<>();


    @Override
    public void execute() {
        String username = data.get("username");
        String password = data.get("password");

        System.out.println(data.toString());

        boolean authIsOk = checkIfAuthenticationOk(username, password);

        // TODO: make this more sensible
        if (authIsOk) {
            frontend.printLine("authOk");
        } else {
            frontend.printLine("authNotOk");
        }
    }

    private boolean checkIfAuthenticationOk(String username, String password) {
        return username.equals("matti") && password.equals("meikalainen");
    }

    @Override
    public void setParameter(String key, String value) {
        System.out.println(key);
        System.out.println(value);
        data.put(key,value);
    }

    @Override
    public void checkData() throws ProtocolException {
        if (!data.containsKey("username") && !data.containsKey("password")) {
            throw new ProtocolException("Username and password was not defined!");
        }

        if (!data.containsKey("username")) {
            throw new ProtocolException("Username was not defined!");
        }

        if (!data.containsKey("password")) {
            throw new ProtocolException("Password was not defined!");
        }
    }
}
