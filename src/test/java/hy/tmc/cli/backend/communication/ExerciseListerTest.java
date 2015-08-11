package hy.tmc.cli.backend.communication;

import hy.tmc.cli.frontend.communication.server.ProtocolException;
import hy.tmc.cli.testhelpers.builders.ExerciseBuilder;
import fi.helsinki.cs.tmc.core.domain.Exercise;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

public class ExerciseListerTest {

    ExerciseLister lister;
    ExerciseBuilder builder;
    
    public ExerciseListerTest() {
        builder = new ExerciseBuilder();
    }

    @Before
    public void setUp() throws IOException, ProtocolException {
        lister = new ExerciseLister();
    }

    @Test
    public void exercisesOutputContainsNames() throws ProtocolException, IOException {
        String output = lister.buildExercisesInfo(exampleExercises());
        assertTrue(output.contains("Nimi"));
        assertTrue(output.contains("Kuusi"));
        assertTrue(output.contains("Hiekkalaatikko"));
        assertTrue(output.contains("Ankka"));
    }

    @Test
    public void withOneDoneExerciseOutputContainsX() throws ProtocolException, IOException {
        ArrayList<Exercise> ex = new ArrayList<>();
        ex.add(builder.asCompleted().build());
        assertTrue(lister.buildExercisesInfo(ex).contains("x"));
    }

    @Test
    public void withNoDoneExercisesOutputContainsNoX() throws ProtocolException, IOException {
        List<Exercise> exercises = new ArrayList<>();

        exercises.add(new Exercise());
        exercises.add(new Exercise());

        String output = lister.buildExercisesInfo(exercises);
        assertFalse(output.contains("x"));
    }

    @Test
    public void withOneAttemptedExerciseOutputContainsNoX() throws ProtocolException, IOException {
        List<Exercise> exercises = new ArrayList<>();

        exercises.add(new Exercise());
        Exercise ex = new Exercise();
        ex.setAttempted(true);
        exercises.add(ex);

        assertFalse(lister.buildExercisesInfo(exercises).contains("x"));

    }
    
    @Test
    public void outputSummaryIsCorrect() throws ProtocolException, IOException {
        List<Exercise> exercises = new ArrayList<>();

        exercises.add(new Exercise());
        Exercise ex = new Exercise();
        ex.setAttempted(true);
        exercises.add(ex);

        String info = lister.buildExercisesInfo(exampleExercises());
        System.out.println("Info: " + info);
        NumberFormat formatter = new DecimalFormat("#0.0");
        assertTrue(info.contains(formatter.format(25.0) + "%"));
        assertTrue(info.contains("Attempted: 2 ("+formatter.format(50.0)+"%)"));
        assertTrue(info.contains("Total: 4"));
    }

    private List<Exercise> exampleExercises() {
        List<Exercise> ex = new ArrayList<>();
        ex.add(builder.withName("Kuusi").asAttempted().build());
        ex.add(builder.withName("Hiekkalaatikko").asCompleted().build());
        ex.add(builder.withName("Ankka").asAttempted().build());
        ex.add(builder.withName("Nimi").build());
        return ex;
    }
}
