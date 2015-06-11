package hy.tmc.cli.frontend.communication.commands;

import com.google.common.base.Optional;
import hy.tmc.cli.backend.communication.ExerciseDownloader;
import hy.tmc.cli.backend.communication.TmcJsonParser;
import hy.tmc.cli.domain.Course;
import hy.tmc.cli.frontend.FrontendListener;
import hy.tmc.cli.frontend.communication.server.ProtocolException;

public class DownloadExercises extends MailCheckingCommand {

    /**
     * ExerciseDownloader that is used for downloading.
     */
    private ExerciseDownloader exDl;

    public DownloadExercises(FrontendListener front) {
        super(front);
        this.exDl = new ExerciseDownloader(front);
    }

    /**
     * Parses the course JSON and executes downloading of the course exercises.
     */
    @Override
    protected void functionality() {
        Optional<Course> courseResult = TmcJsonParser.getCourse(Integer.parseInt(this.data.get("courseID")));
        if (courseResult.isPresent()) {
            Course course = courseResult.get();
            exDl.downloadFiles(course.getExercises(), this.data.get("path"), course.getName());
        }
    }

    /**
     * Checks that command has required parameters courseID is the id of the course and pwd is the
     * path of where files are downloaded and extracted.
     *
     * @throws ProtocolException if pwd isnt supplied
     */
    @Override
    public void checkData() throws ProtocolException {
        checkCourseId();
        if (!this.data.containsKey("path")) {
            throw new ProtocolException("Path required");
        }
    }

    /**
     * Check that user has given also course id.
     * @throws ProtocolException if course id is not a number
     */
    private void checkCourseId() throws ProtocolException {
        if (!this.data.containsKey("courseID")) {
            throw new ProtocolException("Course ID required");
        }
        try {
            int courseId = Integer.parseInt(this.data.get("courseID"));
        } catch (NumberFormatException e) {
            throw new ProtocolException("Given course id is not a number");
        }
    }
}
