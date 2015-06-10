package hy.tmc.cli.frontend.communication.commands;

import hy.tmc.cli.backend.Mailbox;
import hy.tmc.cli.backend.communication.ExerciseLister;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

import hy.tmc.cli.configuration.ClientData;
import hy.tmc.cli.frontend.communication.server.ProtocolException;
import hy.tmc.cli.testhelpers.FrontendStub;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class ListExercisesTest {

    private FrontendStub front;
    private Command list;
    private ExerciseLister lister;
    private String example = "viikko1-Viikko1_000.Hiekkalaatikko[ ]\n"
            + "viikko1-Viikko1_001.Nimi[x]\n"
            + "viikko1-Viikko1_002.HeiMaailma[ ]\n"
            + "viikko1-Viikko1_003.Kuusi[ ]";

    @Before
    public void setup() {
        Mailbox.create();
        ClientData.setPolling(true);
        ClientData.setUserData("Chang", "Jamo");
        lister = Mockito.mock(ExerciseLister.class);
        Mockito.when(lister.listExercises(Mockito.anyString()))
                .thenReturn(example);

        front = new FrontendStub();
        list = new ListExercises(front, lister);
    }

    @Test
    public void testCheckDataSuccess() throws ProtocolException {
        ListExercises ls = new ListExercises(front);
        ls.setParameter("path", "legit");
        try {
            ls.checkData();
        }
        catch (ProtocolException p) {
            fail("testCheckDataSuccess failed");
        }
    }

    @Test
    public void getsExerciseName() throws ProtocolException {
        list.setParameter("path", "any");
        list.execute();
        assertTrue(front.getMostRecentLine().contains("Viikko1"));
    }

    @Test
    public void returnsFailIfBadPath() throws ProtocolException {
        String found = "No course found";
        Mockito.when(lister.listExercises(Mockito.anyString()))
                .thenReturn(found);
        list.setParameter("path", "any");
        list.execute();
        assertEquals(found, front.getMostRecentLine());
    }

    @Test(expected = ProtocolException.class)
    public void throwsErrorIfNoUser() throws ProtocolException {
        ClientData.clearUserData();
        list.setParameter("path", "any");
        list.execute();
    }

    @Test(expected = ProtocolException.class)
    public void throwsErrorIfNoCourseSpecified() throws ProtocolException {
        list.execute();
    }

    @Test
    public void doesntContainWeirdName() {
        list.setParameter("path", "any");
        try {
            list.execute();
            assertFalse(front.getMostRecentLine().contains("Ilari"));
        }
        catch (ProtocolException ex) {
            fail("unexpected exception");
        }
    }
}
