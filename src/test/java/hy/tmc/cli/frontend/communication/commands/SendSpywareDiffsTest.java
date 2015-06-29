package hy.tmc.cli.frontend.communication.commands;

import com.google.common.base.Optional;
import hy.tmc.cli.backend.communication.HttpResult;
import hy.tmc.cli.backend.communication.TmcJsonParser;
import hy.tmc.cli.domain.Course;
import hy.tmc.cli.frontend.communication.server.ProtocolException;
import hy.tmc.cli.spyware.DiffSender;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.junit.Before;
import org.junit.runner.RunWith;
import static org.mockito.Matchers.eq;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(TmcJsonParser.class)
public class SendSpywareDiffsTest {
    
    private SendSpywareDiffs spyware;
    private String spywareUrl = "https://vakoiluservu.org";
    
    @Before
    public void setup() throws IOException, ProtocolException {
       PowerMockito.mockStatic(TmcJsonParser.class);
       
       this.spyware = new SendSpywareDiffs(new byte[55], 313, mockDiffSenderWithStatusCode(200));
       Course fakeCourse = new Course();
       mockTmcJsonParser(Optional.of(fakeCourse));
    }
    
    @Test(expected = ProtocolException.class)
    public void checkDataWhenNoCourseFound() throws Exception {
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
        mockTmcJsonParser(Optional.<Course>absent());
        spyware.call();
    }
    
    @Test
    public void callMethodWithStatus500ReturnsFalse() throws Exception {
       SendSpywareDiffs spy = new SendSpywareDiffs(new byte[55], 313, 
                            mockDiffSenderWithStatusCode(500));
       assertFalse(spy.call());
    }
    
    @Test
    public void callMethodWithStatus200ReturnsTrue() throws Exception {
        assertTrue(spyware.call());
    }
    
    private DiffSender mockDiffSenderWithStatusCode(int statusCode) throws ProtocolException {
        List<HttpResult> fakeResult = new ArrayList<>();
        fakeResult.add(new HttpResult("SendSpywareDiff-command checks only statuscode.", statusCode, true));
        
        DiffSender diffSenderMock = Mockito.mock(DiffSender.class);   
        Mockito.when(diffSenderMock.sendToSpyware(
                Mockito.any(byte[].class), Mockito.any(Course.class)
        )).thenReturn(fakeResult);
        return diffSenderMock;
    }

    private void mockTmcJsonParser(Optional<Course> fakeCourse) throws IOException, ProtocolException {
        PowerMockito
                .when(TmcJsonParser.getCourse(eq(313)))
                .thenReturn(fakeCourse);
    }
}
