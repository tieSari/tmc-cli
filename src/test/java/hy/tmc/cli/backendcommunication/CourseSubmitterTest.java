package hy.tmc.cli.backendcommunication;

import hy.tmc.cli.configuration.ClientData;
import hy.tmc.cli.testhelpers.ExampleJSON;
import hy.tmc.cli.testhelpers.ProjectRootFinderStub;
import hy.tmc.cli.testhelpers.ZipperStub;
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
    public void setup() {
        rootfinder = new ProjectRootFinderStub();
        this.courseSubmitter = new CourseSubmitter(rootfinder, new ZipperStub());
        mockUrlCommunicator("http://tmc.mooc.fi/staging/courses.json?api_version=7", ExampleJSON.allCoursesExample);
        System.out.println("TOIMIIKO? " + ExampleJSON.allCoursesExample);
        mockUrlCommunicator("http://tmc.mooc.fi/staging/courses/3.json?api_version=7", ExampleJSON.courseExample);
        wireMockTMCServer();
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
        String testZip = "testResources/test.zip";
        String testPath = "/home/test/2013_ohpeJaOhja/viikko_01";
        rootfinder.setReturnValue(testPath);
        String exercise = "viikko1-Viikko1_001.Nimi";
        
        String course = "2013_ohpeJaOhja";
        courseSubmitter.submit(testPath, exercise);
        
    }

    private void mockUrlCommunicator(String url, String returnValue) {
        PowerMockito.mockStatic(UrlCommunicator.class);
        HttpResult fakeResult = new HttpResult(returnValue, 200, true);
        PowerMockito
                .when(UrlCommunicator.makeGetRequest(Mockito.eq(url),
                                Mockito.anyString()))
                .thenReturn(fakeResult);
    }

    private void wireMockTMCServer() {
        
    }

}
