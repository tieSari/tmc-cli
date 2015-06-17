package hy.tmc.cli.frontend.communication.commands;


import hy.tmc.cli.backend.Mailbox;
import hy.tmc.cli.configuration.ClientData;
import hy.tmc.cli.frontend.communication.server.ProtocolException;
import hy.tmc.cli.testhelpers.FrontendStub;
import hy.tmc.cli.testhelpers.MailExample;
import hy.tmc.cli.testhelpers.ProjectRootFinderStub;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;

import static org.junit.Assert.assertTrue;

public class MailCheckerTest {

    private MailChecker mailChecker;
    private FrontendStub frontendStub;

    @Before
    public void before() throws Exception {
        Mailbox.create();
        Mailbox.getMailbox().fill(MailExample.reviewExample());
        this.frontendStub = new FrontendStub();
        this.mailChecker = new MailChecker(frontendStub);
        ClientData.setProjectRootFinder(new ProjectRootFinderStub());

    }

    @After
    public void after() throws Exception {
        Mailbox.destroy();
    }

    @Test(expected = ProtocolException.class)
    public void notValidIfNoPathSpecified() throws Exception {
        mailChecker.checkData();
    }

    @Test(expected = ProtocolException.class)
    public void notValidIfNoMailbox() throws Exception {
        Mailbox.destroy();
        mailChecker.data.put("path", "asd");
        mailChecker.checkData();
    }

    @Test(expected = IllegalStateException.class)
    public void notValidIfNotLoggedIn() throws Exception {
        mailChecker.data.put("path", "asd");
        mailChecker.checkData();
    }

    @Test(expected = ProtocolException.class)
    public void notValidIfPathIsWeird() throws Exception {
        mailChecker.data.put("path", "asd");
        ClientData.setUserData("samu", "bossman");
        mailChecker.checkData();
    }

    @Test
    public void ifMailboxHasMessagesItPrintsThemToFrontend() {
        mailChecker.functionality();
        assertTrue(
                frontendStub.getMostRecentLine().contains(" unread code reviews")
        );

    }


} 
