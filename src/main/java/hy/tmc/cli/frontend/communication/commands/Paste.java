package hy.tmc.cli.frontend.communication.commands;

import hy.tmc.cli.backend.communication.CourseSubmitter;
import hy.tmc.cli.backend.communication.HttpResult;
import hy.tmc.cli.backend.communication.SubmissionInterpreter;
import hy.tmc.cli.backend.communication.TmcJsonParser;
import hy.tmc.cli.configuration.ClientData;
import hy.tmc.cli.frontend.FrontendListener;
import hy.tmc.cli.frontend.communication.server.ExpiredException;
import hy.tmc.cli.frontend.communication.server.ProtocolException;
import hy.tmc.cli.zipping.DefaultRootDetector;
import hy.tmc.cli.zipping.ProjectRootFinder;
import hy.tmc.cli.zipping.Zipper;
import java.io.IOException;
import java.text.ParseException;

public class Paste extends Command {

    CourseSubmitter submitter;
    
    public Paste(FrontendListener front) {
        super(front);
        submitter = new CourseSubmitter(
                new ProjectRootFinder(
                        new DefaultRootDetector()
                ),
                new Zipper()
        );
    }

    /**
     * Constructor for mocking.
     *
     * @param front frontend.
     * @param submitter can inject submitter mock.
     */

    public Paste(FrontendListener front, CourseSubmitter submitter) {
        super(front);
        this.submitter = submitter;
    }

    /**
     * Takes a pwd command's output in "path" and prints out the URL for the paste.
     *
     */
    @Override
    protected void functionality() {
        try {
            String returnUrl = submitter.submitPaste(data.get("path"));
            frontend.printLine("Paste submitted. Here it is: \n  " + returnUrl);
        }
        catch (IOException | ParseException | ExpiredException ex) {
            frontend.printLine(ex.getMessage());
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
