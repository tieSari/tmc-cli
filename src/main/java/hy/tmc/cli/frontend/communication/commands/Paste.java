package hy.tmc.cli.frontend.communication.commands;

import com.google.common.base.Optional;
import hy.tmc.cli.backend.communication.CourseSubmitter;
import hy.tmc.cli.configuration.ClientData;
import hy.tmc.cli.frontend.communication.server.ExpiredException;
import hy.tmc.cli.frontend.communication.server.ProtocolException;
import hy.tmc.cli.zipping.DefaultRootDetector;
import hy.tmc.cli.zipping.ProjectRootFinder;
import hy.tmc.cli.zipping.Zipper;
import java.io.IOException;
import java.text.ParseException;
import net.lingala.zip4j.exception.ZipException;

public class Paste extends Command<String> {

    CourseSubmitter submitter;
    
    public Paste() {
        submitter = new CourseSubmitter(
                new ProjectRootFinder( new DefaultRootDetector() ),
                new Zipper()
        );
    }

    /**
     * Constructor for mocking.
     *
     * @param front frontend.
     * @param submitter can inject submitter mock.
     */

    public Paste(CourseSubmitter submitter) {
        this.submitter = submitter;
    }

    /**
     * Takes a pwd command's output in "path" and prints out the URL for the paste.
     *
     */
    protected Optional<String> functionality() {
        try {
            String returnUrl = submitter.submitPaste(data.get("path"));
            return Optional.of("Paste submitted. Here it is: \n  " + returnUrl);
        } catch (IOException | ParseException | ExpiredException | IllegalArgumentException | ZipException ex) {
            return Optional.of(ex.getMessage());
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
    public Optional<String> parseData(Object data) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String call() throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
