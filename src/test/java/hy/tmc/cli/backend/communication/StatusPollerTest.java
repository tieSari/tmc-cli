package hy.tmc.cli.backend.communication;

import hy.tmc.cli.backend.Mailbox;
import hy.tmc.cli.synchronization.PollScheduler;
import hy.tmc.core.domain.Course;
import org.junit.Test;
import org.junit.Before;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertTrue;

public class StatusPollerTest {

    private StatusPoller statusPoller;

    @Before
    public void before() throws Exception {
        Mailbox.create();
        statusPoller = new StatusPoller(new Course(), new PollScheduler(0, TimeUnit.SECONDS));
    }

    @Test
    public void afterOneIterationThereShouldBeMessagesPolled() throws Exception {
        statusPoller.runOneIteration();
        assertTrue(Mailbox.getMailbox().get().reviewsWaiting());
    }

    @Test(expected = IllegalStateException.class)
    public void failsIfNoMailBoxCreated() throws Exception {
        Mailbox.destroy();
        statusPoller.runOneIteration();
    }

} 
