package hy.tmc.cli.frontend.communication.commands;

import com.google.common.base.Optional;
import hy.tmc.cli.backend.communication.TmcJsonParser;
import hy.tmc.cli.configuration.ClientData;
import hy.tmc.cli.frontend.FrontendListener;
import hy.tmc.cli.frontend.communication.server.ProtocolException;

public class ListCourses extends Command<String>  {

    /**
     * use JSONParser to get a list of course names, and print it.
     */
    protected Optional<String> functionality() {
        return Optional.of(TmcJsonParser.getCourseNames());
    }

    /**
     * Checks that the user has authenticated, by verifying ClientData.
     *
     * @throws ProtocolException if ClientData is empty
     */
    @Override
    public void checkData() throws ProtocolException {
        if (!ClientData.userDataExists()) {
            throw new ProtocolException("User must be authorized first");
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
