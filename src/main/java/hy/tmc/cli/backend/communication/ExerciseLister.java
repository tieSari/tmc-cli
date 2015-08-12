package hy.tmc.cli.backend.communication;


import static hy.tmc.cli.frontend.ColorFormatter.coloredString;
import hy.tmc.cli.frontend.CommandLineColor;
import static hy.tmc.cli.frontend.CommandLineColor.GREEN;
import static hy.tmc.cli.frontend.CommandLineColor.RED;
import static hy.tmc.cli.frontend.CommandLineColor.WHITE;
import fi.helsinki.cs.tmc.core.domain.Exercise;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

public class ExerciseLister {

    /**
     * Default Constructor with default root finder.
     */
    public ExerciseLister() {
    }

    /**
     * Builds a printout of the exercises given.
     *
     * @param exercises to build info from
     * @return String containing information
     */
    public String buildExercisesInfo(List<Exercise> exercises) {
        StringBuilder builder = new StringBuilder();

        for (Exercise exercise : exercises) {
            builder.append(buildSuccessOrFailMessage(exercise))
                    .append(exercise.getName())
                    .append("\n");

        }
        builder.append(endSummary(exercises));
        return builder.toString();
    }

    private String buildSuccessOrFailMessage(Exercise exercise) {
        if (exercise.isCompleted()) {
            return coloredString("[x] ", CommandLineColor.GREEN);
        } else if (exercise.isAttempted()) {
            return coloredString("[ ] ", CommandLineColor.RED);
        } else {
            return "[ ] ";
        }
    }

    private String endSummary(List<Exercise> exercises) {
        int completed = 0;
        int attempted = 0;
        int total = 0;

        for (Exercise exercise : exercises) {
            if (exercise.isCompleted()) {
                completed++;
            } else if (exercise.isAttempted()) {
                attempted++;
            }
            total++;
        }
        return coloredString("Completed: " + completed + percentage(completed, total), GREEN) + " "
                + coloredString("Attempted: " + attempted + percentage(attempted, total), RED) + " "
                + coloredString("Total: " + total, WHITE);
    }

    private String percentage(int amount, int total) {
        double percentage = 100.0 * amount / total;
        NumberFormat formatter = new DecimalFormat("#0.0");
        
        return " (" + formatter.format(percentage) +"%)";
    }
}
