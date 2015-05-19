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
                .when(URLCommunicator.makeGetRequest(Mockito.anyString(), Mockito.anyString()))
                .thenReturn(fakeResult);
    }

    @Test
    public void parsesCourseNamesCorrectly() {
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

}
