package hy.tmc.cli.frontend.communication.commands;

import hy.tmc.cli.backend.communication.ExerciseLister;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

import hy.tmc.cli.configuration.ClientData;
import hy.tmc.cli.domain.Exercise;
import hy.tmc.cli.frontend.communication.server.ProtocolException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import org.mockito.Mockito;
import static org.mockito.Mockito.when;

public class ListExercisesTest {

    private ListExercises list;
    private ExerciseLister lister;
    private List<Exercise> exampleExercises;

    private void buildExample() {
        exampleExercises = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            Exercise ex = new Exercise();
            ex.setId(i);
            ex.setName(i + " tehtävä");
            ex.setAttempted(random.nextBoolean());
            ex.setCompleted(random.nextBoolean());

            exampleExercises.add(ex);
        }

    }

    @Before
    public void setup() throws ProtocolException {
        buildExample();
        ClientData.setUserData("Chang", "Jamo");
        lister = Mockito.mock(ExerciseLister.class);
        Mockito.when(lister.listExercises(Mockito.anyString()))
                .thenReturn(exampleExercises);

        list = new ListExercises(lister);
    }

    @Test
    public void testCheckDataSuccess() throws ProtocolException {
        ListExercises ls = new ListExercises();
        ls.setParameter("courseUrl", "legit");
        ls.setParameter("path", "legit");
        try {
            ls.checkData();
        } catch (ProtocolException p) {
            fail("testCheckDataSuccess failed");
        }
    }

    @Test
    public void getsExerciseName() throws Exception {
        list.setParameter("path", "any");
        when(lister.buildExercisesInfo(eq(exampleExercises))).thenCallRealMethod();
        try {
            String result = list.parseData(list.call()).get();
            assertTrue(result.contains("1 tehtävä"));
            assertTrue(result.contains("3 tehtävä"));
        } catch (ProtocolException ex) {
            fail("unexpected exception");
        }
    }

    @Test
    public void returnsFailIfBadPath() throws ProtocolException, Exception {
        String found = "No course found";
        Mockito.when(lister.buildExercisesInfo(eq(exampleExercises))).thenReturn(found);
        list.setParameter("path", "any");
        String result = list.parseData(list.call()).get();
        assertEquals(found, result);

    }

    @Test(expected = ProtocolException.class)
    public void throwsErrorIfNoUser() throws ProtocolException {
        ClientData.clearUserData();
        list.setParameter("path", "any");
        list.call();
    }

    @Test(expected = ProtocolException.class)
    public void throwsErrorIfNoCourseSpecified() throws ProtocolException {
        ClientData.clearUserData();
        list.call();
    }

    @Test
    public void doesntContainWeirdName() throws ProtocolException {
        list.setParameter("path", "any");
        when(lister.buildExercisesInfo(eq(exampleExercises))).thenCallRealMethod();

        String result = list.parseData(list.call()).get();
        assertFalse(result.contains("Ilari"));

    }
}
