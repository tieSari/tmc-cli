package hy.tmc.cli.backendcommunication;

import hy.tmc.cli.configuration.ClientData;
import hy.tmc.cli.domain.Course;
import hy.tmc.cli.zipping.DefaultRootDetector;
import hy.tmc.cli.zipping.ProjectRootFinder;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

public class CourseSubmitterTest {

    private CourseSubmitter courseSubmitter;

    @Before
    public void setup() {
        this.courseSubmitter = new CourseSubmitter(new ProjectRootFinder(new DefaultRootDetector()));
        ClientData.setUserData("test", "1234");
    }

    @Test
    public void firstTest() {
        Course ex1 = this.courseSubmitter.getCurrentCourse("/home/kristianw/Downloads/k14-ohpe/viikko1");
        Course ex2 = this.courseSubmitter.getCurrentCourse("/home/kristianw/Downloads/k14-ohpe/viikko1/Viikko1_001.Nimi");
        assertEquals("k14-ohpe", ex1.getName());
        assertEquals("k14-ohpe", ex2.getName());
    }

    @Test
    public void submission() {

        this.courseSubmitter.submit("/home/kristianw/Downloads/k14-ohpe/viikko1", "Viikko1_001.Nimi");
    }
}
