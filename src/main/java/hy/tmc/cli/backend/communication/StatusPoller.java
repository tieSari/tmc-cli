package hy.tmc.cli.backend.communication;

import com.google.common.base.Optional;
import com.google.common.util.concurrent.AbstractScheduledService;
import static com.google.common.util.concurrent.AbstractScheduledService.Scheduler.newFixedRateSchedule;
import hy.tmc.cli.backend.Mailbox;
import hy.tmc.cli.configuration.ClientData;
import hy.tmc.cli.domain.Course;
import hy.tmc.cli.domain.Review;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class StatusPoller extends AbstractScheduledService {

    @Override
    protected void runOneIteration() throws Exception {
        Optional<List<Review>> reviews = checkReviews();
        if (reviews.isPresent()) {
            Mailbox.getMailbox().fill(reviews.get());
        }
    }

    @Override
    protected Scheduler scheduler() {        
        Scheduler rate = newFixedRateSchedule(0, 5, TimeUnit.SECONDS);
        return rate;
    }

    private Optional<List<Review>> checkReviews() {
        Course course;
        Optional<Course> current = ClientData.getCurrentCourse();
        if (current.isPresent()) {
            course = current.get();
        } else {
            return Optional.absent();
        }
        List<Review> currentReviews = TmcJsonParser.getReviews(course.getReviewsUrl());
        currentReviews = filter(currentReviews);
        
        System.out.println(currentReviews.get(0));
        
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
