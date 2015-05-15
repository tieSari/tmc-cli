package hy.tmc.cli.backend_communication;

import hy.tmc.cli.backendCommunication.HTTPResult;
import hy.tmc.cli.backendCommunication.URLCommunicator;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 *
 * @author samutamm
 */

public class URLCommunicatorTest {
    
    @Test
    public void createGetMethodWorksWithValidParams() {
        HTTPResult result = URLCommunicator.makeGetRequest("https://tmc.mooc.fi/hy", "test:1234");
        assertEquals(result.getStatusCode(), 200);
    }
    
    @Test
    public void createGetMethodDoNotWorkWithoutValidParams() {
        HTTPResult result = URLCommunicator.makeGetRequest("https://tmc.mooc.fo", "test:1234");
        assertEquals(result.getStatusCode(), 400);
    }
    
    
    
}
