package hy.tmc.cli.backend.communication;

<<<<<<< HEAD
<<<<<<< HEAD
import hy.tmc.cli.backend.Mailbox;
=======
import hy.tmc.cli.mail.Mailbox;
import hy.tmc.cli.domain.Course;
>>>>>>> d18562ad97c220fa44a570b6c137583e917575d0
=======
import hy.tmc.cli.mail.Mailbox;
>>>>>>> 6f0a156e8a5a06410f1f1f312e949c5877ace448
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
