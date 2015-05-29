
package hy.tmc.cli.frontend.communication.commands;


import hy.tmc.cli.backendcommunication.ExerciseDownloader;
import hy.tmc.cli.backendcommunication.TmcJsonParser;
import hy.tmc.cli.domain.Course;
import hy.tmc.cli.domain.Exercise;
import hy.tmc.cli.frontend.FrontendListener;
import hy.tmc.cli.frontend.communication.server.ProtocolException;
import hy.tmc.cli.logic.Logic;

import java.util.List;

public class DownloadExercises extends Command {

    /**
     * ExerciseDownloader that is used for downloading.
     */
    private ExerciseDownloader exDl;

    public DownloadExercises(FrontendListener front, Logic backend) {
        super(front, backend);
        this.exDl = new ExerciseDownloader(front);
    }

    /**
     * Parses the course JSON and executes downloading of the course exercises.
     */
    @Override
    protected void functionality() {
        Course course = TmcJsonParser.getCourse(Integer.parseInt(this.data.get("courseID")));
        exDl.downloadFiles(course.getExercises(), this.data.get("pwd"), course.getName());
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
        if (!this.data.containsKey("pwd")) {
            throw new ProtocolException("Pwd required");
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
