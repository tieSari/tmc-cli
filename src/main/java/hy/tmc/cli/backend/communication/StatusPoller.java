package hy.tmc.cli.backend.communication;

import com.google.common.base.Optional;
import com.google.common.util.concurrent.AbstractScheduledService;
import static com.google.common.util.concurrent.AbstractScheduledService.Scheduler.newFixedRateSchedule;
import hy.tmc.cli.configuration.ClientData;
import hy.tmc.cli.domain.Course;
import hy.tmc.cli.domain.Review;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class StatusPoller extends AbstractScheduledService {

    @Override
    protected void runOneIteration() throws Exception {
        checkReviews();
    }

    @Override
    protected Scheduler scheduler() {        
        Scheduler rate = newFixedRateSchedule(0, 5, TimeUnit.SECONDS);
        return rate;
    }

    private void checkReviews() {
        Course course;
        Optional<Course> current = ClientData.getCurrentCourse();
        if (current.isPresent()) {
            course = current.get();
        } else {
            return;
        }
        List<Review> currentReviews = TmcJsonParser.getReviews(course.getReviewsUrl());
        currentReviews = filter(currentReviews);
        
        currentReviews.get(0).markAs(true);
        System.out.println(currentReviews);
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
