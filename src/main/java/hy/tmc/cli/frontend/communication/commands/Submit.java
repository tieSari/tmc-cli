package hy.tmc.cli.frontend.communication.commands;


import hy.tmc.cli.backend.communication.CourseSubmitter;
import hy.tmc.cli.backend.communication.SubmissionInterpreter;
import hy.tmc.cli.configuration.ClientData;
import hy.tmc.cli.frontend.FrontendListener;
import hy.tmc.cli.frontend.communication.server.ExpiredException;
import hy.tmc.cli.frontend.communication.server.ProtocolException;
import hy.tmc.cli.zipping.DefaultRootDetector;
import hy.tmc.cli.zipping.ProjectRootFinder;
import hy.tmc.cli.zipping.Zipper;
import java.io.IOException;
import java.text.ParseException;
import static javax.swing.text.html.HTML.Tag.HEAD;

/**
 * Submit command for submitting exercises to TMC
 */
public class Submit extends Command {
    
    public Submit(FrontendListener front) {
        super(front);
    }

    /**
     * Takes a pwd command's output in "path" and optionally the exercise's name in "exerciseName".
     */
    @Override
    protected void functionality() {
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
            }
        }
        catch (IllegalArgumentException | ParseException ex) {
            frontend.printLine(ex.getMessage());
        }
        catch (IOException | InterruptedException ex) {
            frontend.printLine("Project not found with specified parameters or thread interrupted");
        } 
        catch(ExpiredException ex){
            frontend.printLine("Exercise has expired so submit isn't possible.");
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
