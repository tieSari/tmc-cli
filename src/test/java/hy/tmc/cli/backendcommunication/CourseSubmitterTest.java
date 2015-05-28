package hy.tmc.cli.backendcommunication;

import hy.tmc.cli.testhelpers.ExampleJSON;
import hy.tmc.cli.testhelpers.ProjectRootFinderStub;
import hy.tmc.cli.testhelpers.ZipperStub;
import java.io.File;
import java.io.IOException;
import static org.junit.Assert.assertEquals;
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
    }

    @Test
    public void testGetExerciseName() {
        final String path = "/home/test/ohpe-test/viikko_01";
        rootfinder.setReturnValue(path);
        String[] names = courseSubmitter.getExerciseName(path);
        assertEquals("viikko_01", names[names.length - 1]);
    }
    
    @Test
    public void testSubmit() throws IOException {
        mockUrlCommunicator("http://tmc.mooc.fi/staging/courses.json?api_version=7", ExampleJSON.allCoursesExample);
        mockUrlCommunicator("http://tmc.mooc.fi/staging/courses/3.json?api_version=7", ExampleJSON.courseExample);
        mockUrlCommunicatorWithFile("https://tmc.mooc.fi/staging/exercises/285/submissions.json?api_version=7", ExampleJSON.submitResponse);
        
        String testPath = "/home/test/2013_ohpeJaOhja/viikko_01/viikko1-Viikko1_001.Nimi";
        rootfinder.setReturnValue(testPath);
        String exercise = "viikko1-Viikko1_001.Nimi";
        String submissionPath = "https://tmc.mooc.fi/staging/submissions/1781.json?api_version=7";
        String result = courseSubmitter.submit(testPath, exercise);
        assertEquals(submissionPath, result);
    }

    private void mockUrlCommunicator(String url, String returnValue) {
        HttpResult fakeResult = new HttpResult(returnValue, 200, true);
        PowerMockito
                .when(UrlCommunicator.makeGetRequest(Mockito.eq(url),
                                Mockito.anyString()))
                .thenReturn(fakeResult);
    }

    private void mockUrlCommunicatorWithFile(String url, String returnValue) throws IOException {
        HttpResult fakeResult = new HttpResult(returnValue, 200, true);
        PowerMockito
                .when(UrlCommunicator.makePostWithFile(Mockito.any(File.class),
                                Mockito.eq(url)))
                .thenReturn(fakeResult);
    }
}
