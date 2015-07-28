package hy.tmc.cli.backend.communication;

import com.google.common.base.Optional;
import com.google.common.util.concurrent.AbstractScheduledService;
import com.google.common.util.concurrent.ListenableFuture;
import hy.tmc.cli.TmcCli;
import hy.tmc.cli.mail.Mailbox;

import hy.tmc.cli.synchronization.PollScheduler;
import hy.tmc.core.TmcCore;
import hy.tmc.core.configuration.TmcSettings;
import hy.tmc.core.domain.Course;
import hy.tmc.core.domain.Review;
import hy.tmc.core.exceptions.TmcCoreException;
import java.io.IOException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;


public class ReviewAndUpdatePoller extends AbstractScheduledService {

    private Course currentCourse;
    private PollScheduler pollScheduler;
    private TmcCli cli;

    /**
     * StatusPoller which polls code reviews. Has fixed time interval
     * which can be directly modified even if the polling is running.
     * @param currentCourse course which code reviews are being checked
     * @param schedule object that contains the time interval
     */
    public ReviewAndUpdatePoller(TmcCli cli, Course currentCourse, PollScheduler schedule) {
        this.cli = cli;
        this.currentCourse = currentCourse;
        this.pollScheduler = schedule;
        
    }

    @Override
    protected void runOneIteration() throws IOException, TmcCoreException {
        if (!Mailbox.hasMailboxInitialized()) {
            throw new IllegalStateException("No mailbox initialized.");
        }

        Optional<List<Review>> reviews = getReviews();
        if (reviews.isPresent()) {
            Mailbox.emptyMailbox(); // TODO poll only new mails without clearing all
            Mailbox.getMailbox().get().fill(reviews.get());
        }
    }

    @Override
    protected Scheduler scheduler() {
        return this.pollScheduler;
    }


    private List<Review> filter(List<Review> currentReviews) {
        List<Review> filtered = new ArrayList<>();
        for (Review review : currentReviews) {
            if (!review.isMarkedAsRead()) {
                filtered.add(review);
            }
        }
        return filtered;
    }

    @Override
    protected void startUp() throws Exception {
    }

    @Override
    protected void shutDown() throws Exception {
    }

    private Optional<List<Review>> getReviews() throws TmcCoreException {
        TmcCore core = cli.getCore();
        TmcSettings settings = cli.defaultSettings();
        ListenableFuture<List<Review>> future = core.getNewReviews(currentCourse, settings);
        try {
            List<Review> reviews = future.get();
            if (reviews == null || reviews.isEmpty()) {
                return Optional.absent();
            }
            return Optional.of(reviews);
        } catch (InterruptedException | ExecutionException ex) {
            System.err.println(ex.getMessage());
            return Optional.absent();
        }
    }
}
