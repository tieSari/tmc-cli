package hy.tmc.cli.frontend_communication.Commands;

import hy.tmc.cli.backendCommunication.HTTPResult;
import hy.tmc.cli.backendCommunication.URLCommunicator;
import hy.tmc.cli.testhelpers.FrontendMock;
import hy.tmc.cli.frontend_communication.Server.ProtocolException;
import hy.tmc.cli.logic.Logic;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(URLCommunicator.class)
public class AuthenticateTest {

    final private String testUsername = "test";
    final private String testPassword = "1234";
    private Authenticate auth;
    private FrontendMock serverMock;

    @Before
    public void setUp() {
        Logic logic = new Logic();
        this.serverMock = new FrontendMock();
        this.auth = new Authenticate(serverMock, logic);
    }

    @Test
    public void canAuthenticateWithTestCredentials() throws ProtocolException {
        String result = executeWithParams("username", testUsername, "password", testPassword);
        assertTrue(result.contains("Auth successful."));
    }

    @Test
    public void cannotAuthenticateWithUnexistantCredentials() throws ProtocolException {
        String result = executeWithParams("username", "samu", "password", "salis");
        assertTrue(result.contains("Auth unsuccessful."));
    }

    @Test(expected = ProtocolException.class)
    public void failsWithWrongKeys() throws ProtocolException {
        executeWithParams("usernamee", testUsername, "passwordi", testPassword);
    }

    private String executeWithParams(String key1, String param1,
            String key2, String param2) throws ProtocolException {
        
        auth.setParameter(key1, param1);
        auth.setParameter(key2, param2);
        
        
        HTTPResult fakeResult = new HTTPResult("", 200, true);
        PowerMockito.mockStatic(URLCommunicator.class);
        PowerMockito
                .when(URLCommunicator.makeGetRequest(URLCommunicator.createClient(),
                                                    Mockito.anyString(), 
                                                    Mockito.anyString()))
                .thenReturn(fakeResult);
        
        
        auth.execute();
        FrontendMock mock = serverMock;
        String result = mock.getMostRecentLine();
        return result;
    }

}
