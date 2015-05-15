package hy.tmc.cli.backend_communication;

import hy.tmc.cli.backendCommunication.URLCommunicator;
import org.apache.http.client.HttpClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.mockito.Matchers.anyObject;
import org.mockito.Mockito;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 *
 * @author samutamm
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(URLCommunicator.class)
public class URLCommunicatorTest {
    
    private HttpClient clientMock;

    public URLCommunicatorTest() {
        this.clientMock = mock(HttpClient.class);
    }
    
    @Test
    public void createGetMethodWorks() {
        PowerMockito.mockStatic(URLCommunicator.class);
        when(URLCommunicator.makeGetRequest(null, null));
    }
    
    
}
