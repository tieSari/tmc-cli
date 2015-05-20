package hy.tmc.cli;

import hy.tmc.cli.Configuration.ConfigurationParser;
import hy.tmc.cli.frontend_communication.FrontendListener;
import hy.tmc.cli.frontend_communication.Server.Server;
import hy.tmc.cli.logic.Logic;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException, InterruptedException {
        new ConfigurationParser(args);
        Logic backend = new Logic();
        FrontendListener frontendListener = new Server(1234, backend);
        frontendListener.start();

    }

}
