package hy.tmc.cli;

import fi.helsinki.cs.tmc.core.TmcCore;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {

    /**
     * Starts the main program.
     */
    public static void main(String[] args) throws IOException, InterruptedException {
        Path cacheFile;
        if (System.getenv("XDG_CACHE_HOME") == null) {
            cacheFile = Paths.get(System.getenv("XDG_CACHE_HOME"), "tmc", "cache");
        } else {
            cacheFile = Paths.get(System.getenv("HOME"), ".cache", "tmc", "cache");
        }
        Files.createDirectories(cacheFile.getParent());
        if (!Files.exists(cacheFile)) {
            Files.createFile(cacheFile);
        }
        TmcCore core = new TmcCore(cacheFile.toFile());
        TmcCli cli = new TmcCli(core);
        cli.startServer();
    }
}
