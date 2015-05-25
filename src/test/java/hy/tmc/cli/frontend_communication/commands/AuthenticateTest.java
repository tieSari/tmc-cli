package hy.tmc.cli.frontend_communication.commands;

import hy.tmc.cli.frontend.communication.commands.Authenticate;
import hy.tmc.cli.backendCommunication.Authorization.Authorization;
import hy.tmc.cli.backendCommunication.HTTPResult;
import hy.tmc.cli.backendCommunication.URLCommunicator;
import hy.tmc.cli.testhelpers.FrontendStub;
import hy.tmc.cli.frontend.communication.server.ProtocolException;
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
    private FrontendStub serverMock;

    @Before
    public void setUp() {
        Logic logic = new Logic();
        this.serverMock = new FrontendStub();
        this.auth = new Authenticate(serverMock, logic);
    }

    @Test
    public void canAuthenticateWithTestCredentials() throws ProtocolException {
        new Authorization(); // for code coverage
        String result = executeWithParams("username", testUsername, "password", testPassword);
        assertTrue(result.contains("Auth successful."));
    }

    @Test
    public void cannotAuthenticateWithUnexistantCredentials() throws ProtocolException {
        String result = executeWithParams("username", "samu", "password", "salis");
        assertTrue(result.contains("Auth unsuccessful."));
    }

    @Test(expected = Exception.class)
    public void failsWithWrongKeys() throws ProtocolException {
        executeWithParams("usernamee", testUsername, "passwordi", testPassword);
    }

    private String executeWithParams(String key1, String param1,
            String key2, String param2) throws ProtocolException {
        
        auth.setParameter(key1, param1);
        auth.setParameter(key2, param2);
        PowerMockito.mockStatic(URLCommunicator.class);
        powerMockWithCredentials("test:1234", 200);
        powerMockWithCredentials("samu:salis", 400);
        
        auth.execute();
        String result = serverMock.getMostRecentLine();
        return result;
    }

    private void powerMockWithCredentials(String credentials, int status) {
        HTTPResult fakeResult = new HTTPResult("", status, true);
        PowerMockito
                .when(URLCommunicator.makeGetRequest(Mockito.eq(URLCommunicator.createClient()),
                        Mockito.anyString(),
                        Mockito.eq(credentials)))
                .thenReturn(fakeResult);
    }

}
