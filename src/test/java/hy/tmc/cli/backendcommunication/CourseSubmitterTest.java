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

    @Before
    public void setup() {
        this.courseSubmitter = new CourseSubmitter(new ProjectRootFinder(new DefaultRootDetector()));
    }

    @Test
    public void testLastDirectory() {
        String[] names = courseSubmitter.getExerciseName("");

    }

}
