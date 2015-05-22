package hy.tmc.cli.Configuration;

import org.junit.Test;
import static org.junit.Assert.*;
import static hy.tmc.cli.Configuration.ClientData.*;
import org.junit.Before;


public class ClientDataTest {

    
    @Before
    public void setUp(){
        logOutCurrentUser();
    }
    
    @Test
    public void initiallyUserDataIsInitialData() {
        String s = getUSERNAME() + getPASSWORD();
        assertTrue(s.isEmpty());
    }


    @Test
    public void afterLoginInNewDataIsSet() {
        setUserData("ASD", "DSA");
        assertEquals("ASD", getUSERNAME());
    }

    @Test
    public void logOutResetsUserData() {
        setUserData("ASD", "DSA");
        logOutCurrentUser();
        assertEquals("", getUSERNAME());
    }

    @Test
    public void formatFormatsTheDataRight() {
        setUserData("ASD", "DSA");
        assertEquals("ASD:DSA", getFormattedUserData());
    }
    
    @Test
    public void userDataExistsAfterSet(){
        setUserData("ASD", "DSA");
        assertTrue(userDataExists());
    }

}
