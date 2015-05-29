package hy.tmc.cli.frontend_communication.commands;

import static org.junit.Assert.fail;

import hy.tmc.cli.frontend.communication.commands.DownloadExercises;
import hy.tmc.cli.frontend.communication.server.ProtocolException;
import hy.tmc.cli.testhelpers.FrontendStub;

import org.junit.Before;
import org.junit.Test;


public class DownloadExercisesTest {
    
    private FrontendStub front;

    /**
     * Create FrontendStub. 
     */
    @Before
    public void setup() {
        front = new FrontendStub();
    }
    
    /**
     * Check that data checking success.
     */
    @Test
    public void testCheckDataSuccess() {
        DownloadExercises de = new DownloadExercises(front,null);
        de.setParameter("pwd", "/home/tmccli/uolevipuistossa");
        de.setParameter("courseID", "21");
        try {
            de.checkData();
        } catch (ProtocolException p) {
            fail("testCheckDataSuccess failed");
        }
    }

    /**
     * Check that if user didn't give correct data, data checking fails.
     */
    @Test
    public void testCheckDataFail() {
        DownloadExercises de = new DownloadExercises(front,null);
        try {
            de.checkData();
            fail("testCheckDataFail should have failed");
        } catch (ProtocolException p) {
            return;
        }
    }
    
    /**
     * User gives course id that isn't a number and will be informed about it.
     */
    @Test
    public void courseIdNotANumber() {
        DownloadExercises de = new DownloadExercises(front,null);
        de.setParameter("pwd", "/home/tmccli/uolevipuistossa");
        de.setParameter("courseID", "not a number");
        try {
            de.checkData();
            fail("testCheckDataFail should have failed");
        } catch (ProtocolException p) {
            return;
        }
    }
}
