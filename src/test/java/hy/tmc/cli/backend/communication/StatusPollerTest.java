package hy.tmc.cli.backend.communication;

import com.google.common.util.concurrent.ListenableFuture;
import hy.tmc.cli.TmcCli;
import hy.tmc.cli.mail.Mailbox;
import hy.tmc.cli.synchronization.PollScheduler;
import hy.tmc.cli.testhelpers.TestFuture;
import hy.tmc.core.TmcCore;
import hy.tmc.core.configuration.TmcSettings;
import hy.tmc.core.domain.Course;
import hy.tmc.core.domain.Review;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import org.junit.Test;
import org.junit.Before;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import static org.junit.Assert.assertEquals;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import org.mockito.Mockito;
import static org.mockito.Mockito.when;

public class StatusPollerTest {

    private ReviewAndUpdatePoller statusPoller;
    private TmcCore coreMock;

    @Before
    public void before() throws Exception {
        Mailbox.create();
        coreMock = Mockito.mock(TmcCore.class);
        statusPoller = new ReviewAndUpdatePoller(new TmcCli(coreMock), new Course(), new PollScheduler(0, TimeUnit.SECONDS));
    }

    @Test
    public void afterOneIterationThereShouldBeMessagesPolled() throws Exception {
        List<Review> reviews = new ArrayList<>();
        reviews.add(new Review());
        reviews.add(new Review());
        when(coreMock.getNewReviews(any(Course.class), any(TmcSettings.class)))
                .thenReturn(new TestFuture<>(reviews));

        statusPoller.runOneIteration();
        assertTrue(Mailbox.getMailbox().get().reviewsWaiting());
        assertEquals(2, Mailbox.getMailbox().get().getUnreadReviews().size());
    }

    @Test(expected = IllegalStateException.class)
    public void failsIfNoMailBoxCreated() throws Exception {
        Mailbox.destroy();
        statusPoller.runOneIteration();
    }

}
