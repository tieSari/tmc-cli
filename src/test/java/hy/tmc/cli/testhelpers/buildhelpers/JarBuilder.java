package hy.tmc.cli.testhelpers.buildhelpers;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JarBuilder {

    public boolean jarExists(String path) {
        return buildJar(path);
    }

    private boolean buildJar(String path) {
        File file = null;
        try {
            file = new File(path);
            if (file.exists()) {
                return true;
            }

            System.out.println("Building .jar for tests...");
            ProcessBuilder builder = new ProcessBuilder(path);
            builder.redirectErrorStream(true);
            Process exec = builder.start();
            exec.waitFor();
            file = new File(path);
        }
        catch (IOException | InterruptedException ex) {
            System.err.println(ex.getMessage());
        }

        return file != null && file.exists();
    }

}
