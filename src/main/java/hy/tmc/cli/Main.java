package hy.tmc.cli;

<<<<<<< HEAD
=======

>>>>>>> added CliSettings, implements core.TmcSettings and replaces ClientData
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
<<<<<<< HEAD
=======

>>>>>>> added CliSettings, implements core.TmcSettings and replaces ClientData
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
