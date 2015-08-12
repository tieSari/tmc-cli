package hy.tmc.cli;

import fi.helsinki.cs.tmc.core.TmcCore;
import java.io.File;

import java.io.IOException;
import java.nio.file.Paths;

public class Main {

    /**
     * Starts the main program.
     */
    public static void main(String[] args) throws IOException, InterruptedException {
        File cacheFile = Paths.get("cache").toFile();
        if (!cacheFile.exists()) {
            cacheFile.createNewFile();
        }
        TmcCore core = new TmcCore(cacheFile);
        TmcCli cli = new TmcCli(core);
        cli.startServer();
    }
}
