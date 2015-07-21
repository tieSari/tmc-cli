package hy.tmc.cli.backend.communication;

import com.google.common.base.Optional;
import hy.tmc.cli.configuration.ClientData;
import hy.tmc.cli.frontend.communication.server.ProtocolException;
import hy.tmc.core.communication.TmcJsonParser;
import hy.tmc.core.domain.Course;
import hy.tmc.core.domain.Exercise;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;

public class ExerciseListerTest {

    String fakeName = "2014-mooc-no-deadline";
    String otherFakeName = "2013-tira";
    Course fakeCourse;
    ExerciseLister lister;
    Exercise fakeExercise;
    Exercise fakeExercise2;

    @Before
    public void setUp() throws IOException, ProtocolException {
        ClientData.setUserData("chang", "paras");
        setupFakeCourses();

        lister = new ExerciseLister();

        PowerMockito.mockStatic(TmcJsonParser.class);

        mockExercisesWith(setupFakeExercises());

    }

    private List<Exercise> setupFakeExercises() {
        List<Exercise> exercises = new ArrayList<>();

        fakeExercise = new Exercise();
        fakeExercise.setName("Nimi");

        fakeExercise2 = new Exercise();
        fakeExercise2.setName("Kuusi");
        fakeExercise2.setCompleted(true);

        exercises.add(fakeExercise);
        exercises.add(fakeExercise2);
        return exercises;
    }

    private void setupFakeCourses() {
        fakeCourse = new Course();
        fakeCourse.setName(fakeName);
        fakeCourse.setId(99);
    }

    @After
    public void tearDown() {
        ClientData.clearUserData();
    }

    @Test
    public void withCorrectCourseAndExercisesOutputContainsNames() throws ProtocolException, IOException {
        assertTrue(lister.buildExercisesInfo(exampleExercises()).contains("Nimi"));
        assertTrue(lister.buildExercisesInfo(exampleExercises()).contains("Kuusi"));
    }

    @Test
    public void withOneDoneExerciseOutputContainsX() throws ProtocolException, IOException {
        assertTrue(lister.buildExercisesInfo(exampleExercises()).contains("x"));
    }

    @Test
    public void withNoDoneExercisesOutputContainsNoX() throws ProtocolException, IOException {
        List<Exercise> exercises = new ArrayList<>();

        exercises.add(new Exercise());
        exercises.add(new Exercise());
        mockExercisesWith(exercises);

        assertFalse(lister.buildExercisesInfo(exampleExercises()).contains("x"));
    }

    @Test
    public void withOneAttemptedExerciseOutputContainsNoX() throws ProtocolException, IOException {
        List<Exercise> exercises = new ArrayList<>();

        exercises.add(new Exercise());
        Exercise ex = new Exercise();
        ex.setAttempted(true);
        exercises.add(ex);

        mockExercisesWith(exercises);
        assertFalse(lister.buildExercisesInfo(exampleExercises()).contains("x"));

    }
    
    @Test
    public void outputContainsPercentage() throws ProtocolException, IOException {
        List<Exercise> exercises = new ArrayList<>();

        exercises.add(new Exercise());
        Exercise ex = new Exercise();
        ex.setAttempted(true);
        exercises.add(ex);

        mockExercisesWith(exercises);
        assertTrue(lister.buildExercisesInfo(exampleExercises()).contains("%"));
        assertTrue(lister.buildExercisesInfo(exampleExercises()).contains("Attempted"));
        assertTrue(lister.buildExercisesInfo(exampleExercises()).contains("Total"));
    }

    private void mockExercisesWith(List<Exercise> setupFakeExercises) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private List<Exercise> exampleExercises() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
