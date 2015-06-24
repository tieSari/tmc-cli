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
     * Checks that command has required parameters courseID is the id of the
     * course and path is the path of where files are downloaded and extracted.
     *
     * @throws ProtocolException if path isn't supplied
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
     *
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

    @Override
    public Optional<String> parseData(Object data) {
        return Optional.of((String) data);
    }

    /**
     * Parses the course JSON and executes downloading of the course exercises.
     *
     * @return
     */
    @Override
    public String call() throws ProtocolException {
        Optional<Course> courseResult = TmcJsonParser.getCourse(Integer.parseInt((String) this.data.get("courseID")));
        if (courseResult.isPresent()) {
            Course course = courseResult.get();
            Optional<String> downloadFiles = exerciseDownloader.downloadFiles(
                    course.getExercises(), data.get("path"), course.getName());
            if (downloadFiles.isPresent()) {
                return downloadFiles.get();
            }
        }
        throw new ProtocolException("Failed to fetch exercises. Check your internet connection.");
    }
}
