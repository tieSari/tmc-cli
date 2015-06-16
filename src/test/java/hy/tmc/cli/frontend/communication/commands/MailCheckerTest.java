package hy.tmc.cli.frontend.communication.commands;

import hy.tmc.cli.backend.Mailbox;
import hy.tmc.cli.frontend.communication.server.ProtocolException;
import hy.tmc.cli.testhelpers.FrontendStub;
import hy.tmc.cli.testhelpers.MailExample;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;

public class MailCheckerTest {

    private MailChecker mailChecker;
    private FrontendStub frontendStub;

    @Before
    public void before() throws Exception {
        Mailbox.create();
        Mailbox.getMailbox().fill(MailExample.reviewExample());
        this.frontendStub = new FrontendStub();
        this.mailChecker = new MailChecker(frontendStub);

    }

    @After
    public void after() throws Exception {
        Mailbox.destroy();
    }

    @Test(expected = ProtocolException.class)
    public void notValidIfNoPathSpecified() throws Exception {
        mailChecker.checkData();
    }

    @Test
    public void testCheckData() throws Exception {

    }


} 
