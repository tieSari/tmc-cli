package hy.tmc.cli.zipping;

import java.nio.file.Paths;
import java.util.List;
import org.junit.*;
import static org.junit.Assert.*;

public class DefaultMoveDeciderTest {

    private DefaultUnzipDecider decider;

    @Before
    public void setUp() {
        decider = new DefaultUnzipDecider();
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of readTmcprojectYml method, of class DefaultUnzipDecider.
     */
    @Test
    public void testReadTmcprojectYml() {
        decider.readTmcprojectYml(Paths.get("testResources/.tmcproject.yml"));
        List<String> excludePaths = decider.additionalStudentFiles;
        if (excludePaths == null) {
            fail("failed to read yml file");
        }
        assertTrue(excludePaths.contains("test/StudentTest.java"));
    }

    @Test
    public void emptyPathDoesNothing() {
        decider.readTmcprojectYml(Paths.get(""));
        assertTrue(decider.additionalStudentFiles.isEmpty());
    }
}
