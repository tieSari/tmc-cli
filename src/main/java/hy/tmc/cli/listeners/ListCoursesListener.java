package hy.tmc.cli.listeners;

import com.google.common.base.Optional;
import com.google.common.util.concurrent.ListenableFuture;
import hy.tmc.core.domain.Course;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.List;

public class ListCoursesListener extends ResultListener<List<Course>> {

    public ListCoursesListener(ListenableFuture<List<Course>> commandResult, DataOutputStream output, Socket socket) {
        super(commandResult, output, socket);
    }

    @Override
    public Optional<String> parseData(List<Course> courses) {
        String courselist = getCourseNames(courses);
        if(courselist == null){
            return Optional.absent();
        } else {
            return Optional.of(courselist);
        }
    }
    
    @Override
    public void extraActions(List<Course> courses) {

    }

    private int getLongest(List<Course> courses) {
        int longest = courses.get(0).getName().length();
        for (Course course : courses) {
            longest = Math.max(course.getName().length(), longest);
        }
        return longest;
    }

    private String getCourseNames(List<Course> courses) {
        if (courses.isEmpty()) {
            return "";
        }
        StringBuilder result = new StringBuilder();

        result.append("NAME");
        result = addSpaces(result, "NAME", getLongest(courses))
                .append("ID\n");
        for (Course course : courses) {
            String name = course.getName();
            result.append(name);
            result = addSpaces(result, name, getLongest(courses))
                    .append(course.getId());
            result.append("\n");
        }

        return result.toString();
    }

    private StringBuilder addSpaces(StringBuilder result, String name, int longest) {
        int spaces = longest - name.length() + 2;
        for (int i = 0; i < spaces; i++) {
            result.append(" ");
        }
        return result;
    }
}
