package hy.tmc.cli.spyware;

import edu.emory.mathcs.backport.java.util.Arrays;
import hy.tmc.cli.configuration.ClientData;
import hy.tmc.cli.domain.Course;
import java.io.File;
import org.junit.After;
import org.junit.Test;
import org.junit.Before;

public class DiffSenderTest {
    
    @Before
    public void setup(){
        ClientData.setUserData("test", "1234");
    }
    
    @Test
    public void testSendToSpyware() {
        DiffSender sender = new DiffSender();
        Course testCourse = new Course();
        testCourse.setSpywareUrls(
                Arrays.asList(new String[]{"http://staging.spyware.testmycode.net/"})
        );
        sender.sendToSpyware(new File("testResources/test.zip"), testCourse);
    }
    
    @After
    public void cleanUp() {
        ClientData.clearUserData();
    }
    
}
