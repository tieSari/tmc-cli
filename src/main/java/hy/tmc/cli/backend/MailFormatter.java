package hy.tmc.cli.backend;

import hy.tmc.cli.domain.Exercise;
import hy.tmc.cli.domain.Review;
import java.util.List;


public class MailFormatter {

    public static String format(List<?> elements) {
        if (elements.isEmpty()) {
            return "";
        }
        if (elements.get(0) instanceof Review) {
            return formatReviews((List<Review>) elements);
        } 
        return formatExercises((List<Exercise>) elements);
    }
    
    private static String formatReviews(List<Review> reviews) {
        return "There are "+ reviews.size() + " unread code reviews";
    }
    
    private static String formatExercises(List<Exercise> exercises) {
        return "";
    }
}
