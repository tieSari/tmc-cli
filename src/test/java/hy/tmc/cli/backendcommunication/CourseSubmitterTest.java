package hy.tmc.cli.backendcommunication;

import hy.tmc.cli.domain.Course;
import hy.tmc.cli.testhelpers.ExampleJSON;
import hy.tmc.cli.testhelpers.ProjectRootFinderStub;
import hy.tmc.cli.testhelpers.ZipperStub;
import java.io.File;
import java.io.IOException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(UrlCommunicator.class)
public class CourseSubmitterTest {

    private CourseSubmitter courseSubmitter;
    private ProjectRootFinderStub rootfinder;

    @Before
    public void setup() throws IOException {
        PowerMockito.mockStatic(UrlCommunicator.class);
        rootfinder = new ProjectRootFinderStub();
        this.courseSubmitter = new CourseSubmitter(rootfinder, new ZipperStub());
        
        mockUrlCommunicator("/courses.json?api_version=7", ExampleJSON.allCoursesExample);
        mockUrlCommunicator("courses/3.json?api_version=7", ExampleJSON.courseExample);
        mockUrlCommunicatorWithFile("https://tmc.mooc.fi/staging/exercises/285/submissions.json?api_version=7", ExampleJSON.submitResponse);
    }

    @Test
    public void testGetExerciseName() {
        final String path = "/home/test/ohpe-test/viikko_01";
        rootfinder.setReturnValue(path);
        String[] names = courseSubmitter.getExerciseName(path);
        assertEquals("viikko_01", names[names.length - 1]);
    }
    
    @Test
    public void testFindCourseByCorrectPath() {
        final String path = "/home/kansio/toinen/c-demo/viikko_01";
        Course course = courseSubmitter.findCourseByPath(path.split("/"));
        assertEquals(7, course.getId());
        final String path2 = "/home/kansio/toinen/OLEMATON/viikko_01";
        Course course2 = courseSubmitter.findCourseByPath(path2.split("/"));
        assertNull(null);
    }
    
    @Test
    public void testSubmitWithTwoParams() throws IOException {
        String testPath = "/home/test/2013_ohpeJaOhja/viikko_01/viikko1-Viikko1_001.Nimi";
        rootfinder.setReturnValue(testPath);
        String exercise = "viikko1-Viikko1_001.Nimi";
        String submissionPath = "http://127.0.0.1:8080/submissions/1781.json?api_version=7";
        String result = courseSubmitter.submit(testPath, exercise);
        assertEquals(submissionPath, result);
    }
    
    @Test
    public void testSubmitWithOneParam() throws IOException {
        String testPath = "/home/test/2013_ohpeJaOhja/viikko_01/viikko1-Viikko1_001.Nimi";
        rootfinder.setReturnValue(testPath);
        String exercise = "viikko1-Viikko1_001.Nimi";
        String submissionPath = "http://127.0.0.1:8080/submissions/1781.json?api_version=7";
        String result = courseSubmitter.submit(testPath);
        assertEquals(submissionPath, result);
    }
    
    @Test
    public void testSubmitWithNonexistentExercise() throws IOException {
        String testPath = "/home/test/2013_ohpeJaOhja/viikko_01/feikkitehtava";
        rootfinder.setReturnValue(testPath);
        String exercise = "viikko1-Viikko1_001.Nimi";
        String submissionPath = "http://127.0.0.1:8080/submissions/1781.json?api_version=7";
        String result = courseSubmitter.submit(testPath);
        assertNull(result);
    }
    
    public void submitWithNonExistantCourseReturnsNull() throws IOException {
        String testPath = "/home/test/2013_FEIKKIKURSSI/viikko_01/viikko1-Viikko1_001.Nimi";
        rootfinder.setReturnValue(testPath);
        String exercise = "viikko1-Viikko1_001.Nimi";
        String submissionPath = "http://127.0.0.1:8080/submissions/1781.json?api_version=7";
        String result = courseSubmitter.submit(testPath);
        assertNull(result);
    }

    private void mockUrlCommunicator(String pieceOfURL, String returnValue) {
        HttpResult fakeResult = new HttpResult(returnValue, 200, true);
        PowerMockito
                .when(UrlCommunicator.makeGetRequest(Mockito.contains(pieceOfURL),
                                Mockito.anyString()))
                .thenReturn(fakeResult);
    }

    private void mockUrlCommunicatorWithFile(String url, String returnValue) throws IOException {
        HttpResult fakeResult = new HttpResult(returnValue, 200, true);
        PowerMockito
                .when(UrlCommunicator.makePostWithFile(Mockito.any(File.class),
                                Mockito.contains(url)))
                .thenReturn(fakeResult);
    }
}
