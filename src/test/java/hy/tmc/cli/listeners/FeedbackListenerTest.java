package hy.tmc.cli.listeners;

import hy.tmc.core.communication.HttpResult;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class FeedbackListenerTest {

    private FeedbackListener fbl;

    @Before
    public void setUp() {
        fbl = new FeedbackListener(null, null, null);
    }

    @Test
    public void parseDataTestStatusOk() {
        HttpResult result = new HttpResult("{status:ok}", 200, true);
        String expected = "Your feedback was received succesfully. Thank you for your answers.";
        assertEquals(expected, fbl.parseData(result).get());
    }

    @Test
    public void parseDataTestResultNotSuccessful() {
        HttpResult result = new HttpResult("", 404, false);
        String expected = "Error 404 Sending the feedback failed.";
        assertEquals(expected, fbl.parseData(result).get());
    }

    @Test
    public void parseDataTestReplyNotStatusOk() {
        HttpResult result = new HttpResult("status:fail", 200, true);
        String expected = "Sending the feedback failed: status:fail";
        assertEquals(expected, fbl.parseData(result).get());
    }

}
