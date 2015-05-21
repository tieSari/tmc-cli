/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hy.tmc.cli.zipping;

import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author ilari
 */
public class ProjectRootFinderTest {
    
    ProjectRootFinder finder;
    
    public ProjectRootFinderTest() {
    }
    
    
    @Before
    public void setUp() {
        finder = new ProjectRootFinder(new DefaultRootDetector());
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void testGetRootDirectory() {
        Path root = finder.getRootDirectory(Paths.get("testResources"));
        System.out.println(root);
        assertEquals("testResources/mockProject/root",root.toString());
    }
    
}
