package hy.tmc.cli.frontend.communication.commands;

import hy.tmc.cli.backend.communication.CourseSubmitter;
import hy.tmc.cli.backend.communication.SubmissionInterpreter;
import hy.tmc.cli.configuration.ClientData;
import hy.tmc.cli.domain.submission.FeedbackQuestion;
import hy.tmc.cli.domain.submission.SubmissionResult;
import hy.tmc.cli.frontend.FrontendListener;
import hy.tmc.cli.frontend.communication.server.ExpiredException;
import hy.tmc.cli.frontend.communication.server.ProtocolException;
import hy.tmc.cli.frontend.formatters.CommandLineSubmissionResultFormatter;
import hy.tmc.cli.frontend.formatters.VimSubmissionResultFormatter;
import hy.tmc.cli.zipping.DefaultRootDetector;
import hy.tmc.cli.zipping.ProjectRootFinder;
import hy.tmc.cli.zipping.Zipper;
import net.lingala.zip4j.exception.ZipException;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

/**
 * Submit command for submitting exercises to TMC.
 */
public class Submit extends Command {

    CourseSubmitter submitter;
    SubmissionInterpreter interpreter;

    /**
     * Constructor for Submit command, creates the courseSubmitter.
     * @param front FrontendListener that command will use for printing
     */
    public Submit(FrontendListener front) {
        super(front);
        submitter = new CourseSubmitter(
                new ProjectRootFinder(new DefaultRootDetector()),
                new Zipper()
        );
    }

    /**
     * Constructor for mocking.
     *
     * @param front frontend.
     * @param submitter can inject submitter mock.
     * @param interpreter can inject interpreter mock.
     */
    public Submit(FrontendListener front, CourseSubmitter submitter,
                  SubmissionInterpreter interpreter) {
        super(front);
        this.submitter = submitter;
    }

    /**
     * Takes working directory in "path" and optionally the exercise's name in "exerciseName".
     */
    @Override
    protected void functionality() {
        interpreter = getInterpreter();
        try {
            if (data.containsKey("exerciseName")) {
                frontend.printLine("Doesnt work yet");
            } else {
                String returnUrl = submitter.submit(data.get("path"));
                SubmissionResult submissionResult = interpreter.getSubmissionResult(returnUrl);
                String summary = interpreter.resultSummary(true);
                frontend.printLine(summary);

                if (submissionResult.isAllTestsPassed()) {
                    List<FeedbackQuestion> feedback = submissionResult.getFeedbackQuestions();
                    if (feedback != null && !feedback.isEmpty()) {
                        frontend.printLine("Please give feedback:");
                        frontend.feedback(feedback, submissionResult.getFeedbackAnswerUrl());
                    }
                }
            }
        } catch (IllegalArgumentException | ParseException ex) {
            frontend.printLine(ex.getMessage());
        } catch (IOException | InterruptedException ex) {
            frontend.printLine("Project not found with specified parameters or thread interrupted");
        } catch (ExpiredException ex) {
            frontend.printLine("Exercise has expired.");
        } catch (ZipException ex) {
            frontend.printLine("Zipping exercise failed.");
        } catch (ProtocolException e) {
            frontend.printLine("Failed to receive submit response.");
        }
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
}
