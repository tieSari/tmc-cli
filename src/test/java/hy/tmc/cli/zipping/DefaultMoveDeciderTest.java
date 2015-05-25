package hy.tmc.cli.zipping;

import java.nio.file.Paths;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class DefaultMoveDeciderTest {

    private DefaultMoveDecider decider;

    @Before
    public void setUp() {
        decider = new DefaultMoveDecider();
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of readTmcprojectYml method, of class DefaultMoveDecider.
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
