package hy.tmc.cli.backendcommunication;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import hy.tmc.cli.configuration.ClientData;
import hy.tmc.cli.testhelpers.ExampleJSON;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;


@RunWith(PowerMockRunner.class)
@PrepareForTest(UrlCommunicator.class)
public class JsonParserTest {

    /**
     * Mocks UrlCommunicator.
     */
    @Before
    public void setup() {
        PowerMockito.mockStatic(UrlCommunicator.class);

        HttpResult fakeResult = new HttpResult(ExampleJSON.allCoursesExample, 200, true);
        
        ClientData.setUserData("chang", "paras");
        PowerMockito
                .when(UrlCommunicator.makeGetRequest(Mockito.anyString(), 
                                                    Mockito.anyString()))
                .thenReturn(fakeResult);
        
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

    @Test
    public void getsExercisesCorrectlyFromCourseJson() {
        HttpResult fakeResult = new HttpResult(ExampleJSON.courseExample, 200, true);
        PowerMockito
                .when(UrlCommunicator.makeGetRequest(Mockito.eq("ankka"), 
                                                    Mockito.anyString()))
                .thenReturn(fakeResult);
        String names = TmcJsonParser.getExerciseNames("ankka");

        assertTrue(names.contains("viikko1-Viikko1_001.Nimi"));
        assertTrue(names.contains("viikko1-Viikko1_002.HeiMaailma"));
        assertTrue(names.contains("viikko1-Viikko1_003.Kuusi"));
    }
    
    @Test
    public void getsLastExerciseOfCourseJson() {
        HttpResult fakeResult = new HttpResult(ExampleJSON.courseExample, 200, true);
        PowerMockito
                .when(UrlCommunicator.makeGetRequest(Mockito.eq("ankka"), 
                                                    Mockito.anyString()))
                .thenReturn(fakeResult);
        String names = TmcJsonParser.getExerciseNames("ankka");
        
        assertTrue(names.contains("viikko11-Viikko11_147.Laskin"));
    }
    
    @Test
    public void canFetchOneCourse(){
        // TODO: implement!
    }

}
