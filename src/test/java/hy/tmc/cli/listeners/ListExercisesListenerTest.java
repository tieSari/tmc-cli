package hy.tmc.cli.listeners;

import hy.tmc.cli.backend.communication.ExerciseLister;
import fi.helsinki.cs.tmc.core.domain.Course;
import fi.helsinki.cs.tmc.core.domain.Exercise;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.eq;
import org.mockito.Mockito;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ListExercisesListenerTest {

    private ListExercisesListener lel;
    private ExerciseLister lister;

    @Before
    public void setUp() {
        lister = Mockito.mock(ExerciseLister.class);
        lel = new ListExercisesListener(null, null, null, lister);
    }

    @Test
    public void testParseData() {
        when(lister.buildExercisesInfo(anyList())).thenReturn("abc");
        List<Exercise> exs = new ArrayList<>();
        Course c = new Course();
        c.setExercises(exs);
        assertEquals("abc", lel.parseData(c).get());
        verify(lister, times(1)).buildExercisesInfo(eq(exs));
    }

}
