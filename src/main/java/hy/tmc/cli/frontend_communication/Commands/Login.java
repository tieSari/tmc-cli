package hy.tmc.cli.frontend_communication.Commands;

import hy.tmc.cli.frontend_communication.Server.Server;
import hy.tmc.cli.logic.Logic;

/**
 * Created by samutamm on 13/05/15.
 */
public class Login extends Command {
    public Login(Server backend, Logic frontend) {
        super(backend, frontend);
    }

    @Override
    public void execute() {

    }

    @Override
    public void setParameter(String key, String value) {
        System.out.println(key);
        System.out.println(value);
    }
}
