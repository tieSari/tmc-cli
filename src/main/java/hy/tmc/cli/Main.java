package hy.tmc.cli;

import java.io.File;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;

public class Main {

    /**
     * Starts the main program.
     */
    public static void main(String[] args) throws IOException, InterruptedException {
        
        ZipFile zipFile;
        try {
            zipFile = new ZipFile(new File("zippedsetup.zip"));
            zipFile.addFolder("testResources/mockProject", new ZipParameters());
        }
        catch (ZipException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        
//        Logic backend = new Logic();
//        FrontendListener frontendListener = new Server(backend);
//        frontendListener.start();
    }

}
