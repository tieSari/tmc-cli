/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hy.tmc.cli.zipping;

import java.nio.file.Paths;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author ilari
 */
public class DefaultMoveDeciderTest {
    
    private DefaultMoveDecider decider;
    
    public DefaultMoveDeciderTest() {
    }
    
    @Before
    public void setUp() {
        decider = new DefaultMoveDecider(new DefaultRootDetector());
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of readTmcprojectYml method, of class DefaultMoveDecider.
     */
    @Test
    public void testReadTmcprojectYml() {
        decider.readTmcprojectYml(Paths.get("testResources/mockProject"));
        List<String> excludePaths = decider.unoverwritablePaths;
        if (excludePaths == null){
            fail("failed to read yml file");
        }
        assertTrue(excludePaths.contains("test/StudentTest.java"));
    }
    
    @Test
    public void emptyPathDoesNothing(){
        decider.readTmcprojectYml(Paths.get(""));
        assertTrue(decider.unoverwritablePaths.isEmpty());
    }
    
    @Test
    public void ifTmcprojectFileDoesntExistDoesNothing() {
        decider.readTmcprojectYml(Paths.get("testResources/mockProject/rootWithoutYml"));
    }
    
    @Test
    public void doesNotReadWrongTmcprojectFile() {
        decider.readTmcprojectYml(Paths.get("testResources/mockProject"));
        List<String> excludePaths = decider.unoverwritablePaths;
        if (excludePaths == null){
            fail("failed to read yml file");
        }
        assertFalse(excludePaths.contains("duck.py"));
    }
    
}
