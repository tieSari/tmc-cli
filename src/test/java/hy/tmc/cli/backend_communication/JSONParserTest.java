package hy.tmc.cli.backend_communication;

import hy.tmc.cli.Configuration.ClientData;
import hy.tmc.cli.backendCommunication.HTTPResult;
import hy.tmc.cli.backendCommunication.JSONParser;
import hy.tmc.cli.backendCommunication.URLCommunicator;
import hy.tmc.cli.testhelpers.ExampleJSON;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.mockito.Mockito.verify;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
    
@RunWith(PowerMockRunner.class)
@PrepareForTest(URLCommunicator.class)
public class JSONParserTest {
    
    @Test
    public void parsesCourseNamesCorrectly() {
        
        PowerMockito.mockStatic(URLCommunicator.class);
        
        HTTPResult fakeResult = new HTTPResult(ExampleJSON.coursesExample, 200, true);
        
        
        ClientData.setUserData("chang", "paras");
        PowerMockito
                .when(URLCommunicator.makeGetRequest("http://changonparas.com", ClientData.getFormattedUserData()))
                .thenReturn(fakeResult);
                
        String courses = JSONParser.getCourseNames();
        
        assertTrue(courses.contains("s2014-tira"));
        assertTrue(courses.contains("k2015-ohpe"));
        assertTrue(courses.contains("checkstyle-demo"));
        assertTrue(courses.contains("2014-mooc-no-deadline"));
        assertTrue(courses.contains("c-demo"));
    }
}
