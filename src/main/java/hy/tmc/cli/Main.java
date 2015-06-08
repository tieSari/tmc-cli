package hy.tmc.cli;

import hy.tmc.cli.backend.communication.StatusPoller;
import hy.tmc.cli.synchronization.TmcServiceScheduler;

import java.io.IOException;

public class Main {

    /**
     * Starts the main program.
     *
     * @param args arguments.
     * @throws java.io.IOException if there is an error while reading user input.
     * @throws java.lang.InterruptedException if server is interrupted. Starts the server.
     */
    public static void main(String[] args) throws IOException, InterruptedException {
      //  FrontendListener frontendListener = new Server();
        // frontendListener.start();
   
        StatusPoller poller = new StatusPoller();
        new TmcServiceScheduler().addService(poller).start();

    }
}
