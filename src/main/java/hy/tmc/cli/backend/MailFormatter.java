package hy.tmc.cli.backend;

import hy.tmc.cli.domain.Exercise;
import hy.tmc.cli.domain.Review;

import java.util.ArrayList;
import java.util.List;


public class MailFormatter {
    
    public static String formatReviews(List<Review> reviews) {
        return reviewOutput(reviews);
    }
    
    public static String formatExercises(List<?> exercises) {
        return "";
    }
    
    private static String reviewOutput(List<Review> reviews) {
        StringBuilder builder = new StringBuilder();
        builder.append("There are ")
                .append(reviews.size())
                .append(" unread code reviews\n");
        for (Review review : reviews) {
            addReviewToOutput(review, builder);
        }
        return builder.toString();
    }

    private static void addReviewToOutput(Review review, StringBuilder builder) {
        builder.append(review.toString())
                .append("\n");
    }
}
