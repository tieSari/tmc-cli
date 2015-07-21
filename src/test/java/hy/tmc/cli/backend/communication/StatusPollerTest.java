package hy.tmc.cli.backend.communication;

import hy.tmc.cli.mail.Mailbox;
import hy.tmc.cli.domain.Course;
import hy.tmc.cli.synchronization.PollScheduler;
import hy.tmc.cli.testhelpers.MailExample;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;

@RunWith(PowerMockRunner.class)
@PrepareForTest(TmcJsonParser.class)
public class StatusPollerTest {

    private StatusPoller statusPoller;

    @Before
    public void before() throws Exception {
        Mailbox.create();
        statusPoller = new StatusPoller(new Course(), new PollScheduler(0, TimeUnit.SECONDS));
        PowerMockito.mockStatic(TmcJsonParser.class);
        PowerMockito.when(TmcJsonParser.getReviews(any(String.class)))
                .thenReturn(MailExample.reviewExample());
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
