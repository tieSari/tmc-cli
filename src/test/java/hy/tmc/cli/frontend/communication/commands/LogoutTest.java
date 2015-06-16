package hy.tmc.cli.frontend.communication.commands;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import hy.tmc.cli.configuration.ClientData;
import hy.tmc.cli.frontend.communication.server.ProtocolException;

import org.junit.Before;
import org.junit.Test;

public class LogoutTest {

    private Logout logout;

    @Before
    public void setup() {
        logout = new Logout();
    }

    @Test
    public void clearsUserData() throws ProtocolException {
        ClientData.setUserData("Chang", "Samu");
        logout.call();
        assertFalse(ClientData.userDataExists());
    }

    @Test
    public void messageCorrectIfLoggedOut() throws ProtocolException {
        ClientData.setUserData("Chang", "Paras");
        String response = logout.call();
        assertTrue(response.contains("clear"));
    }

    @Test
    public void messageCorrectIfNobodyLoggedIn() throws ProtocolException {
        ClientData.clearUserData();
        String response = logout.call();
        assertTrue(response.contains("Nobody"));
    }
}
