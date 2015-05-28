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

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class CourseTest {
    
    private Course course;
    private final int id = 7;
    private final String name = "ankka";
    private List<Exercise> exercises;
    private Exercise ex;
    
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
        course = new Course();
        course.setId(id);
        course.setName(name);
        course.setDetailsUrl("http://mooc.fi/");

        exercises = new ArrayList<>();
        ex = new Exercise();
        ex.setName("test");
        ex.setId(2);
        exercises.add(ex);

        course.setExercises(exercises);
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

    @Test
    public void testGetExercises() {
        assertEquals(exercises,course.getExercises());
    }

    @Test
    public void testSetExercises() {
        List<Exercise> newExercises = new ArrayList<>();
        course.setExercises(newExercises);
        assertEquals(newExercises,course.getExercises());
    }

    @Test
    public void testGetDetailsUrl() {
        assertEquals("http://mooc.fi/", course.getDetailsUrl());
    }

    @Test
    public void testSetDetailsUrl() {
        course.setDetailsUrl("http://cs.helsinki.fi");
        assertEquals("http://cs.helsinki.fi", course.getDetailsUrl());
    }
}
