package hy.tmc.cli.backend.communication;

import com.google.common.base.Optional;
import hy.tmc.cli.domain.Course;
import hy.tmc.cli.domain.Exercise;
import hy.tmc.cli.frontend.ColorFormatter;
import hy.tmc.cli.frontend.CommandLineColor;
import hy.tmc.cli.frontend.communication.server.ProtocolException;
import hy.tmc.cli.zipping.DefaultRootDetector;
import hy.tmc.cli.zipping.ProjectRootFinder;
import hy.tmc.cli.zipping.RootFinder;
import java.util.List;

public class ExerciseLister {

    private RootFinder finder;

    /**
     * Default Constructor with default root finder.
     */
    public ExerciseLister() {
        finder = new ProjectRootFinder(new DefaultRootDetector());
    }

    /**
     * Constructor with specific finder.
     *
     * @param finder a RootFinder instance.
     */
    public ExerciseLister(RootFinder finder) {
        this.finder = finder;
    }

    /**
     * Returns a list of exercises of a current directory in which a course exists. 
     *
     * @param path directory path to lookup course from
     * @return String with a list of exercises.
     */
    public List<Exercise> listExercises(String path) throws ProtocolException {
        Optional<Course> course = finder.getCurrentCourse(path);
        
        if (!course.isPresent()) {
            throw new ProtocolException("No course found");
        }
        
        List<Exercise> exercises = TmcJsonParser.getExercises(course.get());
        if (exercises == null || exercises.isEmpty()) {
            throw new ProtocolException("No exercises found");
        }

        return exercises;
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
            builder.append(exercise.getName())
                    .append(buildSuccessOrFailMessage(exercise))
                    .append("\n");
        }
        return builder.toString();
    }

    private String buildSuccessOrFailMessage(Exercise exercise) {
        if (exercise.isCompleted()) {
            return ColorFormatter.coloredString(" [x]", CommandLineColor.GREEN);
        } else if (exercise.isAttempted()) {
            return ColorFormatter.coloredString(" [ ]", CommandLineColor.RED);
        } else {
            return ColorFormatter.coloredString(" [ ]", CommandLineColor.WHITE);
        }
    }
}
