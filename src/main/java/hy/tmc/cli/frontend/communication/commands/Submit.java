package hy.tmc.cli.frontend.communication.commands;


import hy.tmc.cli.backend.communication.CourseSubmitter;
import hy.tmc.cli.backend.communication.SubmissionInterpreter;
import hy.tmc.cli.configuration.ClientData;
import hy.tmc.cli.frontend.FrontendListener;
import hy.tmc.cli.frontend.communication.server.ProtocolException;
import hy.tmc.cli.zipping.DefaultRootDetector;
import hy.tmc.cli.zipping.ProjectRootFinder;
import hy.tmc.cli.zipping.Zipper;

import java.io.IOException;
import static javax.swing.text.html.HTML.Tag.HEAD;

/**
 * Submit command for submitting exercises to TMC
 */
public class Submit extends Command {
    
<<<<<<< HEAD
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
=======
    public Submit(FrontendListener front) {
        super(front);
>>>>>>> a3f2f9be92426cd89883a869c76a9f187e20a8b1
    }

    /**
     * Takes a pwd command's output in "path" and optionally the exercise's name in "exerciseName".
     */
    @Override
    protected void functionality() {
<<<<<<< HEAD
        try {
            if (data.containsKey("exerciseName")) {
                frontend.printLine("Doesnt work yet");
            } else {
                String returnUrl = submitter.submit(data.get("path"));
                frontend.printLine(interpreter.resultSummary(returnUrl, true));
=======
        CourseSubmitter submitter = new CourseSubmitter(
                new ProjectRootFinder(
                        new DefaultRootDetector()
                ),
                new Zipper()
        );
        try {
            if (data.containsKey("exerciseName")) {
//                String returnUrl = submitter.submit(data.get("path"), data.get("exerciseName"));
//                frontend.printLine(new SubmissionInterpreter().resultSummary(returnUrl, true));
                frontend.printLine("Doesnt work yet");
            } else {
                String returnUrl = submitter.submit(data.get("path"));
                frontend.printLine(new SubmissionInterpreter().resultSummary(returnUrl, true));
>>>>>>> a3f2f9be92426cd89883a869c76a9f187e20a8b1
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
