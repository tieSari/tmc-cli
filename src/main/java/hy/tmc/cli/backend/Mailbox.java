package hy.tmc.cli.backend;

import hy.tmc.cli.domain.Course;
import hy.tmc.cli.domain.Exercise;
import hy.tmc.cli.domain.Review;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Mailbox {
    
    private static Mailbox mailbox;
    
    private boolean newReviews;
    private boolean newUpdates;
    
    private List<Review> unreadReviews;
    private Map<Course, List<Exercise>> exerciseUpdates;
    
    private Mailbox() {
        unreadReviews = new ArrayList<>();
        exerciseUpdates = new HashMap<>();
    }
    
    public synchronized void fill(List<Review> reviews) {
        if(mailbox == null) throw new IllegalStateException("No mailbox found.");
        newReviews = true;
        unreadReviews.addAll(reviews);
    }
    
    public synchronized void fill(Course course, List<Exercise> exercises) {
        newUpdates = false;
        exercises.addAll(exerciseUpdates.get(course));
        exerciseUpdates.put(course, exercises);
    }
    
    public synchronized List<Review> getUnreadReviews() {
        newReviews = false;
        List<Review> unread = new ArrayList<>(unreadReviews);
        unreadReviews.clear();
        return unread;
    }
    
    public synchronized List<Exercise> getExerciseUpdates(Course course) {
        newUpdates = false;
        List<Exercise> updates = exerciseUpdates.get(course);
        exerciseUpdates.remove(course);
        return updates;
    }
    
    public synchronized boolean reviewsWaiting() {
        return this.newReviews;
    }
    
    public synchronized boolean updatesWaiting() {
        return this.newUpdates;
    }
    
    public static void create() {
        mailbox = new Mailbox();
    }
    
    public static void destroy() {
        mailbox = null;
    }

    public static boolean hasMailboxInitialized() {
        return mailbox != null;
    }

    public static Mailbox getMailbox() {
        return mailbox;
    }   
}
