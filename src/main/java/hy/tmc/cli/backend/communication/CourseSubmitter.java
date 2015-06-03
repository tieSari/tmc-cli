package hy.tmc.cli.backend.communication;

import com.google.common.base.Optional;

import hy.tmc.cli.domain.Course;
import hy.tmc.cli.domain.Exercise;
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
import java.util.List;
import java.util.Locale;

import java.util.Map;
import net.lingala.zip4j.exception.ZipException;

public class CourseSubmitter {

    private RootFinder rootFinder;
    private ZipMaker zipper;
    private String submissionZipPath;

    public CourseSubmitter(RootFinder rootFinder, ZipMaker zipper) {
        this.zipper = zipper;
        this.rootFinder = rootFinder;
    }

    /**
     * Submits folder of exercise to TMC.
     *
     * @param currentPath path from which this was called.
     * @param exerciseName name of exercise to submit
     * @return String with url from which to get results or null if exercise was not found.
     * @throws IOException if failed to create zip.
     */
    public String submit(String currentPath, String exerciseName) throws IOException {
        throw new UnsupportedOperationException("Doesnt work yet");
    }

    /**
     * Submits folder of exercise to TMC. Finds it from current directory.
     *
     * @param currentPath path from which this was called.
     * @return String with url from which to get results or null if exercise was not found.
     * @throws IOException if failed to create zip.
     */
    public String submit(String currentPath) throws IOException {
        Exercise currentExercise = searchExercise(currentPath);
        return sendZipFile(currentPath, currentExercise, false);
    }

    /**
     * Submits folder of exercise to TMC. Finds it from current directory.
     * Result includes URL of paste.
     *
     * @param currentPath path from which this was called.
     * @return String with url from which to get paste URL or null if exercise was not found.
     * @throws IOException if failed to create zip.
     */
    public String submitPaste(String currentPath) throws IOException {
        Exercise currentExercise = searchExercise(currentPath);
        return sendZipFile(currentPath, currentExercise, true);
    }

    private Exercise searchExercise(String currentPath) throws IllegalArgumentException {
        Exercise currentExercise = findExercise(currentPath);
        if (currentExercise == null) {
            deleteZipIfExists();
            throw new IllegalArgumentException("Could not find exercise in this directory");
        }
        return currentExercise;
    }

    public boolean isExpired(Exercise currentExercise) {
        Date date = new Date();
        Date current = new Date();
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss zzzz", Locale.ENGLISH);
        try {
            date = format.parse(currentExercise.getDeadline());
        }
        catch (ParseException ex) {
            deleteZipIfExists();
            return false;
        }
        return date.getTime() > current.getTime();
    }

    private String sendSubmissionToServerWithPaste(
            String submissionZipPath,
            String url) throws IOException {
        HttpResult result = UrlCommunicator.makePostWithFile(
                new File(submissionZipPath),
                url + "&paste=1",
                Optional.<Map<String, String>>absent()
        );
        return TmcJsonParser.getPasteUrl(result);
    }

    private String sendZipFile(String currentPath, Exercise currentExercise, boolean paste) throws IOException {
        this.submissionZipPath = currentPath + "/submission.zip";
        String returnUrl = currentExercise.getReturnUrlWithApiVersion();
        deleteZipIfExists();
        zip(findExerciseFolderToZip(currentPath), submissionZipPath);
        String resultUrl;
        if (paste) {
            resultUrl = sendSubmissionToServerWithPaste(submissionZipPath, returnUrl);
        } else {
            resultUrl = sendSubmissionToServer(submissionZipPath, returnUrl);
        }
        deleteZipIfExists();
        return resultUrl;
    }

    private String findExerciseFolderToZip(String currentPath) {
        return rootFinder.getRootDirectory(
                Paths.get(currentPath)
        ).toString();
    }

    private String sendSubmissionToServer(String submissionZipPath, String url) throws IOException {
        HttpResult result = UrlCommunicator.makePostWithFile(
                new File(submissionZipPath), url, Optional.<Map<String, String>>absent()
        );
        return TmcJsonParser.getSubmissionUrl(result);
    }

    private Exercise findExercise(String currentPath) {
        return findCurrentExercise(findCourseExercises(currentPath), currentPath);
    }

    private List<Exercise> findCourseExercises(String currentPath) {
        Course currentCourse = getCurrentCourse(currentPath);
        if (currentCourse == null) {
            deleteZipIfExists();
            throw new IllegalArgumentException("Not under any course directory");
        };
        List<Exercise> courseExercises = TmcJsonParser.getExercises(currentCourse.getId());
        return courseExercises;
    }

    private void zip(String exerciseFolderToZip, String currentPath) {
        try {
            this.zipper.zip(exerciseFolderToZip, currentPath);
        }
        catch (ZipException ex) {
            System.err.println(ex.getMessage());
        }
    }

    private Exercise findCurrentExercise(List<Exercise> courseExercises, String currentDir) {
        Path rootDir = rootFinder.getRootDirectory(Paths.get(currentDir));
        if (rootDir == null) {
            deleteZipIfExists();
            throw new IllegalArgumentException("Could not find exercise directory");
        }
        String[] path = rootDir.toString().split("/");
        String directory = path[path.length - 1];
        return getExerciseByName(directory, courseExercises);
    }

    private Exercise getExerciseByName(String name, List<Exercise> courseExercises) {
        for (Exercise exercise : courseExercises) {
            if (exercise.getName().contains(name)) {
                return exercise;
            }
        }
        return null;
    }

    private Course getCurrentCourse(String directoryPath) {
        String[] foldersOfPwd = getExerciseName(directoryPath);
        return findCourseByPath(foldersOfPwd);
    }

    /**
     * Downloads all courses and iterates over them. Returns Course whose name matches with one
     * folder in given path.
     *
     * @param foldersPath contains the names of the folders in path
     * @return Course
     */
    public Course findCourseByPath(String[] foldersPath) {
        List<Course> courses = TmcJsonParser.getCourses();
        Course currentCourse = null;
        for (Course course : courses) {
            for (String folderName : foldersPath) {
                if (course.getName().equals(folderName)) {
                    return course;
                }
            }
        }
        assert currentCourse != null;
        return currentCourse;
    }

    public String[] getExerciseName(String directoryPath) {
        return directoryPath.split("/");
    }

    /**
     * If class submissionZipPath is defined in sendZipFile-method 
     * and the file in defined path exists, it will be removed.
     * This method should be invoked allways when submit-function fails.
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
