package hy.tmc.cli.backend_communication;

import hy.tmc.cli.Configuration.ClientData;
import hy.tmc.cli.backendCommunication.HTTPResult;
import hy.tmc.cli.backendCommunication.JSONParser;
import hy.tmc.cli.backendCommunication.URLCommunicator;
import hy.tmc.cli.testhelpers.ExampleJSON;
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
@PrepareForTest(URLCommunicator.class)
public class JSONParserTest {

    @Before
    public void setup() {
        PowerMockito.mockStatic(URLCommunicator.class);

        HTTPResult fakeResult = new HTTPResult(ExampleJSON.coursesExample, 200, true);
        
        ClientData.setUserData("chang", "paras");
        PowerMockito
                .when(URLCommunicator.makeGetRequest(Mockito.eq(URLCommunicator.createClient()),
                                                    Mockito.anyString(), 
                                                    Mockito.anyString()))
                .thenReturn(fakeResult);
        
    }

    @Test
    public void parsesCourseNamesCorrectly() {
        new JSONParser();
        String courses = JSONParser.getCourseNames();
        assertTrue(courses.contains("s2014-tira"));
        assertTrue(courses.contains("k2015-ohpe"));
        assertTrue(courses.contains("checkstyle-demo"));
        assertTrue(courses.contains("2014-mooc-no-deadline"));
        assertTrue(courses.contains("c-demo"));
    }

    @Test
    public void coursesDontContainWeirdNames() {
        String courses = JSONParser.getCourseNames();
        assertFalse(courses.contains("Chang"));
        assertFalse(courses.contains("Ilari"));
        assertFalse(courses.contains("Pihla"));
        assertFalse(courses.contains("Kristian"));
        assertFalse(courses.contains("Samu"));
        assertFalse(courses.contains("Jani"));
    }

    @Test
    public void getsExercisesCorrectlyFromCourseJSON() {
        HTTPResult fakeResult = new HTTPResult(ExampleJSON.courseExample, 200, true);
        PowerMockito
                .when(URLCommunicator.makeGetRequest(Mockito.eq(URLCommunicator.createClient()),
                                                    Mockito.eq("ankka"), 
                                                    Mockito.anyString()))
                .thenReturn(fakeResult);
        String names = JSONParser.getExerciseNames("ankka");

        assertTrue(names.contains("week7-week7_01.Smileys"));
        assertTrue(names.contains("week7-week7_02.CharacterStringChanger"));
        assertTrue(names.contains("week7-week7_03.Calculator"));
    }
    
    @Test
    public void getsLastExerciseOfCourseJSON() {
        HTTPResult fakeResult = new HTTPResult(ExampleJSON.courseExample, 200, true);
        PowerMockito
                .when(URLCommunicator.makeGetRequest(Mockito.eq(URLCommunicator.createClient()),
                                                    Mockito.eq("ankka"), 
                                                    Mockito.anyString()))
                .thenReturn(fakeResult);
        String names = JSONParser.getExerciseNames("ankka");
        
        assertTrue(names.contains("week7-week7_08.Airport"));
    }
    
    @Test
    public void canFetchOneCourse(){
        // TODO: implement!
    }

}
