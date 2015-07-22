package hy.tmc.cli.frontend.communication.commands;

import com.google.common.base.Optional;
import hy.tmc.cli.configuration.ClientData;
import hy.tmc.cli.frontend.communication.server.ProtocolException;
import hy.tmc.core.communication.TmcJsonParser;
import hy.tmc.core.domain.Course;
import java.io.IOException;
import java.util.List;

public class ListCourses extends CommandResultParser<List<Course>> {

    
    public ListCourses(){
        
    }


    @Override
    public Optional<String> parseData(Object data) {
        @SuppressWarnings("unchecked")
        List<Course> courses = (List<Course>) data;
        String courselist = getCourseNames(courses);
        if(courselist == null){
            return Optional.absent();
        } else {
            return Optional.of(courselist);
        }
    }

    private int getLongest(List<Course> courses) {
        int longest = courses.get(0).getName().length();
        for (Course course : courses) {
            longest = Math.max(course.getName().length(), longest);
        }
        return longest;
    }

    private String getCourseNames(List<Course> courses) {
        StringBuilder result = new StringBuilder();

        for (Course course : courses) {
            String name = course.getName();
            result.append(name).append(", ");
            result = addSpaces(result, name, getLongest(courses))
                    .append("id:")
                    .append(course.getId());
            result.append("\n");
        }

        return result.toString();
    }

    private StringBuilder addSpaces(StringBuilder result, String name, int longest) {
        int spaces = longest - name.length();
        for (int i = 0; i < spaces; i++) {
            result.append(" ");
        }
        return result;
    }
}
