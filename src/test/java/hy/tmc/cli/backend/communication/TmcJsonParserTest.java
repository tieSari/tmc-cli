package hy.tmc.cli.backend.communication;

import hy.tmc.cli.backend.communication.HttpResult;
import hy.tmc.cli.backend.communication.TmcJsonParser;
import hy.tmc.cli.backend.communication.UrlCommunicator;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import hy.tmc.cli.configuration.ClientData;
import hy.tmc.cli.configuration.ConfigHandler;
import hy.tmc.cli.synchronization.TmcServiceScheduler;
import hy.tmc.cli.testhelpers.ExampleJson;
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
public class TmcJsonParserTest {

    /**
     * Mocks UrlCommunicator.
     */
    @Before
    public void setup() {
        TmcServiceScheduler.getScheduler().stop();
        PowerMockito.mockStatic(UrlCommunicator.class);

        HttpResult fakeResult = new HttpResult(ExampleJson.allCoursesExample, 200, true);
        ClientData.setUserData("chang", "paras");
        PowerMockito
                .when(UrlCommunicator.makeGetRequest(Mockito.anyString(),
                                Mockito.anyString()))
                .thenReturn(fakeResult);

    }

    @Test
<<<<<<< HEAD
    public void parsesCourseNamesCorrectly() {
        new TmcJsonParser();
        String courses = TmcJsonParser.getCourseNames(new ConfigHandler()
                .readCoursesAddress());
        assertTrue(courses.contains("s2014-tira"));
        assertTrue(courses.contains("k2015-ohpe"));
        assertTrue(courses.contains("checkstyle-demo"));
        assertTrue(courses.contains("2014-mooc-no-deadline"));
        assertTrue(courses.contains("c-demo"));
    }

    @Test
    public void coursesDontContainWeirdNames() {
        String courses = TmcJsonParser.getCourseNames(new ConfigHandler()
                .readCoursesAddress());
        assertFalse(courses.contains("Chang"));
        assertFalse(courses.contains("Ilari"));
        assertFalse(courses.contains("Pihla"));
        assertFalse(courses.contains("Kristian"));
        assertFalse(courses.contains("Samu"));
        assertFalse(courses.contains("Jani"));
    }

    @Test
=======
>>>>>>> 8455d444abaefe7ec9d4ee10e6298a287c2d1b65
    public void getsExercisesCorrectlyFromCourseJson() {
        HttpResult fakeResult = new HttpResult(ExampleJson.courseExample, 200, true);
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
        HttpResult fakeResult = new HttpResult(ExampleJson.courseExample, 200, true);
        PowerMockito
                .when(UrlCommunicator.makeGetRequest(Mockito.eq("ankka"),
                                Mockito.anyString()))
                .thenReturn(fakeResult);
        String names = TmcJsonParser.getExerciseNames("ankka");

        assertTrue(names.contains("viikko11-Viikko11_147.Laskin"));
    }

    @Test
    public void canFetchOneCourse() {
        // TODO: implement!
    }

    @Test
    public void parsesSubmissionUrlFromJson() {
        HttpResult fakeResult = new HttpResult(ExampleJson.submitResponse, 200, true);
        PowerMockito
                .when(UrlCommunicator.makeGetRequest(Mockito.anyString(),
                                Mockito.anyString()))
                .thenReturn(fakeResult);
        assertEquals("http://127.0.0.1:8080/submissions/1781.json?api_version=7", TmcJsonParser.getSubmissionUrl(fakeResult));
    }

    @Test
    public void parsesPasteUrlFromJson() {
        HttpResult fakeResult = new HttpResult(ExampleJson.pasteResponse, 200, true);
        PowerMockito
                .when(UrlCommunicator.makeGetRequest(Mockito.anyString(),
                                Mockito.anyString()))
                .thenReturn(fakeResult);
        assertEquals("https://tmc.mooc.fi/staging/paste/ynpw7_mZZGk3a9PPrMWOOQ", TmcJsonParser.getPasteUrl(fakeResult));
    }

}
