package hy.tmc.cli.configuration;

import org.junit.Test;
import static org.junit.Assert.*;
import static hy.tmc.cli.configuration.ClientData.*;
import hy.tmc.cli.domain.Course;
import org.junit.Before;

public class ClientDataTest {

    @Before
    public void setUp() {
        logOutCurrentUser();
    }

    @Test
    public void initiallyUserDataIsInitialData() {
        String s = getUsername() + getPassword();
        assertTrue(s.isEmpty());
    }

    @Test
    public void afterLoginInNewDataIsSet() {
        setUserData("ASD", "DSA");
        assertEquals("ASD", getUsername());
    }

    @Test
    public void logOutResetsUserData() {
        setUserData("ASD", "DSA");
        logOutCurrentUser();
        assertEquals("", getUsername());
    }

    @Test
    public void formatFormatsTheDataRight() {
        setUserData("ASD", "DSA");
        assertEquals("ASD:DSA", getFormattedUserData());
    }

    @Test
    public void userDataExistsAfterSet() {
        setUserData("ASD", "DSA");
        assertTrue(userDataExists());
    }

    /**
     * Test of setUserData method, of class ClientData.
     */
    @Test
    public final void testSetUserData() {
        String username = "";
        String password = "";
        ClientData.setUserData(username, password);
        assertEquals("", ClientData.getUsername());
    }

    /**
     * Test of getCurrentCourse method, of class ClientData.
     */
    @Test
    public final void testGetCurrentCourse() {
        Course expResult = null;
        Course result = ClientData.getCurrentCourse();
        assertEquals(expResult, result);
    }

    /**
     * Test of setCurrentCourse method, of class ClientData.
     */
    @Test
    public void testSetCurrentCourse() {
        Course currentCourse = null;
        ClientData.setCurrentCourse(currentCourse);
        assertEquals(currentCourse, ClientData.getCurrentCourse());
    }

    /**
     * Test of userDataExists method, of class ClientData.
     */
    @Test
    public void testUserDataExists() {
        boolean expResult = false;
        boolean result = ClientData.userDataExists();
        assertEquals(expResult, result);
    }

    /**
     * Test of clearUserData method, of class ClientData.
     */
    @Test
    public void testClearUserData() {
        System.out.println("clearUserData");
        ClientData.clearUserData();
        assertFalse(ClientData.userDataExists());
    }

    /**
     * Test of getFormattedUserData method, of class ClientData.
     */
    @Test
    public void testGetFormattedUserData() {
        String expResult = ":";
        String result = ClientData.getFormattedUserData();
        assertEquals(expResult, result);
    }

    /**
     * Test of logOutCurrentUser method, of class ClientData.
     */
    @Test
    public void testLogOutCurrentUser() {
        ClientData.logOutCurrentUser();
        assertFalse(ClientData.userDataExists());
    }

    /**
     * Test of getPID method, of class ClientData.
     */
    @Test
    public void testGetPID() {
        int expResult = 0;
        int result = ClientData.getPid();
        assertEquals(expResult, result);
    }

    /**
     * Test of setPID method, of class ClientData.
     */
    @Test
    public void testSetPID() {
        int PID = 123;
        ClientData.setPid(PID);
        assertEquals(PID, ClientData.getPid());
    }

    /**
     * Test of getUSERNAME method, of class ClientData.
     */
    @Test
    public void testGetUSERNAME() {
        String expResult = "";
        String result = ClientData.getUsername();
        assertEquals(expResult, result);
    }

    /**
     * Test of getPASSWORD method, of class ClientData.
     */
    @Test
    public void testGetPASSWORD() {
        String expResult = "";
        String result = ClientData.getPassword();
        assertEquals(expResult, result);
    }

}
