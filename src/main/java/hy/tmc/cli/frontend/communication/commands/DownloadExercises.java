package hy.tmc.cli.frontend.communication.commands;

import com.google.common.base.Optional;
import hy.tmc.cli.backend.communication.ExerciseDownloader;
import hy.tmc.cli.backend.communication.TmcJsonParser;
import hy.tmc.cli.configuration.ClientData;
import hy.tmc.cli.domain.Course;
import hy.tmc.cli.frontend.communication.server.ProtocolException;
import hy.tmc.cli.synchronization.TmcServiceScheduler;

public class DownloadExercises extends Command<String> {

    /**
     * ExerciseDownloader that is used for downloading.
     */
    private ExerciseDownloader exerciseDownloader;
    private MailChecker mail;
    private Course current;

    public DownloadExercises() {
        this.mail = new MailChecker();
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
        if (!ClientData.userDataExists()) {
            throw new ProtocolException("Please authorize first.");
        }
        Optional<Course> currentCourse = ClientData.getCurrentCourse(data.get("path"));
        if (currentCourse.isPresent()) {
            this.current = currentCourse.get();
        } else {
            throw new ProtocolException("No course resolved from the path.");
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
        String mail = checkMail();
        return Optional.of(mail + "\n" + data);
    }

    /**
     * Parses the course JSON and executes downloading of the course exercises.
     *
     * @return
     */
    @Override
    public String call() throws ProtocolException {
        TmcServiceScheduler.startIfNotRunning(this.current);
        checkData();

        Optional<Course> courseResult = TmcJsonParser.getCourse(Integer.parseInt(this.data.get("courseID")));
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

    /**
     * HUOM EXTRAKTOI TÄMÄ OMAAN LUOKKAAN
     * Executes the mail command with necessary params.
     * Gives the mail command either a courseID (preferably) or a path
     * for determining which courses reviews and updates should be fetched.
     *
     * @throws ProtocolException if unable to find necessary params.
     */
    private String checkMail() {
        if (data.containsKey("courseID")) {
            mail.setParameter("courseID", data.get("courseID"));
        } else if (data.containsKey("path")) {
            mail.setParameter("path", data.get("path"));
        } else {
            return "must specify path";
        }
        try {
            return mail.call();
        } catch (ProtocolException e) {
            return e.getMessage();
        }
    }
}
