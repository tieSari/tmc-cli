package hy.tmc.cli.frontend_communication.Commands;

import hy.tmc.cli.Configuration.ClientData;
import hy.tmc.cli.backendCommunication.HTTPResult;
import hy.tmc.cli.backendCommunication.URLCommunicator;
import hy.tmc.cli.frontend_communication.Server.ProtocolException;
import hy.tmc.cli.logic.Logic;
import hy.tmc.cli.testhelpers.ExampleJSON;
import hy.tmc.cli.testhelpers.FrontendMock;
import java.util.logging.Level;
import java.util.logging.Logger;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(URLCommunicator.class)
public class ListExercisesTest {

    private FrontendMock front;
    private Command list;

    @Before
    public void setup() {
        front = new FrontendMock();
        list = new ListExercises(front, new Logic());

        PowerMockito.mockStatic(URLCommunicator.class);

        HTTPResult fakeResult = new HTTPResult(ExampleJSON.courseExample, 200, true);

        ClientData.setUserData("chang", "paras");
        PowerMockito
                .when(URLCommunicator.makeGetRequest(
                        Mockito.eq(URLCommunicator.createClient()),
                        Mockito.anyString(), Mockito.anyString()))
                .thenReturn(fakeResult);
    }

    @Test
    public void testCheckDataSuccess() throws ProtocolException {
        ListExercises ls = new ListExercises(front, new Logic());
        ls.setParameter("courseUrl", "legit");
        try {
            ls.checkData();
        }
        catch (ProtocolException p) {
            fail("testCheckDataSuccess failed");
        }
    }
    
    @Test
    public void getsExerciseName() {
        list.setParameter("courseUrl", "any");
        try {
            list.execute();
            assertTrue(front.getMostRecentLine().contains("Dictionary"));
        }
        catch (ProtocolException ex) {
            Logger.getLogger(ListCoursesTest.class.getName()).log(Level.SEVERE, null, ex);
            fail("unexpected exception");
        }
        
    }
    
    @Test
    public void doesntContainWeirdName() {
        list.setParameter("courseUrl", "any");
        try {
            list.execute();
            System.err.println(front.getMostRecentLine());
            assertFalse(front.getMostRecentLine().contains("Ilari"));
        }
        catch (ProtocolException ex) {
            Logger.getLogger(ListCoursesTest.class.getName()).log(Level.SEVERE, null, ex);
            fail("unexpected exception");
        }
        
    }
    

}
