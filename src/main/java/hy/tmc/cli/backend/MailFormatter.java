package hy.tmc.cli.backend;

import hy.tmc.cli.domain.Exercise;
import hy.tmc.cli.domain.Review;

import java.util.ArrayList;
import java.util.List;


public class MailFormatter {

    /**
     *
     * */
    public static String format(List<?> elements) {
        if (elements.isEmpty()) return "";
        if (elements.get(0) instanceof Review) {
            return formatReviews(elements);
        }
        return formatExercises(elements);
    }
    
    private static String formatReviews(List<?> reviews) {
        return "There are "+ reviews.size() + " unread code reviews";
    }
    
    private static String formatExercises(List<?> exercises) {
        return "";
    }
}
