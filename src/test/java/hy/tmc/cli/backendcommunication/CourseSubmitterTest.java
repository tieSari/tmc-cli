package hy.tmc.cli.backendcommunication;

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
    }
    
    @Test
    public void firstTest() {
        String ex1 = this.courseSubmitter.getExerciseName("/home/samutamm/NetBeansProjects/s2014-tira");
        String ex2 = this.courseSubmitter.getExerciseName("/home/samutamm/NetBeansProjects/s2014-tira/viikko11-tira11.2");
        assertEquals("s2014-tira", ex1);
        assertEquals("s2014-tira", ex2);
    }
}
