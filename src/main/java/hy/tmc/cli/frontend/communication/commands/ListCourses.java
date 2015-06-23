package hy.tmc.cli.frontend.communication.commands;

import com.google.common.base.Optional;
import hy.tmc.cli.backend.communication.TmcJsonParser;
import hy.tmc.cli.configuration.ClientData;
import hy.tmc.cli.domain.Course;
import hy.tmc.cli.frontend.communication.server.ProtocolException;
import java.util.List;

public class ListCourses extends Command<List<Course>> {

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
        @SuppressWarnings("unchecked")
        List<Course> courses = (List<Course>) data;

        return Optional.of(TmcJsonParser.getCourseNames(courses));
    }

    @Override
    public List<Course> call() throws Exception {
        checkData();
        return TmcJsonParser.getCourses();
    }
}
