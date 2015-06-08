package hy.tmc.cli.backendcommunication;

import com.google.common.base.Optional;
import hy.tmc.cli.backend.communication.HttpResult;
import hy.tmc.cli.backend.communication.TmcJsonParser;
import hy.tmc.cli.backend.communication.UrlCommunicator;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import hy.tmc.cli.configuration.ClientData;
import hy.tmc.cli.domain.Course;
import hy.tmc.cli.domain.submission.SubmissionResult;
import hy.tmc.cli.testhelpers.ExampleJson;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(UrlCommunicator.class)
public class JSONParserTest {

    String realAddress = "http://real.address.fi";

    @Before
    public void setup() {
        PowerMockito.mockStatic(UrlCommunicator.class);

        HttpResult fakeResult = new HttpResult(ExampleJson.allCoursesExample, 200, true);

        ClientData.setUserData("chang", "paras");
        PowerMockito
                .when(UrlCommunicator.makeGetRequest(Mockito.anyString(),
                                Mockito.anyString()))
                .thenReturn(fakeResult);
    }

    @After
    public void teardown() {
        ClientData.clearUserData();
    }

    @Test
    public void parsesCourseNamesCorrectly() {
        new TmcJsonParser();
        String courses = TmcJsonParser.getCourseNames();
        assertTrue(courses.contains("s2014-tira"));
        assertTrue(courses.contains("k2015-ohpe"));
        assertTrue(courses.contains("checkstyle-demo"));
        assertTrue(courses.contains("2014-mooc-no-deadline"));
        assertTrue(courses.contains("c-demo"));
    }

    @Test
    public void coursesDontContainWeirdNames() {
        String courses = TmcJsonParser.getCourseNames();
        assertFalse(courses.contains("Chang"));
        assertFalse(courses.contains("Ilari"));
        assertFalse(courses.contains("Pihla"));
        assertFalse(courses.contains("Kristian"));
        assertFalse(courses.contains("Samu"));
        assertFalse(courses.contains("Jani"));
    }

    private void mockCourse(String url) {
        HttpResult fakeResult = new HttpResult(ExampleJson.courseExample, 200, true);
        PowerMockito
                .when(UrlCommunicator.makeGetRequest(Mockito.eq(url),
                                Mockito.anyString()))
                .thenReturn(fakeResult);
    }

    @Test
    public void getsExercisesCorrectlyFromCourseJSON() {
        mockCourse(realAddress);
        String names = TmcJsonParser.getExerciseNames(realAddress);

        assertTrue(names.contains("viikko1-Viikko1_001.Nimi"));
        assertTrue(names.contains("viikko1-Viikko1_002.HeiMaailma"));
        assertTrue(names.contains("viikko1-Viikko1_003.Kuusi"));
    }

    @Test
    public void getsLastExerciseOfCourseJSON() {
        mockCourse(realAddress);
        String names = TmcJsonParser.getExerciseNames(realAddress);

        assertTrue(names.contains("viikko11-Viikko11_147.Laskin"));
    }

    @Test
    public void canFetchOneCourse() {
        HttpResult fakeResult = new HttpResult(ExampleJson.courseExample, 200, true);
        PowerMockito
                .when(UrlCommunicator.makeGetRequest(Mockito.anyString(),
                                Mockito.anyString()))
                .thenReturn(fakeResult);

        Optional<Course> course = TmcJsonParser.getCourse(3);
        assertTrue(course.isPresent());
        assertEquals("2013_ohpeJaOhja", course.get().getName());

    }

    private void mockSubmissionUrl() {
        PowerMockito.mockStatic(UrlCommunicator.class);

        HttpResult fakeResult = new HttpResult(ExampleJson.successfulSubmission, 200, true);
        PowerMockito
                .when(UrlCommunicator.makeGetRequest(Mockito.anyString(),
                                Mockito.anyString()))
                .thenReturn(fakeResult);
    }

    @Test
    public void canFetchSubmissionData() {
        mockSubmissionUrl();
        SubmissionResult result = TmcJsonParser.getSubmissionResult("http://real.address.fi");
        assertNotNull(result);
        assertEquals("2014-mooc-no-deadline", result.getCourse());
    }

}
