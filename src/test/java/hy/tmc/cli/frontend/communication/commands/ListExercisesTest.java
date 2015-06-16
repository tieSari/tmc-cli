package hy.tmc.cli.frontend.communication.commands;

import hy.tmc.cli.backend.communication.ExerciseLister;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

import hy.tmc.cli.configuration.ClientData;
import hy.tmc.cli.frontend.communication.server.ProtocolException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class ListExercisesTest {

    private ListExercises list;
    private ExerciseLister lister;
    private String example = "viikko1-Viikko1_000.Hiekkalaatikko[ ]\n"
            + "viikko1-Viikko1_001.Nimi[x]\n"
            + "viikko1-Viikko1_002.HeiMaailma[ ]\n"
            + "viikko1-Viikko1_003.Kuusi[ ]";

    @Before
    public void setup() {
        ClientData.setUserData("Chang", "Jamo");
        lister = Mockito.mock(ExerciseLister.class);
        Mockito.when(lister.listExercises(Mockito.anyString()))
                .thenReturn(example);

        list = new ListExercises(lister);
    }

    @Test
    public void testCheckDataSuccess() throws ProtocolException {
        ListExercises ls = new ListExercises();
        ls.setParameter("courseUrl", "legit");
        ls.setParameter("path", "legit");
        try {
            ls.checkData();
        }
        catch (ProtocolException p) {
            fail("testCheckDataSuccess failed");
        }
    }

    @Test
    public void getsExerciseName() throws Exception {
        list.setParameter("path", "any");
        try {
            String result = list.parseData(list.call()).get();
            assertTrue(result.contains("Viikko1_000.Hiekkalaatikko"));
            assertTrue(result.contains("viikko1-Viikko1_002.HeiMaailma"));
        }
        catch (ProtocolException ex) {
            fail("unexpected exception");
        }
    }

    @Test
    public void returnsFailIfBadPath() throws ProtocolException, Exception {
        String found = "No course found";
        Mockito.when(lister.listExercises(Mockito.anyString()))
                .thenReturn(found);
        list.setParameter("path", "any");
        String result = list.parseData(list.call()).get();
        assertEquals(found, result);

    }

    @Test(expected = ProtocolException.class)
    public void throwsErrorIfNoUser() throws ProtocolException, Exception {
        ClientData.clearUserData();
        list.setParameter("path", "any");
        list.call();
    }

    @Test(expected = ProtocolException.class)
    public void throwsErrorIfNoCourseSpecified() throws ProtocolException, Exception {
        list.call();
    }

    @Test
    public void doesntContainWeirdName() {
        list.setParameter("path", "any");
        try {
            String result = list.parseData(list.call()).get();
            assertFalse(result.contains("Ilari"));
        }
        catch (ProtocolException ex) {
            fail("unexpected exception");
        }
    }
}
