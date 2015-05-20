package hy.tmc.cli.Configuration;

import org.junit.Test;
import static org.junit.Assert.*;
import static hy.tmc.cli.Configuration.ServerData.*;


public class ServerDataTest {

    @Test
    public void returnsCoursesUrl() {
        String superSite = "www.lol.fi";
        setCoursesUrl(superSite);
        setAuthUrl(superSite);
        assertEquals(superSite, getCoursesUrl());
    }
    
    
    

}
