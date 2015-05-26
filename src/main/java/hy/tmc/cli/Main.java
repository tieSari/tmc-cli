package hy.tmc.cli;

import hy.tmc.cli.frontend.FrontendListener;
import hy.tmc.cli.frontend.communication.server.Server;
import hy.tmc.cli.logic.Logic;

import java.io.IOException;

public class Main {

    /**
     * Starts the main program.
     * @param args
     * @throws java.io.IOException
     * @throws java.lang.InterruptedException
     */
    public static void main(String[] args) throws IOException, InterruptedException {
        Logic backend = new Logic();
        FrontendListener frontendListener = new Server(backend);
        frontendListener.start();
    }

}
