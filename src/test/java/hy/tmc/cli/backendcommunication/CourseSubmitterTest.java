package hy.tmc.cli.backendcommunication;

import hy.tmc.cli.configuration.ClientData;
import hy.tmc.cli.domain.Course;
import hy.tmc.cli.zipping.DefaultRootDetector;
import hy.tmc.cli.zipping.ProjectRootFinder;
import java.io.IOException;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

public class CourseSubmitterTest {

    private CourseSubmitter courseSubmitter;
    private String testFilePath = "/home/kristianw/Downloads/k14-ohpe/viikko1";
    private final String kurssinNimi = "k14-ohpe";
    private final String tehtavanNimi = "Viikko1_001.Nimi";

    @Before
    public void setup() {
        this.courseSubmitter = new CourseSubmitter(new ProjectRootFinder(new DefaultRootDetector()));
        ClientData.setUserData("test", "1234");
    }


    public void firstTest() {
        Course ex2 = this.courseSubmitter.getCurrentCourse(testFilePath);
        assertEquals(kurssinNimi, ex2.getName());
    }


    public void submission() throws IOException {
        
        this.courseSubmitter.submit(testFilePath, tehtavanNimi);
        
    }
}
