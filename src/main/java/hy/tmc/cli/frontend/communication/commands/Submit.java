package hy.tmc.cli.frontend.communication.commands;

import com.google.common.base.Optional;
import hy.tmc.cli.backend.communication.CourseSubmitter;
import hy.tmc.cli.backend.communication.SubmissionInterpreter;
import hy.tmc.cli.configuration.ClientData;
import hy.tmc.cli.domain.submission.FeedbackQuestion;
import hy.tmc.cli.domain.submission.SubmissionResult;
import hy.tmc.cli.frontend.communication.server.ExpiredException;
import hy.tmc.cli.frontend.communication.server.ProtocolException;
import hy.tmc.cli.frontend.formatters.CommandLineSubmissionResultFormatter;
import hy.tmc.cli.frontend.formatters.VimSubmissionResultFormatter;
import hy.tmc.cli.zipping.DefaultRootDetector;
import hy.tmc.cli.zipping.ProjectRootFinder;
import hy.tmc.cli.zipping.Zipper;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.lingala.zip4j.exception.ZipException;

/**
 * Submit command for submitting exercises to TMC.
 */
public class Submit extends Command implements Callable<SubmissionResult> {

    CourseSubmitter submitter;
    SubmissionInterpreter interpreter;

    /**
     * Constructor for Submit command, creates the courseSubmitter.
     */
    public Submit() {
        submitter = new CourseSubmitter(
                new ProjectRootFinder(new DefaultRootDetector()),
                new Zipper()
        );
    }

    /**
     * Constructor for mocking.
     *
     * @param submitter can inject submitter mock.
     * @param interpreter can inject interpreter mock.
     */
    public Submit(CourseSubmitter submitter, SubmissionInterpreter interpreter) {
        this.interpreter = interpreter;
        this.submitter = submitter;
    }

    /**
     * Takes a pwd command's output in "path" and optionally the exercise's name
     * in "exerciseName".
     */
    private Optional<SubmissionResult> functionality() throws IOException, ParseException, ExpiredException, IllegalArgumentException, ZipException, InterruptedException, ProtocolException {
        interpreter = getInterpreter();
        String returnUrl = submitter.submit(data.get("path"));
        return Optional.of(interpreter.getSubmissionResult(returnUrl));
    }

    private SubmissionInterpreter getInterpreter() {
        if (data.containsKey("--vim")) {
            return new SubmissionInterpreter(new VimSubmissionResultFormatter());
        } else {
            return new SubmissionInterpreter(new CommandLineSubmissionResultFormatter());
        }
    }

    /**
     * Requires auth and pwd in "path" parameter.
     *
     * @throws ProtocolException if no auth or no path supplied.
     */
    @Override
    public void checkData() throws ProtocolException {
        if (!ClientData.userDataExists()) {
            throw new ProtocolException("User must be authorized first");
        }
        if (!this.data.containsKey("path")) {
            throw new ProtocolException("pwd not supplied");
        }
    }

    @Override
    public SubmissionResult call() throws Exception {
        checkData();
        return functionality().or(new SubmissionResult());
    }

    @Override
    public Optional<String> parseData(Object data) {
        try {
            SubmissionResult submissionResult = (SubmissionResult) data;
            String output = "";
            output = interpreter.resultSummary(true);
            
            if (submissionResult.isAllTestsPassed()) {
                List<FeedbackQuestion> feedback = submissionResult.getFeedbackQuestions();
                if (feedback != null && !feedback.isEmpty()) {
                    output += "Please give feedback:";
                    output += feedback + submissionResult.getFeedbackAnswerUrl();
                }
            }
            return Optional.of(output);
        } catch (InterruptedException ex) {
            return Optional.of("Error while parsing submissionResult.");
        }
    }
}
