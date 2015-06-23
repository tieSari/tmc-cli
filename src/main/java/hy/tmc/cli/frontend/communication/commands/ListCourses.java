package hy.tmc.cli.frontend.communication.commands;

import com.google.common.base.Optional;
import hy.tmc.cli.backend.communication.TmcJsonParser;
import hy.tmc.cli.configuration.ClientData;
import hy.tmc.cli.domain.Course;
import hy.tmc.cli.frontend.communication.server.ProtocolException;
import static java.lang.Integer.max;
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
        
        return Optional.of(getCourseNames(courses));
    }
    
    private int getLongest(List<Course> courses) {
        int longest = courses.get(0).getName().length();
        for (Course course : courses) {
            longest = max(course.getName().length(), longest);
        }
        return longest;
    }
    
    private String getCourseNames(List<Course> courses) {
        StringBuilder result = new StringBuilder();
        
        for (Course course : courses) {
            String name = course.getName();
            result = addSpaces(result, name, getLongest(courses));
            result.append(name).append(", id:")
                    .append(course.getId());
            result.append("\n");
        }
        
        return result.toString();
    }

    private static StringBuilder addSpaces(StringBuilder result, String name, int longest) {
        int spaces = longest - name.length();
        for (int i = 0; i < spaces; i++) {
            result.append(" ");
        }
        return result;
    }
    
    @Override
    public List<Course> call() throws ProtocolException {
        checkData();
        return TmcJsonParser.getCourses();
    }
}
