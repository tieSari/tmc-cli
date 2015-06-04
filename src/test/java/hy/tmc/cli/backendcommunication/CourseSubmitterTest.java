package hy.tmc.cli.backendcommunication;

import com.google.common.base.Optional;
import static org.junit.Assert.assertEquals;

import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import hy.tmc.cli.backend.communication.CourseSubmitter;
import hy.tmc.cli.backend.communication.HttpResult;
import hy.tmc.cli.backend.communication.UrlCommunicator;
import hy.tmc.cli.configuration.ClientData;
import hy.tmc.cli.configuration.ConfigHandler;
import hy.tmc.cli.domain.Course;
import hy.tmc.cli.testhelpers.ExampleJson;
import hy.tmc.cli.testhelpers.ProjectRootFinderStub;
import hy.tmc.cli.testhelpers.ZipperStub;

import java.io.File;
import java.io.IOException;
import org.junit.After;
import static org.junit.Assert.assertFalse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(PowerMockRunner.class)
@PrepareForTest(UrlCommunicator.class)
public class CourseSubmitterTest {

    private CourseSubmitter courseSubmitter;
    private ProjectRootFinderStub rootFinder;

    /**
     * Mocks components that use Internet.
     */
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
        mockUrlCommunicatorWithFile("https://tmc.mooc.fi/staging/exercises/287/submissions.json?api_version=7", ExampleJson.pasteResponse);

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
        Optional<Course> course = courseSubmitter.findCourseByPath(path.split("/"));
        assertEquals(7, course.get().getId());
        final String path2 = "/home/kansio/toinen/OLEMATON/viikko_01";
        Optional<Course> course2 = courseSubmitter.findCourseByPath(path2.split("/"));
        assertFalse(course2.isPresent());
    }

    @Test
    public void testSubmitWithOneParam() throws IOException {
        String testPath = "/home/test/2013_ohpeJaOhja/viikko_01/viikko1-Viikko1_001.Nimi";
        rootFinder.setReturnValue(testPath);
        String submissionPath = "http://127.0.0.1:8080/submissions/1781.json?api_version=7";
        String result = courseSubmitter.submit(testPath);
        assertEquals(submissionPath, result);
    }

    @Test
    public void submitWithPasteReturnsPasteUrl() throws IOException {
        String testPath = "/home/test/2013_ohpeJaOhja/viikko_01/viikko1-Viikko1_003.Kuusi";
        rootFinder.setReturnValue(testPath);
        String pastePath = "https://tmc.mooc.fi/staging/paste/ynpw7_mZZGk3a9PPrMWOOQ";
        String result = courseSubmitter.submitPaste(testPath);
        assertEquals(pastePath, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void submitWithPasteFromBadPathThrowsException() throws IOException {
        String testPath = "/home/test/2013_ohpeJaOhja/viikko_01/feikeintehtava";
        rootFinder.setReturnValue(testPath);
        String result = courseSubmitter.submit(testPath);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitWithNonexistentExercise() throws IOException {
        String testPath = "/home/test/2013_ohpeJaOhja/viikko_01/feikkitehtava";
        rootFinder.setReturnValue(testPath);
        String result = courseSubmitter.submit(testPath);
    }

    @Test(expected = IllegalArgumentException.class)
    public void submitWithNonExistentCourseThrowsException() throws IOException {
        String testPath = "/home/test/2013_FEIKKIKURSSI/viikko_01/viikko1-Viikko1_001.Nimi";
        rootFinder.setReturnValue(testPath);
        String result = courseSubmitter.submit(testPath);
    }

    private void mockUrlCommunicator(String pieceOfUrl, String returnValue) {
        HttpResult fakeResult = new HttpResult(returnValue, 200, true);
        PowerMockito
                .when(UrlCommunicator.makeGetRequest(Mockito.contains(pieceOfUrl),
                                Mockito.anyString()))
                .thenReturn(fakeResult);
    }

    private void mockUrlCommunicatorWithFile(String url, String returnValue) throws IOException {
        HttpResult fakeResult = new HttpResult(returnValue, 200, true);
        PowerMockito
                .when(UrlCommunicator.makePostWithFile(Mockito.any(File.class),
                                Mockito.contains(url), Mockito.any(Optional.class)))
                .thenReturn(fakeResult);
    }
}
