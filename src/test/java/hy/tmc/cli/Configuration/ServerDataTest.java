package hy.tmc.cli.Configuration;

import org.junit.Test;
import static org.junit.Assert.*;
import static hy.tmc.cli.Configuration.ServerData.*;

/**
 *
 * @author kristianw
 */
public class ServerDataTest {

    @Test
    public void returnsCoursesUrl() {
        assertEquals("https://tmc.mooc.fi/staging/courses.json?api_version=7", getCoursesUrl());
    }
    
    
    

}
