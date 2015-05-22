package hy.tmc.cli.domain;

import java.util.Date;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ExerciseTest {

    Exercise exercise;

    @Before
    public void setUp() {
        exercise = new Exercise();
    }

    @Test
    public void testId() {
        exercise.setId(1);
        assertEquals(1, exercise.getId());
    }

    @Test
    public void testName() {
        exercise.setName("ok");
        assertEquals("ok", exercise.getName());
    }

    @Test
    public void testLocked() {
        exercise.setLocked(true);
        assertEquals(true, exercise.isLocked());
    }

    @Test
    public void testDeadline_description() {
        exercise.setDeadline_description("asd");
        assertEquals("asd", exercise.getDeadline_description());
    }

    @Test
    public void testDeadline() {
        String dateNow = new Date().toString();
        exercise.setDeadline(dateNow);
        assertEquals(dateNow, exercise.getDeadline());
    }
}
