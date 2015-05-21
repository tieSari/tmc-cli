package hy.tmc.cli;

import hy.tmc.cli.Configuration.ZipHandler;
import hy.tmc.cli.frontend_communication.FrontendListener;
import hy.tmc.cli.frontend_communication.Server.Server;
import hy.tmc.cli.logic.Logic;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.lingala.zip4j.exception.ZipException;

public class Main {

    public static void main(String[] args) throws IOException, InterruptedException {
          
        ZipHandler zh = new ZipHandler("testzip/testzip.zip",".");
        try {
            zh.unzip();
            
            //Logic backend = new Logic();
            //frontendListener.start();
            //frontendListener.start();
        }
        catch (ZipException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("shiiit");
        }

    }

}
