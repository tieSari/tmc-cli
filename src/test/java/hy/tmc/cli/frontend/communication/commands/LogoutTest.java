package hy.tmc.cli.frontend.communication.commands;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import hy.tmc.cli.configuration.ClientData;
import hy.tmc.cli.frontend.communication.server.ProtocolException;
import hy.tmc.cli.testhelpers.FrontendStub;

import org.junit.Before;
import org.junit.Test;

public class LogoutTest {

    private FrontendStub front;
    private Command logout;

    @Before
    public void setup() {
        front = new FrontendStub();
        logout = new Logout(front);
    }

    @Test
    public void clearsUserData() {
        ClientData.setUserData("Chang", "Samu");
        try {
            logout.execute();
            assertFalse(ClientData.userDataExists());
        } catch (ProtocolException ex) {
            fail("Something went wrong");
        }
    }

    @Test
    public void messageCorrectIfLoggedOut() {
        ClientData.setUserData("Chang", "Paras");
        try {
            logout.execute();
            String response = front.getMostRecentLine();
            assertTrue(response.contains("clear"));
        } catch (ProtocolException ex) {
            fail("Something went wrong");
        }
    }

    @Test
    public void messageCorrectIfNobodyLoggedIn() {
        ClientData.clearUserData();
        try {
            logout.execute();
            String response = front.getMostRecentLine();
            assertTrue(response.contains("Nobody"));
        } catch (ProtocolException e) {
            fail("Something went wrong");
        }
    }

}
