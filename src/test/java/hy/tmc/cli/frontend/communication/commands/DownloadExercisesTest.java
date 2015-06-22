package hy.tmc.cli.frontend.communication.commands;


import hy.tmc.cli.frontend.communication.server.ProtocolException;
import org.junit.Test;

public class DownloadExercisesTest {

    /**
     * Check that data checking success.
     */
    @Test
    public void testCheckDataSuccess() throws ProtocolException {
        DownloadExercises de = new DownloadExercises();
        de.setParameter("path", "/home/tmccli/uolevipuistossa");
        de.setParameter("courseID", "21");
        de.checkData();
    }

    /**
     * Check that if user didn't give correct data, data checking fails.
     */
    @Test(expected = ProtocolException.class)
    public void testCheckDataFail() throws ProtocolException {
        DownloadExercises de = new DownloadExercises();
        de.checkData();
    }

    /**
     * User gives course id that isn't a number and will be informed about it.
     */
    @Test(expected = ProtocolException.class)
    public void courseIdNotANumber() throws ProtocolException {
        DownloadExercises de = new DownloadExercises();
        de.setParameter("path", "/home/tmccli/uolevipuistossa");
        de.setParameter("courseID", "not a number");
        de.checkData();
    }
}
