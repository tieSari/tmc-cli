/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hy.tmc.cli.domain;

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
public class CourseTest {
    
    private Course course;
    private final int id = 7;
    private final String name = "ankka";
    
    public CourseTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        course = new Course(id, name);
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void testGetName() {
        assertEquals(name, course.getName());
    }
    
    @Test
    public void testSetName() {
        course.setName("höyrykaivuri");
        assertEquals("höyrykaivuri", course.getName());
    }
    
    @Test
    public void testGetID(){
        assertEquals(id, course.getId());
    }
    
    @Test
    public void testSetID(){
        course.setId(888);
        assertEquals(888, course.getId());
    }
}
