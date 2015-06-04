
package hy.tmc.cli.zipping;

import com.google.common.base.Optional;
import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;

import java.nio.file.Path;
import java.nio.file.Paths;


public class ProjectRootFinderTest {
    
    ProjectRootFinder finder;
    
    @Before
    public void setUp() {
        finder = new ProjectRootFinder(new DefaultRootDetector());
    }
    
    @After
    public void tearDown() {
    }

    public void testGetRootDirectory() {
        Optional<Path> root = finder.getRootDirectory(Paths.get("testResources/mockProject"));
        assertEquals("testResources/mockProject/root",root.get().toString());
    }
    
    public void testGetRootDirectory2() {
        Optional<Path> root = finder.getRootDirectory(Paths.get("testResources/noyml"));
        System.out.println(root);
        assertEquals("testResources/noyml/rootWithoutYml",root.get().toString());
    }
}
