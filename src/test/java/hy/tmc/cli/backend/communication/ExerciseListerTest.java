package hy.tmc.cli.backend.communication;

import com.google.common.base.Optional;
import hy.tmc.cli.configuration.ClientData;
import hy.tmc.cli.domain.Course;
import hy.tmc.cli.domain.Exercise;
import hy.tmc.cli.zipping.ProjectRootFinder;
import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(TmcJsonParser.class)
public class ExerciseListerTest {

    String fakeName = "2014-mooc-no-deadline";
    String otherFakeName = "2013-tira";
    ProjectRootFinder rootFinderMock;
    Course fakeCourse;
    ExerciseLister lister;
    Exercise fakeExercise;
    Exercise fakeExercise2;

    @Before
    public void setUp() {
        ClientData.setUserData("chang", "paras");
        setupFakeCourses();

        rootFinderMock = Mockito.mock(ProjectRootFinder.class);
        Mockito.when(rootFinderMock.getCurrentCourse(Mockito.anyString()))
                .thenReturn(Optional.of(fakeCourse));
        lister = new ExerciseLister(rootFinderMock);

        PowerMockito.mockStatic(TmcJsonParser.class);

        mockExercisesWith(setupFakeExercises());

    }

    private void mockExercisesWith(List<Exercise> exercises) {
        PowerMockito
                .when(TmcJsonParser.getExercises((Course) Mockito.any()))
                .thenReturn(exercises);
    }

    private List<Exercise> setupFakeExercises() {
        List<Exercise> exercises = new ArrayList();

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
    public void ifNoCourseIsFoundThenReturnsCorrectString() {
        String correct = "No course found";
        Mockito.when(rootFinderMock.getCurrentCourse(Mockito.anyString()))
                .thenReturn(Optional.<Course>absent());
        assertEquals(correct, lister.listExercises("any"));
    }

    @Test
    public void ifNoExercisesFoundThenReturnsCorrectString() {
        String correct = "No exercises found";

        mockExercisesWith(null);

        assertEquals(correct, lister.listExercises("any"));
    }

    @Test
    public void withCorrectCourseAndExercisesOutputContainsNames() {
        assertTrue(lister.listExercises("any").contains("Nimi"));
        assertTrue(lister.listExercises("any").contains("Kuusi"));

    }

    @Test
    public void withOneDoneExerciseOutputContainsX() {
        assertTrue(lister.listExercises("any").contains("x"));
    }

    @Test
    public void withNoDoneExercisesOutputContainsNoX() {
        List<Exercise> exercises = new ArrayList();

        exercises.add(new Exercise());
        exercises.add(new Exercise());
        mockExercisesWith(exercises);

        assertFalse(lister.listExercises("any").contains("x"));
    }

    @Test
    public void withOneAttemptedExerciseOutputContainsNoX() {
        List<Exercise> exercises = new ArrayList();

        exercises.add(new Exercise());
        Exercise ex = new Exercise();
        ex.setAttempted(true);
        exercises.add(ex);

        mockExercisesWith(exercises);
        assertFalse(lister.listExercises("any").contains("x"));

    }
}
