package hy.tmc.cli;

import hy.tmc.cli.frontend.communication.server.Server;
import hy.tmc.cli.frontend.communication.server.ProtocolException;
import hy.tmc.core.TmcCore;
import java.io.File;

import java.io.IOException;
import java.nio.file.Paths;

public class Main {

    /**
     * Starts the main program.
     *
     * @param args arguments.
     * @throws java.io.IOException if there is an error while reading user
     * input.
     * @throws java.lang.InterruptedException if server is interrupted. Starts
     * the server.
     */
    public static void main(String[] args) throws IOException, InterruptedException, ProtocolException {
        File cacheFile = Paths.get("bin", "cache").toFile();
        if (!cacheFile.exists()) {
            cacheFile.createNewFile();
        }
        TmcCore core = new TmcCore(cacheFile);
        TmcCli cli = new TmcCli(core);
        cli.startServer();
    }
}
