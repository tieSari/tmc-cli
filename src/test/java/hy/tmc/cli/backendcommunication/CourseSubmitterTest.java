package hy.tmc.cli.backendcommunication;

import hy.tmc.cli.configuration.ClientData;
import hy.tmc.cli.configuration.ConfigHandler;
import hy.tmc.cli.domain.Course;
import hy.tmc.cli.testhelpers.ExampleJson;
import hy.tmc.cli.testhelpers.ProjectRootFinderStub;
import hy.tmc.cli.testhelpers.ZipperStub;
import java.io.File;
import java.io.IOException;
import org.junit.After;
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
    private ProjectRootFinderStub rootFinder;

    @Before
    public void setup() throws IOException {
        new ConfigHandler().writeServerAddress("http://mooc.fi/staging");
        PowerMockito.mockStatic(UrlCommunicator.class);
        rootFinder = new ProjectRootFinderStub();
        this.courseSubmitter = new CourseSubmitter(rootFinder, new ZipperStub());
        ClientData.setUserData("chang", "rajani");
        
        mockUrlCommunicator("/courses.json?api_version=7", ExampleJson.allCoursesExample);
        mockUrlCommunicator("courses/3.json?api_version=7", ExampleJson.courseExample);
        mockUrlCommunicatorWithFile("https://tmc.mooc.fi/staging/exercises/285/submissions.json?api_version=7", ExampleJson.submitResponse);
    }
    
    @After
    public void clear() {
        ClientData.clearUserData();
    }

    @Test
    public void testGetExerciseName() {
        final String path = "/home/test/ohpe-test/viikko_01";
        rootFinder.setReturnValue(path);
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
    public void testSubmitWithOneParam() throws IOException {
        String testPath = "/home/test/2013_ohpeJaOhja/viikko_01/viikko1-Viikko1_001.Nimi";
        rootFinder.setReturnValue(testPath);
        String exercise = "viikko1-Viikko1_001.Nimi";
        String submissionPath = "http://127.0.0.1:8080/submissions/1781.json?api_version=7";
        String result = courseSubmitter.submit(testPath);
        assertEquals(submissionPath, result);
    }
    
    @Test (expected=IllegalArgumentException.class)
    public void testSubmitWithNonexistentExercise() throws IOException {
        String testPath = "/home/test/2013_ohpeJaOhja/viikko_01/feikkitehtava";
        rootFinder.setReturnValue(testPath);
        String result = courseSubmitter.submit(testPath);
        assertNull(result);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void submitWithNonExistentCourseThrowsException() throws IOException {
        String testPath = "/home/test/2013_FEIKKIKURSSI/viikko_01/viikko1-Viikko1_001.Nimi";
        rootFinder.setReturnValue(testPath);
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
