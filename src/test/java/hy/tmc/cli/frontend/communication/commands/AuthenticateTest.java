package hy.tmc.cli.frontend.communication.commands;

import hy.tmc.cli.backend.communication.authorization.Authorization;
import hy.tmc.cli.frontend.communication.server.ProtocolException;
import java.io.IOException;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.mockito.Mockito;

import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

public class AuthenticateTest {

    private final String testUsername = "test";
    private final String testPassword = "1234";
    private Authenticate auth;

    /**
     * Set up server mock and Authenticate command.
     */
    @Before
    public void setUp() {
        this.auth = new Authenticate();
    }

    @Test
    public void canAuthenticateWithTestCredentials() throws ProtocolException, IOException {
        new Authorization(); // for code coverage
        String result = executeWithParams("username", testUsername, "password", testPassword);
        assertTrue(result.contains("Auth successful."));
    }

    @Test
    public void cannotAuthenticateWithUnexistantCredentials() throws ProtocolException, IOException {
        String result = executeWithParams("username", "samu", "password", "salis");
        assertTrue(result.contains("Auth unsuccessful."));
    }

    @Test(expected = ProtocolException.class)
    public void failsWithWrongKeys() throws ProtocolException, IOException {
        executeWithParams("usernamee", testUsername, "passwordi", testPassword);
    }

    private String executeWithParams(String key1, String param1,
            String key2, String param2) throws ProtocolException, IOException {

        auth.setParameter(key1, param1);
        auth.setParameter(key2, param2);
        PowerMockito.mockStatic(UrlCommunicator.class);
        powerMockWithCredentials("test:1234", 200);
        powerMockWithCredentials("samu:salis", 400);
        auth.checkData();
        return auth.parseData(auth.call()).get();

    }

    private void powerMockWithCredentials(String credentials, int status) throws IOException, ProtocolException {
        HttpResult fakeResult = new HttpResult("", status, true);
        PowerMockito
                .when(UrlCommunicator.makeGetRequest(
                                Mockito.anyString(),
                                Mockito.eq(credentials)))
                .thenReturn(fakeResult);
    }
}
