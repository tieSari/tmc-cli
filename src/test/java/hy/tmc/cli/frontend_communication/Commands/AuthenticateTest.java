
package hy.tmc.cli.frontend_communication.Commands;

import hy.tmc.cli.testhelpers.FrontendMock;
import hy.tmc.cli.frontend_communication.FrontendListener;
import hy.tmc.cli.frontend_communication.Server.ProtocolException;
import hy.tmc.cli.logic.Logic;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author samutamm
 */
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
    
    @Test
    public void failsWithWrongKeys() throws ProtocolException {
        String result = executeWithParams("usernamee", testUsername, "passwordi", testPassword);
        assertTrue(result.contains("Auth unsuccessful."));
    }

    private String executeWithParams(String key1, String param1,
                                     String key2, String param2) {
        auth.setParameter(key1, param1);
        auth.setParameter(key2, param2);
        auth.execute();
        FrontendMock mock =  serverMock;
        String result = mock.getMostRecentLine();
        return result;
    }
    
    
}


