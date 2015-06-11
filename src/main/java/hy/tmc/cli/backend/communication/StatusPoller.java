package hy.tmc.cli.backend.communication;

import com.google.common.base.Optional;
import com.google.common.util.concurrent.AbstractScheduledService;
import hy.tmc.cli.backend.Mailbox;
import hy.tmc.cli.domain.Course;
import hy.tmc.cli.domain.Review;
import hy.tmc.cli.synchronization.PollScheduler;
import java.util.ArrayList;
import java.util.List;

public class StatusPoller extends AbstractScheduledService {

    private Course currentCourse;
    private PollScheduler pollScheduler;

    public StatusPoller(Course currentCourse, PollScheduler schedule) {
        this.currentCourse = currentCourse;
        this.pollScheduler = schedule;
    }

    @Override
    protected void runOneIteration() throws Exception {
        Optional<List<Review>> reviews = checkReviews();
        if (reviews.isPresent()) {
            System.out.println(reviews);
            Mailbox.getMailbox().fill(reviews.get());
        }

    }

    @Override
    protected Scheduler scheduler() {
        return this.pollScheduler;
    }

    private Optional<List<Review>> checkReviews() {
        List<Review> currentReviews = TmcJsonParser.getReviews(this.currentCourse.getReviewsUrl());
        currentReviews = filter(currentReviews);

        if (currentReviews.isEmpty()) {
            return Optional.absent();
        }
        return Optional.of(currentReviews);
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

}
