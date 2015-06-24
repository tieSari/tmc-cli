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
import java.net.URI;
import java.text.ParseException;
import net.lingala.zip4j.exception.ZipException;

public class Paste extends Command<URI> {

    CourseSubmitter submitter;

    public Paste() {
        submitter = new CourseSubmitter(
                new ProjectRootFinder(new DefaultRootDetector()),
                new Zipper()
        );
    }

    /**
     * Constructor for mocking.
     *
     * @param submitter can inject submitter mock.
     */
    public Paste(CourseSubmitter submitter) {
        this.submitter = submitter;
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
        URI returnURI = (URI) data;
        return Optional.of("Paste submitted. Here it is: \n  " + returnURI);
    }

    /**
     * Takes a pwd command's output in "path" and prints out the URL for the
     * paste.
     *
     * @return 
     * @throws java.io.IOException
     * @throws java.text.ParseException
     * @throws hy.tmc.cli.frontend.communication.server.ExpiredException
     * @throws net.lingala.zip4j.exception.ZipException
     * @throws hy.tmc.cli.frontend.communication.server.ProtocolException
     */
    @Override
    public URI call() throws IOException, ParseException, ExpiredException, IllegalArgumentException, ZipException, ProtocolException {
        return URI.create(submitter.submitPaste(data.get("path")));
    }
}
