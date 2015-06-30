package hy.tmc.cli.frontend.communication.commands;

import com.google.common.base.Optional;
import hy.tmc.cli.backend.communication.HttpResult;
import hy.tmc.cli.backend.communication.TmcJsonParser;
import hy.tmc.cli.backend.communication.UrlCommunicator;
import hy.tmc.cli.domain.Course;
import hy.tmc.cli.frontend.communication.server.ProtocolException;
import hy.tmc.cli.spyware.DiffSender;
import hy.tmc.cli.testhelpers.ExampleJson;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.http.entity.mime.content.ContentBody;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.junit.Before;
import org.junit.runner.RunWith;
import static org.mockito.Matchers.*;
import static org.mockito.Matchers.eq;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({TmcJsonParser.class, UrlCommunicator.class})
public class SendSpywareDiffsTest {
    
    private SendSpywareDiffs spyware;
    private String spywareUrl = "http://staging.spyware.testmycode.net/";
    private byte[] byteArray = "a!¤(aflwj2n134oh1odho1w8d08shoh".getBytes();
    private int courseID = 313;
    
    @Before
    public void setup() throws IOException, ProtocolException {
       this.spyware = new SendSpywareDiffs(new byte[55], 313, mockDiffSenderWithStatusCode(200));
    }
    
    @Test(expected = ProtocolException.class)
    public void checkDataWhenNoCourseFound() throws Exception {
        Course fakeCourse = new Course();
        mockTmcJsonParser(Optional.of(fakeCourse));
        mockTmcJsonParser(Optional.<Course>absent());
        spyware.checkData();
    }
    
    @Test(expected = ProtocolException.class)
    public void checkDataWhenNullByteArray() throws Exception {
        this.spyware = new SendSpywareDiffs(null, 313);
        spyware.checkData();
    }
    
    @Test(expected = ProtocolException.class)
    public void callMethodCallsCheckData() throws Exception {
        Course fakeCourse = new Course();
        mockTmcJsonParser(Optional.of(fakeCourse));
        mockTmcJsonParser(Optional.<Course>absent());
        spyware.call();
    }
    
    @Test
    public void callMethodWithStatus500ReturnsFalse() throws Exception {
        Course fakeCourse = new Course();
        mockTmcJsonParser(Optional.of(fakeCourse));
       SendSpywareDiffs spy = new SendSpywareDiffs(new byte[55], 313, 
                            mockDiffSenderWithStatusCode(500));
       assertFalse(spy.call());
    }
    
    @Test
    public void callMethodWithStatus200ReturnsTrue() throws Exception {
        Course fakeCourse = new Course();
        mockTmcJsonParser(Optional.of(fakeCourse));
        assertTrue(spyware.call());
    }
    
    @Test
    public void callFindsCorrectCourse() throws Exception {
        spyware = new SendSpywareDiffs(byteArray, courseID);
        
        //PowerMockito.when(TmcJsonParser.getCourse(anyInt())).thenCallRealMethod();
        PowerMockito.mockStatic(UrlCommunicator.class);
        
        PowerMockito.when(UrlCommunicator.makeGetRequest(anyString(), any(String[].class)))
                .thenReturn(new HttpResult(ExampleJson.allCoursesExample, 200, true));
        
        PowerMockito.when(UrlCommunicator.makeGetRequest(eq("http://tmc.mooc.fi/staging/courses/313.json?api_version=7"), any(String[].class)))
                .thenReturn(new HttpResult(ExampleJson.courseExample.replace("284", "313") , 200, true));
        
        PowerMockito.when(UrlCommunicator.makePostWithFile(any(ContentBody.class), anyString(), any(Map.class)))
                .thenReturn(new HttpResult("ok", 500, true));
        
        PowerMockito.when(UrlCommunicator.makePostWithFile(any(ContentBody.class), eq(this.spywareUrl), any(Map.class)))
                .thenReturn(new HttpResult("ok", 200, true));
        
        Boolean call = spyware.call();
        assertTrue(call);
    }
    
    private DiffSender mockDiffSenderWithStatusCode(int statusCode) throws ProtocolException, IOException {
        List<HttpResult> fakeResult = new ArrayList<>();
        fakeResult.add(new HttpResult("SendSpywareDiff-command checks only statuscode.", statusCode, true));
        
        DiffSender diffSenderMock = Mockito.mock(DiffSender.class);   
        Mockito.when(diffSenderMock.sendToSpyware(
                Mockito.any(byte[].class), Mockito.any(Course.class)
        )).thenReturn(fakeResult);
        return diffSenderMock;
    }
    
    private void mockTmcJsonParser(Optional<Course> fakeCourse) throws IOException, ProtocolException {
        PowerMockito.mockStatic(TmcJsonParser.class);
        
        PowerMockito
                .when(TmcJsonParser.getCourse(eq(313)))
                .thenReturn(fakeCourse);
    }
}
