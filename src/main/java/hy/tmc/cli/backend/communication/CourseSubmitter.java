package hy.tmc.cli.backend.communication;

import com.google.common.base.Optional;

import net.lingala.zip4j.exception.ZipException;
import org.apache.http.entity.mime.content.FileBody;

import hy.tmc.cli.domain.Course;
import hy.tmc.cli.domain.Exercise;
import hy.tmc.cli.frontend.communication.server.ExpiredException;
import hy.tmc.cli.zipping.DefaultRootDetector;
import hy.tmc.cli.zipping.ProjectRootFinder;
import hy.tmc.cli.zipping.RootFinder;
import hy.tmc.cli.zipping.ZipMaker;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class CourseSubmitter {

    private RootFinder rootFinder;
    private ZipMaker zipper;

    /**
     * Exercise deadline is checked with this date format.
     */
    private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSX";

    private String submissionZipPath;

    public CourseSubmitter(RootFinder rootFinder, ZipMaker zipper) {
        this.zipper = zipper;
        this.rootFinder = rootFinder;
    }

    /**
     * Check if exercise is expired.
     *
     * @param currentExercise Exercise
     * @throws ParseException to frontend
     */
    private boolean isExpired(Exercise currentExercise) throws ParseException {
        if (hasNoDeadline(currentExercise)) {
            return false;
        }
        Date deadlineDate = getDeadlineDate(currentExercise);
        Date current = new Date();
        return deadlineGone(current, deadlineDate);
    }

    private Date getDeadlineDate(Exercise currentExercise) throws ParseException {
        DateFormat format = new SimpleDateFormat(DATE_FORMAT);
        return format.parse(currentExercise.getDeadline());
    }

    private boolean hasNoDeadline(Exercise currentExercise) {
        return currentExercise.getDeadline() == null || currentExercise.getDeadline().equals("");
    }

    /**
     * Compare two dates and tell if deadline has gone.
     */
    private boolean deadlineGone(Date current, Date deadline) {
        return current.getTime() > deadline.getTime();
    }

    /**
     * Submits folder of exercise to TMC. Finds it from current directory.
     *
     * @param currentPath path from which this was called.
     * @return String with url from which to get results or null if exercise was not found.
     * @throws IOException if failed to create zip
     * @throws java.text.ParseException if deadline is wrongly formatted
     * @throws hy.tmc.cli.frontend.communication.server.ExpiredException if exercise
     is already expired
     */
    public String submit(String currentPath)
            throws ParseException, ExpiredException, IOException, ZipException {

        Exercise currentExercise = initExercise(currentPath);
        return sendZipFile(currentPath, currentExercise, false);
    }

    /**
     * Submits folder of exercise to TMC. Finds it from current directory. Result includes URL of
     * paste.
     *
     * @param currentPath path from which this was called.
     * @return String with url from which to get paste URL or null if exercise was not found.
     * @throws IOException if failed to create zip
     * @throws java.text.ParseException if deadline is wrongly formatted
     * @throws hy.tmc.cli.frontend.communication.server.ExpiredException if exercise
     is already expired
     */
    public String submitPaste(String currentPath)
            throws IOException, ParseException, ExpiredException,
            IllegalArgumentException, ZipException {

        Exercise currentExercise = initExercise(currentPath);
        return sendZipFile(currentPath, currentExercise, true);
    }

    private Exercise initExercise(String currentPath)
            throws ParseException, IOException, ExpiredException {

        Exercise currentExercise = searchExercise(currentPath);
        if (isExpired(currentExercise) || !currentExercise.isReturnable()) {
            deleteZipIfExists();
            throw new ExpiredException("Exercise is expired.");
        }
        return currentExercise;
    }

    private Exercise searchExercise(String currentPath)
            throws IllegalArgumentException, IOException {
        Optional<Exercise> currentExercise = findExercise(currentPath);
        if (!currentExercise.isPresent()) {
            deleteZipIfExists();
            throw new IllegalArgumentException("Could not find exercise in this directory");
        }
        return currentExercise.get();
    }

    private String sendSubmissionToServerWithPaste(
            String submissionZipPath,
            String url) throws IOException {

        final String pasteExtensionForTmcServer = "&paste=1";

        HttpResult result = UrlCommunicator.makePostWithFile(
                new FileBody(new File(submissionZipPath)),
                url + pasteExtensionForTmcServer,
                new HashMap<String, String>()
        );
        return TmcJsonParser.getPasteUrl(result);
    }

    private String sendZipFile(String currentPath, Exercise currentExercise, boolean paste)
            throws IOException, ZipException {

        formSubmissionZipPath(currentPath);
        String returnUrl = currentExercise.getReturnUrlWithApiVersion();
        deleteZipIfExists();

        zip(findExerciseFolderToZip(currentPath), submissionZipPath);
        String resultUrl = sendSubmission(paste, returnUrl);
        deleteZipIfExists();

        return resultUrl;
    }

    private String sendSubmission(boolean paste, String returnUrl) throws IOException {
        String resultUrl;
        if (paste) {
            resultUrl = sendSubmissionToServerWithPaste(submissionZipPath, returnUrl);
        } else {
            resultUrl = sendSubmissionToServer(submissionZipPath, returnUrl);
        }
        return resultUrl;
    }

    private void formSubmissionZipPath(String currentPath) {
        String submissionExtension = File.separator + "submission.zip";
        this.submissionZipPath = currentPath + submissionExtension;
    }

    private String findExerciseFolderToZip(String currentPath) {
        return rootFinder.getRootDirectory(
                Paths.get(currentPath)
        ).get().toString();
    }

    private String sendSubmissionToServer(String submissionZipPath, String url) throws IOException {
        HttpResult result = UrlCommunicator.makePostWithFile(
                new FileBody(new File(submissionZipPath)),
                url,
                new HashMap<String, String>()
        );
        return TmcJsonParser.getSubmissionUrl(result);
    }

    private Optional<Exercise> findExercise(String currentPath) throws IllegalArgumentException, IOException {
        return findCurrentExercise(findCourseExercises(currentPath), currentPath);
    }

    private List<Exercise> findCourseExercises(String currentPath) throws IllegalArgumentException, IOException {
        Optional<Course> currentCourse = new ProjectRootFinder(
                new DefaultRootDetector()).getCurrentCourse(currentPath);
        if (!currentCourse.isPresent()) {
            deleteZipIfExists();
            throw new IllegalArgumentException("Not under any course directory");
        }
        List<Exercise> courseExercises = TmcJsonParser.getExercises(currentCourse.get().getId());
        return courseExercises;
    }

    private void zip(String exerciseFolderToZip, String currentPath) throws ZipException {
        try {
            this.zipper.zip(exerciseFolderToZip, currentPath);
        } catch (ZipException ex) {
            throw new ZipException("Zipping failed because of " + ex.getMessage());
        }
    }

    private Optional<Exercise> findCurrentExercise(List<Exercise> courseExercises, String currentDir) throws IllegalArgumentException {
        Optional<Path> rootDir = rootFinder.getRootDirectory(Paths.get(currentDir));
        if (!rootDir.isPresent()) {
            deleteZipIfExists();
            throw new IllegalArgumentException("Could not find exercise directory");
        }
        String[] path = rootDir.get().toString().split(File.separator);
        String directory = path[path.length - 1];
        return getExerciseByName(directory, courseExercises);
    }

    private Optional<Exercise> getExerciseByName(String name, List<Exercise> courseExercises) {
        for (Exercise exercise : courseExercises) {
            if (exercise.getName().contains(name)) {
                return Optional.of(exercise);
            }
        }
        return Optional.absent();
    }

    public String[] getExerciseName(String directoryPath) {
        return directoryPath.split(File.separator);
    }

    /**
     * If class submissionZipPath is defined in sendZipFile-method
     * and the file in defined path exists, it will be removed.
     * This method should be invoked always when submit-function fails.
     */
    private void deleteZipIfExists() {
        if (submissionZipPath != null) {
            File zip = new File(submissionZipPath);
            if (zip.exists()) {
                zip.delete();
            }
        }
    }
}
