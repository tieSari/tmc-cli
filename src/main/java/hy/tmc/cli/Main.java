package hy.tmc.cli;

import hy.tmc.cli.Configuration.ZipHandler;
import hy.tmc.cli.frontend_communication.FrontendListener;
import hy.tmc.cli.frontend_communication.Server.Server;
import hy.tmc.cli.logic.Logic;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException, InterruptedException {
        
        ZipHandler zip = new ZipHandler("viikko1-Viikko1_004.Muuttujat.zip", ".");
        zip.unzip();
        
//        Logic backend = new Logic();
//        FrontendListener frontendListener = new Server(1234, backend);
//        frontendListener.start();

    }

}
