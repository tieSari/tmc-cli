package hy.tmc.cli.frontend.communication.commands;

import com.google.common.base.Optional;
import hy.tmc.cli.backend.communication.ExerciseDownloader;
import hy.tmc.cli.backend.communication.TmcJsonParser;
import hy.tmc.cli.domain.Course;
import hy.tmc.cli.frontend.communication.server.ProtocolException;

public class DownloadExercises extends Command<String> {

    /**
     * ExerciseDownloader that is used for downloading.
     */
    private ExerciseDownloader exerciseDownloader;

    public DownloadExercises() {
        this.exerciseDownloader = new ExerciseDownloader();
    }

    /**
     * Parses the course JSON and executes downloading of the course exercises.
     */
    protected Optional<String> functionality() {
        Optional<Course> courseResult = TmcJsonParser.getCourse(Integer.parseInt((String)this.data.get("courseID")));
        if (courseResult.isPresent()) {
            Course course = courseResult.get();
            return exerciseDownloader.downloadFiles(course.getExercises(),(String) this.data.get("pwd"), course.getName());
        }
        return Optional.absent();
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
     *
     * @throws ProtocolException if course id is not a number
     */
    private void checkCourseId() throws ProtocolException {
        if (!this.data.containsKey("courseID")) {
            throw new ProtocolException("Course ID required");
        }
        try {
   //         int courseId = Integer.parseInt(this.data.get("courseID"));
        }
        catch (NumberFormatException e) {
            throw new ProtocolException("Given course id is not a number");
        }
    }

    @Override
    public Optional<String> parseData(Object data) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String call() throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
