package hy.tmc.cli.backend.communication;

import com.google.common.base.Optional;

import net.lingala.zip4j.exception.ZipException;
import org.apache.http.entity.mime.content.FileBody;

import hy.tmc.cli.domain.Course;
import hy.tmc.cli.domain.Exercise;
import hy.tmc.cli.frontend.communication.server.ExpiredException;
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

import net.lingala.zip4j.exception.ZipException;
import org.apache.http.entity.mime.content.FileBody;

public class ExerciseSubmitter {

    private final String pasteExtensionForTmcServer = "&paste=1";
    private RootFinder rootFinder;
    private ZipMaker zipper;

    /**
     * Exercise deadline is checked with this date format.
     */
    private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSX";

    private String submissionZipPath;
    
    public ExerciseSubmitter(RootFinder rootFinder, ZipMaker zipper) {
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
<<<<<<< HEAD:src/main/java/hy/tmc/cli/backend/communication/ExerciseSubmitter.java
     * @return String with url from which to get results or null if exercise was
     * not found.
     * @throws IOException if failed to create zip.
     * @throws java.text.ParseException
     * @throws hy.tmc.cli.frontend.communication.server.ExpiredException
=======
     * @return String with url from which to get results or null if exercise was not found.
     * @throws IOException if failed to create zip
     * @throws java.text.ParseException if deadline is wrongly formatted
     * @throws hy.tmc.cli.frontend.communication.server.ExpiredException if exercise
     is already expired
>>>>>>> e69dab4e159f2006c0badafe201b5eb0f25652e0:src/main/java/hy/tmc/cli/backend/communication/CourseSubmitter.java
     */
    public String submit(String currentPath)
            throws ParseException, ExpiredException, IOException, ZipException {

        Exercise currentExercise = initExercise(currentPath);
        return sendZipFile(currentPath, currentExercise, false);
    }

    /**
<<<<<<< HEAD:src/main/java/hy/tmc/cli/backend/communication/ExerciseSubmitter.java
     * Submits folder of exercise to TMC. Finds it from current directory.
     * Result includes URL of paste.
     *
     * @param currentPath path from which this was called.
     * @return String with url from which to get paste URL or null if exercise
     * was not found.
     * @throws IOException if failed to create zip.
     * @throws java.text.ParseException
     * @throws hy.tmc.cli.frontend.communication.server.ExpiredException
=======
     * Submits folder of exercise to TMC. Finds it from current directory. Result includes URL of
     * paste.
     *
     * @param currentPath path from which this was called.
     * @return String with url from which to get paste URL or null if exercise was not found.
     * @throws IOException if failed to create zip
     * @throws java.text.ParseException if deadline is wrongly formatted
     * @throws hy.tmc.cli.frontend.communication.server.ExpiredException if exercise
     is already expired
>>>>>>> e69dab4e159f2006c0badafe201b5eb0f25652e0:src/main/java/hy/tmc/cli/backend/communication/CourseSubmitter.java
     */
    public String submitPaste(String currentPath)
            throws IOException, ParseException, ExpiredException,
            IllegalArgumentException, ZipException {

        Exercise currentExercise = initExercise(currentPath);
        return sendZipFile(currentPath, currentExercise, true);
    }

    /**
     * Search exercise and throw exception if exercise is expired or not
     * returnable.
     *
     * @throws ParseException to frontend
     * @throws ExpiredException to frontend
     */
    private Exercise initExercise(String currentPath) throws ParseException, ExpiredException, IllegalArgumentException, IOException {
        Exercise currentExercise = searchExercise(currentPath);

        if (exerciseCanBeReturned(currentExercise)) {
            return currentExercise;
        }
        deleteZipIfExists();
        throw new ExpiredException();

    }

    private boolean exerciseCanBeReturned(Exercise exercise) throws ParseException {
        return !isExpired(exercise) && exercise.isReturnable();
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

    private String sendSubmissionToServerWithPaste(String submissionZipPath, String url)
            throws IOException {
        
        HttpResult result = UrlCommunicator.makePostWithFile(
                new File(submissionZipPath), url + pasteExtensionForTmcServer);
        return TmcJsonParser.getPasteUrl(result);
    }

    private String sendZipFile(String currentPath, Exercise currentExercise, boolean paste)
            throws IOException, ZipException {

        formSubmissionZipPath(currentPath);
        String returnUrl = currentExercise.getReturnUrlWithApiVersion();
        deleteZipIfExists();
        zipper.zip(findExerciseFolderToZip(currentPath), submissionZipPath);
        String resultUrl = sendSubmission(returnUrl, paste);
        deleteZipIfExists();
        return resultUrl;
    }
    
    private String sendSubmission(String returnUrl, boolean paste) throws IOException {
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
        return rootFinder.getRootDirectory(Paths.get(currentPath)).get().toString();
    }

    private String sendSubmissionToServer(String submissionZipPath, String url) 
            throws IOException {
        HttpResult result = UrlCommunicator.makePostWithFile(new File(submissionZipPath), url);
        return TmcJsonParser.getSubmissionUrl(result);
    }

    private Optional<Exercise> findExercise(String currentPath) 
            throws IllegalArgumentException, IOException {
        return findCurrentExercise(findCourseExercises(currentPath), currentPath);
    }

    private List<Exercise> findCourseExercises(String currentPath) 
            throws IllegalArgumentException, IOException {
        Optional<Course> currentCourse = new ProjectRootFinder().getCurrentCourse(currentPath);
        if (!currentCourse.isPresent()) {
            deleteZipIfExists();
            throw new IllegalArgumentException("Not under any course directory");
        }
        List<Exercise> courseExercises = TmcJsonParser.getExercises(currentCourse.get().getId());
        return courseExercises;
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
<<<<<<< HEAD:src/main/java/hy/tmc/cli/backend/communication/ExerciseSubmitter.java
     * If class submissionZipPath is defined in sendZipFile-method and the file
     * in defined path exists, it will be removed. This method should be invoked
     * always when submit-function fails.
=======
     * If class submissionZipPath is defined in sendZipFile-method
     * and the file in defined path exists, it will be removed.
     * This method should be invoked always when submit-function fails.
>>>>>>> e69dab4e159f2006c0badafe201b5eb0f25652e0:src/main/java/hy/tmc/cli/backend/communication/CourseSubmitter.java
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
