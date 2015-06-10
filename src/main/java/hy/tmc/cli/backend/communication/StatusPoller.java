package hy.tmc.cli.backend.communication;

import com.google.common.base.Optional;
import com.google.common.util.concurrent.AbstractScheduledService;
import static com.google.common.util.concurrent.AbstractScheduledService.Scheduler.newFixedRateSchedule;
import hy.tmc.cli.backend.Mailbox;
import hy.tmc.cli.configuration.ClientData;
import hy.tmc.cli.domain.Course;
import hy.tmc.cli.domain.Review;
import hy.tmc.cli.zipping.DefaultRootDetector;
import hy.tmc.cli.zipping.ProjectRootFinder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class StatusPoller extends AbstractScheduledService {

    String path;
    
    public StatusPoller(String path) {
        this.path = path;
    }
    
    
    @Override
    protected void runOneIteration() throws Exception {
        Optional<List<Review>> reviews = checkReviews();
        if (reviews.isPresent()) {
            System.out.println(reviews);
            Mailbox.getMailbox().fill(reviews.get());
        }
        
        System.out.println("polled @"+System.currentTimeMillis());
    }

    @Override
    protected Scheduler scheduler() {
        Scheduler rate = newFixedRateSchedule(0, 15, TimeUnit.SECONDS);
        return rate;
    }

    private Optional<List<Review>> checkReviews() {
        Course course;
        Optional<Course> current = ClientData.getCurrentCourse();
        if (current.isPresent()) {
            course = current.get();
        } else {
            current = findCourse();
            if (current.isPresent()) {
                course = current.get();
            } else {
                return Optional.absent();
            }
        }
        List<Review> currentReviews = TmcJsonParser.getReviews(course.getReviewsUrl());
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

    private Optional<Course> findCourse() {
        ProjectRootFinder prf = new ProjectRootFinder(new DefaultRootDetector());
        return prf.getCurrentCourse(this.path);
    }

}
