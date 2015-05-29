package hy.tmc.cli.frontend.communication.commands;

import static org.junit.Assert.assertTrue;

import hy.tmc.cli.backendcommunication.HttpResult;
import hy.tmc.cli.backendcommunication.UrlCommunicator;
import hy.tmc.cli.backendcommunication.authorization.Authorization;
import hy.tmc.cli.frontend.communication.server.ProtocolException;
import hy.tmc.cli.logic.Logic;
import hy.tmc.cli.testhelpers.FrontendStub;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.mockito.Mockito;

import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(UrlCommunicator.class)
public class AuthenticateTest {

    private final String testUsername = "test";
    private final String testPassword = "1234";
    private Authenticate auth;
    private FrontendStub serverMock;

    /**
     * Set up server mock and Authenticate command.
     */
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
        PowerMockito.mockStatic(UrlCommunicator.class);
        powerMockWithCredentials("test:1234", 200);
        powerMockWithCredentials("samu:salis", 400);
        
        auth.execute();
        String result = serverMock.getMostRecentLine();
        return result;
    }

    private void powerMockWithCredentials(String credentials, int status) {
        HttpResult fakeResult = new HttpResult("", status, true);
        PowerMockito
                .when(UrlCommunicator.makeGetRequest(
                        Mockito.anyString(),
                        Mockito.eq(credentials)))
                .thenReturn(fakeResult);
    }

}
