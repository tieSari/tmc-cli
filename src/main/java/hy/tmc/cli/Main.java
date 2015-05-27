package hy.tmc.cli;

import com.google.common.collect.ImmutableList;
import fi.helsinki.cs.tmc.langs.RunResult;
import static fi.helsinki.cs.tmc.langs.RunResult.Status.TESTS_FAILED;
import fi.helsinki.cs.tmc.langs.TestResult;
import hy.tmc.cli.frontend.FrontendListener;
import hy.tmc.cli.frontend.ResultInterpreter;
import hy.tmc.cli.frontend.communication.server.Server;
import hy.tmc.cli.logic.Logic;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {

    /**
     * Starts the main program.
     */
    public static void main(String[] args) throws IOException, InterruptedException {

        Logic backend = new Logic();
        FrontendListener frontendListener = new Server(backend);
        frontendListener.start();
    }
}
