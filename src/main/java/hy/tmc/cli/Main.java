package hy.tmc.cli;

import hy.tmc.cli.zipping.ZipHandler;
import hy.tmc.cli.frontend_communication.FrontendListener;
import hy.tmc.cli.frontend_communication.Server.Server;
import hy.tmc.cli.logic.Logic;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.lingala.zip4j.exception.ZipException;

public class Main {

    public static void main(String[] args) throws IOException, InterruptedException {

        Logic backend = new Logic();
        FrontendListener frontendListener = new Server(backend);
        frontendListener.start();

    }

}
