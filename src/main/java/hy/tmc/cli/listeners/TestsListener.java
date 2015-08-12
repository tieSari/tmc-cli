package hy.tmc.cli.listeners;

import com.google.common.util.concurrent.ListenableFuture;
import fi.helsinki.cs.tmc.core.domain.submission.ValidationError;
import fi.helsinki.cs.tmc.core.domain.submission.Validations;
import fi.helsinki.cs.tmc.langs.domain.RunResult;
import static fi.helsinki.cs.tmc.langs.domain.RunResult.Status.PASSED;
import hy.tmc.cli.frontend.ResultInterpreter;
import hy.tmc.cli.frontend.formatters.CheckstyleFormatter;
import hy.tmc.cli.frontend.formatters.TestResultFormatter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TestsListener implements Runnable {

    private TestResultFormatter runResultFormatter;
    private boolean showStackStrace;
    private ListenableFuture<Validations> checkstyleFuture;
    private ListenableFuture<RunResult> runResultFuture;
    private DataOutputStream output;
    private CheckstyleFormatter checkstyleFormatter;
    private Socket socket;
    

    public TestsListener(ListenableFuture<RunResult> testsResult,
            ListenableFuture<Validations> checkstyle,
            DataOutputStream output, Socket socket,
            TestResultFormatter interpreter,
            CheckstyleFormatter checkFormatter, boolean verbose) {
        
        this.output = output;
        this.socket = socket;
        this.runResultFuture = testsResult;
        this.checkstyleFuture = checkstyle;
        this.runResultFormatter = interpreter;
        this.checkstyleFormatter = checkFormatter;
        this.showStackStrace = verbose;
    }

    private String parseData(RunResult result) {
        ResultInterpreter resInt = new ResultInterpreter(result, runResultFormatter);
        return resInt.interpret(showStackStrace);
    }

    private String parseData(Validations result) {
        if (result.getValidationErrors() == null || result.getValidationErrors().isEmpty()) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<String, List<ValidationError>> entry : result.getValidationErrors().entrySet()) {
            builder.append(this.checkstyleFormatter.checkstyleErrors(entry.getKey(), entry.getValue()));
        }

        return builder.toString();
    }
    
    @Override
    public void run() {
        if (this.checkstyleFuture.isDone() && this.runResultFuture.isDone()) {
            try {
                RunResult tests = this.runResultFuture.get();
                Validations checks = this.checkstyleFuture.get();
                
                if (tests.status == PASSED && !checks.getValidationErrors().isEmpty()) {
                    output.write("All tests passed, but there are checkstyle errors.\n".getBytes());
                    output.write(parseData(checks).getBytes());
                    output.write("\n".getBytes());
                    socket.close();
                    return;
                }
                
                output.write(parseData(tests).getBytes());
                output.write(parseData(checks).getBytes());
                output.write("\n".getBytes());
                socket.close();
                
            } catch (InterruptedException | ExecutionException | IOException ex) {
                System.err.println(ex.getMessage());
                Logger.getLogger(TestsListener.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
