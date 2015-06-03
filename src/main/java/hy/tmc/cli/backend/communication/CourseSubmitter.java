package hy.tmc.cli.backend.communication;

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
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import net.lingala.zip4j.exception.ZipException;

public class CourseSubmitter {

    private RootFinder rootFinder;
    private ZipMaker zipper;

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
        Exercise currentExercise = findExercise(currentPath);
        if (currentExercise == null) {
            throw new IllegalArgumentException("Could not find exercise in this directory");
        }
        return sendZipFile(currentPath, currentExercise);
    }
    
    public boolean isExpired(Exercise currentExercise){
        Date date = new Date();
        Date current = new Date();
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss zzzz", Locale.ENGLISH);
        try {
            date = format.parse(currentExercise.getDeadline());
        }
        catch (ParseException ex) {
            return false;
        }
        if(date.getTime() > current.getTime()){
            return true;
        }
        return false;
    }

    private String sendZipFile(String currentPath, Exercise currentExercise) throws IOException {
        String submissionZipPath = currentPath + "/submission.zip";
        String returnUrl = currentExercise.getReturnUrlWithApiVersion();

        zip(findExerciseFolderToZip(currentPath), submissionZipPath);
        String resultUrl = sendSubmissionToServer(submissionZipPath, returnUrl);
        new File(submissionZipPath).delete();
        return resultUrl;
    }

    private String findExerciseFolderToZip(String currentPath) {
        return rootFinder.getRootDirectory(
                Paths.get(currentPath)
        ).toString();
    }

    private String sendSubmissionToServer(String submissionZipPath, String url) throws IOException {
        HttpResult result = UrlCommunicator.makePostWithFile(
                new File(submissionZipPath), url, null
        );
        return TmcJsonParser.getSubmissionUrl(result);
    }

    private Exercise findExercise(String currentPath) {
        return findCurrentExercise(findCourseExercises(currentPath), currentPath);
    }

    private List<Exercise> findCourseExercises(String currentPath) {
        Course currentCourse = getCurrentCourse(currentPath);
        if (currentCourse == null) {
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
}
