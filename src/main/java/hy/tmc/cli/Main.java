package hy.tmc.cli;

import com.google.common.collect.ImmutableList;
import fi.helsinki.cs.tmc.langs.RunResult;
import static fi.helsinki.cs.tmc.langs.RunResult.Status.TESTS_FAILED;
import fi.helsinki.cs.tmc.langs.TestResult;
import hy.tmc.cli.frontend.ResultInterpreter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {

    /**
     * Starts the main program.
     * @param args
     * @throws java.io.IOException
     * @throws java.lang.InterruptedException
     */
    public static void main(String[] args) throws IOException, InterruptedException {
        
        
        ImmutableList<TestResult> tests = tests();
        
        RunResult r = new RunResult(TESTS_FAILED, tests, null);
        ResultInterpreter i = new ResultInterpreter();
        System.out.println(i.interpret(r));

        //Logic backend = new Logic();
        //FrontendListener frontendListener = new Server(backend);
        //frontendListener.start();
    }
    
    private static ImmutableList<TestResult> tests() {
        List<TestResult> l = new ArrayList<>();
        List<String> ll = new ArrayList<>();
        ll.add("asdf");
        ll.add("dasdf");
        l.add(new TestResult("test", true, null, "error", ImmutableList.copyOf(ll)));
        
        return ImmutableList.copyOf(l);
    }
}
