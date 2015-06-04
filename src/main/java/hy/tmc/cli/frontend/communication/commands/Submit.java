package hy.tmc.cli.frontend.communication.commands;


import hy.tmc.cli.backend.communication.CourseSubmitter;
import hy.tmc.cli.backend.communication.SubmissionInterpreter;
import hy.tmc.cli.configuration.ClientData;
import hy.tmc.cli.domain.submission.FeedbackQuestion;
import hy.tmc.cli.domain.submission.SubmissionResult;
import hy.tmc.cli.frontend.FrontendListener;
import hy.tmc.cli.frontend.communication.server.ProtocolException;
import hy.tmc.cli.zipping.DefaultRootDetector;
import hy.tmc.cli.zipping.ProjectRootFinder;
import hy.tmc.cli.zipping.Zipper;

import java.io.IOException;
import java.util.List;

/**
 * Submit command for submitting exercises to TMC
 */
public class Submit extends Command {
    
    CourseSubmitter submitter;
    SubmissionInterpreter interpreter;
    
    public Submit(FrontendListener front) {
        super(front);
        submitter = new CourseSubmitter(
                new ProjectRootFinder(
                        new DefaultRootDetector()
                ),
                new Zipper()
        );
        interpreter = new SubmissionInterpreter();
    }
    
    /**
     * Constructor for mocking.
     * 
     * @param front frontend.
     * @param submitter can inject submitter mock.
     * @param interpreter can inject interpreter mock.
     */
    
    public Submit(FrontendListener front, CourseSubmitter submitter, SubmissionInterpreter interpreter) {
        super(front);
        this.submitter = submitter;
        this.interpreter = interpreter;
    }

    /**
     * Takes working directory in "path" and optionally the exercise's name in "exerciseName".
     */
    @Override
    protected void functionality() {
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
        }
        catch (IllegalArgumentException ex) {
            frontend.printLine(ex.getMessage());
        }
        catch (IOException | InterruptedException ex) {
            frontend.printLine("Project not found with specified parameters or thread interrupted");
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
