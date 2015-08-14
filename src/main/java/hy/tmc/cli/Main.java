package hy.tmc.cli;

import fi.helsinki.cs.tmc.core.TmcCore;
import fi.helsinki.cs.tmc.core.exceptions.TmcCoreException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {

    /**
     * Starts the main program.
     */
    public static void main(String[] args)
        throws IOException, InterruptedException, TmcCoreException {

        TmcCli cli = new TmcCli();
        cli.startServer();
    }
}
