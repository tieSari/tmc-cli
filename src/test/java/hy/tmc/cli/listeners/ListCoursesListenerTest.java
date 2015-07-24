package hy.tmc.cli.listeners;

import com.google.common.util.concurrent.ListenableFuture;
import hy.tmc.core.domain.Course;
import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.DataOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class ListCoursesListenerTest {

    private ListenableFuture future;
    private DataOutputStream output;
    private Socket socket;
    private ListCoursesListener listener;

    @Before
    public void setup() {
        future = Mockito.mock(ListenableFuture.class);
        output = Mockito.mock(DataOutputStream.class);
        socket = Mockito.mock(Socket.class);
        this.listener = new ListCoursesListener(future, output, socket);
    }

    @Test
    public void emptyList() {
        assertEquals(listener.parseData(new ArrayList<Course>()).get(), "");
    }

    @Test
    public void smallList() {
        List<Course> courses = new ArrayList<>();
        courses.add(new Course("Networking 1"));
        String result = listener.parseData(courses).get();
        assertThat(result, CoreMatchers.containsString("Networking 1"));
        assertThat(result, CoreMatchers.containsString("id"));
    }
}