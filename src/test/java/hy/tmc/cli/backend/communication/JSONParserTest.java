package hy.tmc.cli.backend.communication;

import com.google.common.base.Optional;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import hy.tmc.cli.configuration.ClientData;
import hy.tmc.cli.domain.Course;
import hy.tmc.cli.domain.submission.SubmissionResult;
import hy.tmc.cli.testhelpers.ExampleJson;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.mockito.Matchers.contains;

import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(UrlCommunicator.class)
public class JSONParserTest {

    String realAddress = "http://real.address.fi";

    @Before
    public void setup() throws IOException {
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

    private void mockCourse(String url) throws IOException {
        HttpResult fakeResult = new HttpResult(ExampleJson.courseExample, 200, true);
        PowerMockito
                .when(UrlCommunicator.makeGetRequest(Mockito.eq(url),
                                Mockito.anyString()))
                .thenReturn(fakeResult);
    }

    @Test
    public void getsExercisesCorrectlyFromCourseJSON() throws IOException {
        mockCourse(realAddress);
        String names = TmcJsonParser.getExerciseNames(realAddress);

        assertTrue(names.contains("viikko1-Viikko1_001.Nimi"));
        assertTrue(names.contains("viikko1-Viikko1_002.HeiMaailma"));
        assertTrue(names.contains("viikko1-Viikko1_003.Kuusi"));
    }

    @Test
    public void getsLastExerciseOfCourseJSON() throws IOException {
        mockCourse(realAddress);
        String names = TmcJsonParser.getExerciseNames(realAddress);

        assertTrue(names.contains("viikko11-Viikko11_147.Laskin"));
    }

    @Test
    public void canFetchOneCourse() throws IOException {
        HttpResult fakeResult = new HttpResult(ExampleJson.courseExample, 200, true);
        PowerMockito
                .when(UrlCommunicator.makeGetRequest(contains("/courses/3"),
                                Mockito.anyString()))
                .thenReturn(fakeResult);

        Optional<Course> course = TmcJsonParser.getCourse(3);
        assertTrue(course.isPresent());
        assertEquals("2013_ohpeJaOhja", course.get().getName());

    }

    private void mockSubmissionUrl() throws IOException {
        PowerMockito.mockStatic(UrlCommunicator.class);

        HttpResult fakeResult = new HttpResult(ExampleJson.successfulSubmission, 200, true);
        PowerMockito
                .when(UrlCommunicator.makeGetRequest(Mockito.anyString(),
                                Mockito.anyString()))
                .thenReturn(fakeResult);
    }

    @Test
    public void canFetchSubmissionData() throws IOException {
        mockSubmissionUrl();
        SubmissionResult result = TmcJsonParser.getSubmissionResult("http://real.address.fi");
        assertNotNull(result);
        assertEquals("2014-mooc-no-deadline", result.getCourse());
    }
}
