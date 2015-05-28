package hy.tmc.cli.backendcommunication;

import hy.tmc.cli.backend.communication.CourseSubmitter;
import hy.tmc.cli.configuration.ClientData;
import hy.tmc.cli.domain.Course;
import hy.tmc.cli.testhelpers.ProjectRootFinderStub;
import hy.tmc.cli.zipping.DefaultRootDetector;
import hy.tmc.cli.zipping.ProjectRootFinder;
import java.io.IOException;
import java.util.Arrays;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

public class CourseSubmitterTest {

    private CourseSubmitter courseSubmitter;

    @Before
    public void setup() {
        ProjectRootFinderStub projectRootFinderStub = new ProjectRootFinderStub();
        this.courseSubmitter = new CourseSubmitter(projectRootFinderStub);
    }

    @Test
    public void testGetExerciseName() {
        String[] names = courseSubmitter.getExerciseName("/home/test/ohpe-test/viikko_01");
        assertEquals("viikko_01", names[names.length - 1]);
    }

}
