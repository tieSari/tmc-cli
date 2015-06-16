package hy.tmc.cli.backend;

import hy.tmc.cli.domain.Exercise;
import hy.tmc.cli.domain.Review;

import java.util.ArrayList;
import java.util.List;


public class MailFormatter {
    
    public static String formatReviews(List<?> reviews) {
        return "There are "+ reviews.size() + " unread code reviews";
    }
    
    public static String formatExercises(List<?> exercises) {
        return "";
    }
}
