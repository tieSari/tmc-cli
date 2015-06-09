package hy.tmc.cli.frontend.communication.commands;

import hy.tmc.cli.testhelpers.FrontendStub;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class ReplyToPingTest {

    @Test
    public void pingAnswersAsPong() {
        FrontendStub frontStub = new FrontendStub();
        ReplyToPing ping = new ReplyToPing();
        ping.functionality();
        assertEquals("pong", frontStub.getMostRecentLine());
    }
}
