package hy.tmc.cli.frontend.communication.commands;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import hy.tmc.cli.configuration.ClientData;
import hy.tmc.cli.frontend.communication.server.ProtocolException;

import org.junit.Before;
import org.junit.Test;

public class LogoutTest {

    private Command logout;

    @Before
    public void setup() {
        logout = new Logout();
    }

    @Test
    public void clearsUserData() {
        ClientData.setUserData("Chang", "Samu");
        try {
            logout.call();
            assertFalse(ClientData.userDataExists());
        } catch (ProtocolException ex) {
            fail("Something went wrong");
        }
    }

    @Test
    public void messageCorrectIfLoggedOut() {
        ClientData.setUserData("Chang", "Paras");
        try {
            String response = logout.call();
            assertTrue(response.contains("clear"));
        } catch (ProtocolException ex) {
            fail("Something went wrong");
        }
    }

    @Test
    public void messageCorrectIfNobodyLoggedIn() {
        ClientData.clearUserData();
        try {
            String response = logout.call();
            assertTrue(response.contains("Nobody"));
        } catch (ProtocolException e) {
            fail("Something went wrong");
        }
    }
}
