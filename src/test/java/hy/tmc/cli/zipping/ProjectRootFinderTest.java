package hy.tmc.cli.zipping;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

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

    @Test
    public void testGetRootDirectory() {
        Path root = finder.getRootDirectory(Paths.get("testResources/mockProject"));
        System.out.println(root);
        assertEquals("testResources/mockProject/root",root.toString());
    }
    
    @Test
    public void testGetRootDirectory2() {
        Path root = finder.getRootDirectory(Paths.get("testResources/noyml"));
        System.out.println(root);
        assertEquals("testResources/noyml/rootWithoutYml",root.toString());
    }
}
