package hy.tmc.cli.listeners;

import com.google.common.util.concurrent.ListenableFuture;
import hy.tmc.core.domain.Course;
import org.junit.Before;
import org.junit.Test;

import java.io.DataOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import static org.hamcrest.CoreMatchers.containsString;

import static org.junit.Assert.*;

public class ListCoursesListenerTest {

    private ListenableFuture future;
    private DataOutputStream output;
    private Socket socket;
    private ListCoursesListener listener;

    @Before
    public void setup() {
        this.listener = new ListCoursesListener(null, null, null);
    }

    @Test
    public void emptyList() {
        assertEquals("", listener.parseData(new ArrayList<Course>()).get());
    }

    @Test
    public void smallList() {
        List<Course> courses = new ArrayList<>();
        courses.add(new Course("Networking 1"));
        String result = listener.parseData(courses).get();
        assertThat(result, containsString("Networking 1"));
        assertThat(result, containsString("id"));
    }

    @Test
    public void testIndentation() {
        List<Course> courses = new ArrayList<>();
        String longerName = "Introduction to Number Theory";
        String shorterName = "Networking 1";
        courses.add(course(shorterName, 5));
        courses.add(course(longerName, 7));
        String result = listener.parseData(courses).get();

        assertThat(result, containsString(shorterName));
        assertThat(result, containsString(longerName));
        assertThat(result, containsString(expectedLine(shorterName, 5, longerName.length())));
        assertThat(result, containsString(expectedLine(longerName, 7, longerName.length())));
    }

    private Course course(String name, int id) {
        Course course = new Course(name);
        course.setId(id);
        return course;
    }

    private String expectedLine(String name, int id, int length) {
        return name + "," + spaces(name.length(), length) + "id:" + id + "\n";
    }

    private String spaces(int a, int b) {
        String spaces = " ";
        for (int i = 0; i < b - a; i++) {
            spaces += " ";
        }
        return spaces;
    }
}
