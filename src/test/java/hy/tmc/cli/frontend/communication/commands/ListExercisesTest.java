package hy.tmc.cli.frontend.communication.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import com.google.common.base.Optional;

import hy.tmc.cli.backend.Mailbox;
import hy.tmc.cli.backend.communication.ExerciseLister;

import hy.tmc.cli.configuration.ClientData;
import hy.tmc.cli.domain.Course;
import hy.tmc.cli.frontend.communication.server.ProtocolException;
import hy.tmc.cli.synchronization.TmcServiceScheduler;
import hy.tmc.cli.testhelpers.FrontendStub;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(ClientData.class)
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
        TmcServiceScheduler.disablePolling();
        ClientData.setUserData("Chang", "Jamo");
        mock();
        lister = Mockito.mock(ExerciseLister.class);
        Mockito.when(lister.listExercises(Mockito.anyString()))
                .thenReturn(example);

        front = new FrontendStub();
        list = new ListExercises(front, lister);
    }

    private void mock() {

        PowerMockito.mockStatic(ClientData.class);
        PowerMockito
                .when(ClientData.getCurrentCourse(Mockito.anyString()))
                .thenReturn(Optional.of(new Course()));
        PowerMockito
                .when(ClientData.getFormattedUserData())
                .thenReturn("Chang:Jamo");
        PowerMockito
                .when(ClientData.userDataExists())
                .thenReturn(true);
    }

    @Test
    public void testCheckDataSuccess() throws ProtocolException {
        ListExercises ls = new ListExercises(front);
        ls.setParameter("path", "legit");
        try {
            ls.checkData();
        } catch (ProtocolException p) {
            fail("testCheckDataSuccess failed");
        }
    }

    @Test
    public void getsExerciseName() throws ProtocolException {
        list.setParameter("path", "any");
        list.execute();
        System.out.println(front.getMostRecentLine());
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
        PowerMockito.mockStatic(ClientData.class);
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
        } catch (ProtocolException ex) {
            fail("unexpected exception");
        }
    }
}
