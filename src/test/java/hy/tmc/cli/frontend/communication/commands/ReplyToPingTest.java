package hy.tmc.cli.frontend.communication.commands;

import hy.tmc.cli.frontend.communication.server.ProtocolException;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class ReplyToPingTest {

    @Test
    public void pingAnswersAsPong() throws ProtocolException {
        ReplyToPing ping = new ReplyToPing();
        assertEquals("pong", ping.call());
    }
}
