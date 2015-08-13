package hy.tmc.cli.listeners;

import fi.helsinki.cs.tmc.core.domain.Exercise;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class DownloadExercisesListenerTest {

    private DownloadExercisesListener dll;

    @Before
    public void setUp() {
        dll = new DownloadExercisesListener(null, null, null);
    }

    @After 
    public void tearDown() {
    }

    @Test
    public void testParseData() {
        List<Exercise> exs = new ArrayList<>();
        exs.add(new Exercise());
        exs.add(new Exercise());
        exs.add(new Exercise());
        String expected = "Downloaded 3 exercises.";
        assertEquals(expected, dll.parseData(exs).get());
    }
}
