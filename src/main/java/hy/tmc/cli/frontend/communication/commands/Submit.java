package hy.tmc.cli.frontend.communication.commands;

import com.google.common.base.Optional;
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

    CourseSubmitter submitter;
    SubmissionInterpreter interpreter;

    public Submit() {
        submitter = new CourseSubmitter(
                new ProjectRootFinder(new DefaultRootDetector()),
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
    public Submit(CourseSubmitter submitter, SubmissionInterpreter interpreter) {
        this.submitter = submitter;
        this.interpreter = interpreter;
    }

    /**
     * Takes a pwd command's output in "path" and optionally the exercise's name
     * in "exerciseName".
     */
    @Override
    protected Optional<String> functionality() {
        try {
            if (data.containsKey("exerciseName")) {
                return Optional.of("Doesnt work yet");
            } else {
                String returnUrl = submitter.submit(data.get("path"));
                return Optional.of(interpreter.resultSummary(returnUrl, true));
            }
        } catch (IllegalArgumentException | ParseException ex) {
            return Optional.of(ex.getMessage());
        } catch (IOException | InterruptedException ex) {
            return Optional.of("Project not found with specified parameters or thread interrupted");
        } catch (ExpiredException ex) {
            return Optional.of("Exercise has expired.");
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
