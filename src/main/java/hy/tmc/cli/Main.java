package hy.tmc.cli;

import hy.tmc.cli.frontend.FrontendListener;
import hy.tmc.cli.frontend.communication.server.Server;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException, InterruptedException {
        FrontendListener frontendListener = new Server();
        frontendListener.start();
    }
}
