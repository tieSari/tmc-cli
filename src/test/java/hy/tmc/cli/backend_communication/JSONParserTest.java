package hy.tmc.cli.backend_communication;

import hy.tmc.cli.Configuration.ClientData;
import hy.tmc.cli.backendCommunication.JSONParser;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class JSONParserTest {
    
    @Test
    public void getsCourseNamesFromServer() {
        ClientData.setUserData("test", "1234");
        String courses = JSONParser.parseCourseNames();
        assertTrue(courses.contains("s2014-tira"));
        assertTrue(courses.contains("k2015-ohpe"));
        assertTrue(courses.contains("checkstyle-demo"));
        assertTrue(courses.contains("2014-mooc-no-deadline"));
        assertTrue(courses.contains("c-demo"));
    }
}
