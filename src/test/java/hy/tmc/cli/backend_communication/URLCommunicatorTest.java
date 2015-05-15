package hy.tmc.cli.backend_communication;

import hy.tmc.cli.backendCommunication.URLCommunicator;
import org.apache.http.client.HttpClient;
import org.junit.Test;
import static org.mockito.Matchers.anyObject;
import org.mockito.Mockito;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 *
 * @author samutamm
 */
public class URLCommunicatorTest {
    
    private HttpClient clientMock;

    public URLCommunicatorTest() {
        this.clientMock = mock(HttpClient.class);
    }
    
    @Test
    public void createGetMethodWorks() {
        TestURLCommunicator
    }
    
    
}
