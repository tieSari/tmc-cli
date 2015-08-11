package hy.tmc.cli.listeners;

import hy.tmc.cli.backend.communication.SubmissionInterpreter;
import fi.helsinki.cs.tmc.core.domain.submission.SubmissionResult;
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import org.mockito.Mockito;
import static org.powermock.api.mockito.PowerMockito.when;

public class SubmissionListenerTest {

    private SubmissionListener sl;
    private SubmissionInterpreter itptr;

    @Before
    public void setup() throws Exception {
        itptr = Mockito.mock(SubmissionInterpreter.class);
        when(itptr.summary(any(SubmissionResult.class), eq(true))).thenReturn("detailed");
        when(itptr.summary(any(SubmissionResult.class), eq(false))).thenReturn("not detailed");
    }

    @Test
    public void parseDataTest() {

        sl = new SubmissionListener(null, null, null, itptr, true);
        assertEquals("detailed", sl.parseData(new SubmissionResult()).get());
    }

    @Test
    public void parseDataTest2() {

        sl = new SubmissionListener(null, null, null, itptr, false);
        assertEquals("not detailed", sl.parseData(new SubmissionResult()).get());
    }  
}
