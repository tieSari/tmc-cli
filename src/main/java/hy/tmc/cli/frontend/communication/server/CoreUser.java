package hy.tmc.cli.frontend.communication.server;

import com.google.common.base.Optional;
import com.google.common.util.concurrent.AsyncFunction;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import fi.helsinki.cs.tmc.core.TmcCore;
import fi.helsinki.cs.tmc.core.configuration.TmcSettings;
import fi.helsinki.cs.tmc.core.domain.Course;
import fi.helsinki.cs.tmc.core.domain.Exercise;
import fi.helsinki.cs.tmc.core.domain.submission.SubmissionResult;
import fi.helsinki.cs.tmc.core.domain.submission.ValidationError;
import fi.helsinki.cs.tmc.core.domain.submission.Validations;
import fi.helsinki.cs.tmc.core.exceptions.TmcCoreException;
import fi.helsinki.cs.tmc.langs.domain.RunResult;
import fi.helsinki.cs.tmc.stylerunner.validation.ValidationResult;
import hy.tmc.cli.CliSettings;
import hy.tmc.cli.TmcCli;
import hy.tmc.cli.frontend.CommandLineProgressObserver;
import hy.tmc.cli.frontend.CourseFinder;
import hy.tmc.cli.frontend.formatters.CheckstyleFormatter;
import hy.tmc.cli.frontend.formatters.DefaultCheckstyleFormatter;
import hy.tmc.cli.frontend.formatters.DefaultSubmissionResultFormatter;
import hy.tmc.cli.frontend.formatters.DefaultTestResultFormatter;
import hy.tmc.cli.frontend.formatters.SubmissionResultFormatter;
import hy.tmc.cli.frontend.formatters.TestResultFormatter;
import hy.tmc.cli.frontend.formatters.VimCheckstyleFormatter;
import hy.tmc.cli.frontend.formatters.VimSubmissionResultFormatter;
import hy.tmc.cli.frontend.formatters.VimTestResultFormatter;
import hy.tmc.cli.listeners.*;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.net.URI;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import org.apache.commons.lang.StringUtils;

public class CoreUser {

    private final TmcCore core;
    private final DataOutputStream output;
    private final Socket socket;
    private final ListeningExecutorService threadPool;
    private final TmcCli tmcCli;
    private final CommandLineProgressObserver observer;
    private final CourseFinder courseFinder;

    public CoreUser(TmcCli tmcCli, DataOutputStream output, Socket socket, ListeningExecutorService pool) {
        this.core = tmcCli.getCore();
        this.threadPool = pool;
        this.tmcCli = tmcCli;
        this.socket = socket;
        this.output = output;
        this.observer = new CommandLineProgressObserver(output);
        this.courseFinder = new CourseFinder();
    }

    public void findAndExecute(String commandName, Map<String, String> params) throws ProtocolException, TmcCoreException, IOException, InterruptedException, ExecutionException, ParseException {
        switch (commandName) {
            case "login":
                login(params);
                break;
            case "listCourses":
                listCourses(params);
                break;
            case "listExercises":
                listExercises(params);
                break;
            case "downloadExercises":
                downloadExercises(params);
                break;
            case "logout":
                logout(params);
                break;
            case "submit":
                submit(params);
                break;
            case "runTests":
                runTests(params);
                break;
            case "paste":
                paste(params);
                break;
            case "update":
                update(params);
                break;
            default:
                throw new ProtocolException("Command not found.");
        }
    }

    public void runTests(Map<String, String> params) throws ProtocolException, TmcCoreException {
        if (!params.containsKey("path") || params.get("path").isEmpty()) {
            throw new ProtocolException("File path to exercise required.");
        }
        boolean verbose = params.containsKey("verbose");
        // run tests need none of the defaults
        CliSettings settings = new CliSettings();
        settings.setMainDirectory(params.get("path"));

        ListenableFuture<RunResult> result = core.test(params.get("path"), settings);
        ListenableFuture<Validations> checkstyle = convertToValidations(core.runCheckstyle(params.get("path"), settings));
        TestResultFormatter formatter;
        CheckstyleFormatter checkFormatter;
        formatter = getTestResultFormatter(params);
        checkFormatter = getCheckFormatter(params);

        TestsListener listener = new TestsListener(result, checkstyle, output, socket, formatter, checkFormatter, verbose);
        result.addListener(listener, threadPool);
        checkstyle.addListener(listener, threadPool);
    }

    private TestResultFormatter getTestResultFormatter(Map<String, String> params) {
        TestResultFormatter formatter;
        if (params.containsKey("--vim")) {
            formatter = new VimTestResultFormatter();
        } else {
            formatter = new DefaultTestResultFormatter();
        }
        return formatter;
    }

    private CheckstyleFormatter getCheckFormatter(Map<String, String> params) {
        CheckstyleFormatter formatter;
        if (params.containsKey("--vim")) {
            formatter = new VimCheckstyleFormatter();
        } else {
            formatter = new DefaultCheckstyleFormatter();
        }
        return formatter;
    }

    public void login(Map<String, String> params) throws ProtocolException, TmcCoreException, IllegalStateException {
        if (credentialsAreMissing(params)) {
            throw new ProtocolException("Username or/and password is missing!.");
        }
        try {
            CliSettings settings = this.tmcCli.defaultSettings();
            settings.setUserData(params.get("username"), params.get("password"));
            ListenableFuture<Boolean> result = core.verifyCredentials(settings);
            LoginListener listener = new LoginListener(result, output, socket, tmcCli, settings);
            result.addListener(listener, threadPool);
        } catch (ParseException | IOException | IllegalStateException ex) {
            this.writeToOutputSocket(ex.getMessage());
        }

    }

    public void listCourses(Map<String, String> params) throws ProtocolException, TmcCoreException, IllegalStateException {
        CliSettings settings;
        try {
            settings = this.tmcCli.defaultSettings();
        } catch (IllegalStateException | IOException | ParseException ex) {
            this.writeToOutputSocket(ex.getMessage());
            return;
        }
        if (loginIsDone(settings)) {
            ListenableFuture<List<Course>> coursesFuture = core.listCourses(settings);
            ResultListener coursesListener = new ListCoursesListener(coursesFuture, output, socket);
            coursesFuture.addListener(coursesListener, threadPool);
        }
    }

    public void listExercises(Map<String, String> params) throws ProtocolException, TmcCoreException, IllegalStateException {
        CliSettings settings;
        try {
            settings = this.tmcCli.defaultSettings();
        } catch (IllegalStateException | ParseException | IOException ex) {
            this.writeToOutputSocket(ex.getMessage());
            return;
        }
        if (loginIsDone(settings)) {
            if (!params.containsKey("path")) {
                throw new ProtocolException("Path not recieved");
            }
            try {
                Optional<Course> currentCourse = this.courseFinder.getCurrentCourse(
                        params.get("path"),
                        core.listCourses(settings).get()
                );
                if (currentCourse.isPresent()) {
                    ListenableFuture<Course> course = core.getCourse(settings,
                            currentCourse.get().getDetailsUrl());
                    ResultListener exercisesListener = new ListExercisesListener(course, output, socket);
                    course.addListener(exercisesListener, threadPool);
                } else {
                    writeToOutputSocket("Could not find current course from your path.");
                }
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
    }

    public void downloadExercises(Map<String, String> params) throws ProtocolException, TmcCoreException,
            IOException, IllegalStateException, NumberFormatException {
        CliSettings settings;
        try {
            settings = this.tmcCli.defaultSettings();
            if (params.get("path") == null || params.get("path").isEmpty()) {
                throw new ProtocolException("Path required");
            }
            settings.setMainDirectory(params.get("path"));
        } catch (IllegalStateException | ParseException ex) {
            this.writeToOutputSocket(ex.getMessage());
            return;
        }

        if (loginIsDone(settings)) {
            ListenableFuture<List<Exercise>> exercisesFuture;
            if (StringUtils.isNotBlank(params.get("courseID"))) {
                int id = Integer.parseInt(params.get("courseID"));
                exercisesFuture = downloadById(id, settings);
            } else if (StringUtils.isNotBlank(params.get("courseName"))) {
                exercisesFuture = downloadByName(params.get("courseName"), settings);
            } else {
                throw new ProtocolException("Either a course ID or a course name must be given");
            }
            ResultListener resultListener = new DownloadExercisesListener(exercisesFuture, output, socket);
            exercisesFuture.addListener(resultListener, threadPool);
        }
    }

    private ListenableFuture<List<Exercise>> downloadById(int id, CliSettings settings) throws ProtocolException, TmcCoreException {
        ListenableFuture<List<Exercise>> exercisesFuture = core.downloadExercises(
                settings.getTmcMainDirectory(), "" + id, settings, observer
        );
        return exercisesFuture;
    }

    private ListenableFuture<List<Exercise>> downloadByName(String name, CliSettings settings) throws TmcCoreException, IOException {
        ListenableFuture courseFuture = core.getCourseByName(settings, name);
        DownloadCourse dl = new DownloadCourse(settings);
        return Futures.transform(courseFuture, dl);
    }

    public void logout(Map<String, String> params) {
        this.tmcCli.logout();
        writeToOutputSocket("User data cleared!");
    }

    public void submit(Map<String, String> params) throws ProtocolException, IllegalStateException {
        CliSettings settings;
        try {
            settings = this.tmcCli.defaultSettings();
        } catch (IllegalStateException | ParseException | IOException ex) {
            this.writeToOutputSocket(ex.getMessage());
            return;
        }
        if (loginIsDone(settings)) {
            if (!params.containsKey("path")) {
                throw new ProtocolException("path not supplied");
            }
            settings.setPath(params.get("path"));
            try {
                sendSubmission(settings, params);
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private void sendSubmission(CliSettings settings, Map<String, String> params) throws TmcCoreException, ExecutionException, InterruptedException {
        fetchCourseToSettings(settings);
        ListenableFuture<SubmissionResult> result = core.submit(params.get("path"), settings);
        SubmissionResultFormatter formatter;
        formatter = getSubmissionFormatter(params);
        result.addListener(new SubmissionListener(result, output, socket, formatter), threadPool);
    }

    private SubmissionResultFormatter getSubmissionFormatter(Map<String, String> params) {
        SubmissionResultFormatter formatter;
        if (params.containsKey("--vim")) {
            formatter = new VimSubmissionResultFormatter();
        } else {
            formatter = new DefaultSubmissionResultFormatter();
        }
        return formatter;
    }

    private void fetchCourseToSettings(CliSettings settings)
            throws TmcCoreException, InterruptedException, ExecutionException {
        List<Course> courses = core.listCourses(settings).get(); // wait for completion
        Optional<Course> course = this.courseFinder.getCurrentCourse(settings.getPath(), courses);
        if (course.isPresent()) {
            settings.setCurrentCourse(course.get());
        } else {
            throw new TmcCoreException("Could not find course from current path");
        }
    }

    public void paste(Map<String, String> params) throws ProtocolException, TmcCoreException, InterruptedException, ExecutionException, IllegalStateException {
        CliSettings settings;
        try {
            settings = this.tmcCli.defaultSettings();
        } catch (IllegalStateException | ParseException | IOException ex) {
            this.writeToOutputSocket(ex.getMessage());
            return;
        }
        if (loginIsDone(settings)) {
            if (!params.containsKey("path")) {
                throw new ProtocolException("path not supplied");
            }
            settings.setPath(params.get("path"));
            fetchCourseToSettings(settings);
            settings.setUserData(settings.getUsername(), settings.getPassword());
            ListenableFuture<URI> result = core.pasteWithComment(params.get("path"), settings, "");
            result.addListener(new PasteListener(result, output, socket), threadPool);
        }
    }

    private void update(Map<String, String> params) throws TmcCoreException, IOException, InterruptedException, ExecutionException, ProtocolException, ParseException, IllegalStateException {
        Optional<CliSettings> optSettings = this.getDefaultSettings();
        if (!optSettings.isPresent()) {
            return;
        }
        CliSettings settings = optSettings.get();
        if (!loginIsDone(settings)) {
            return;
        }
        settings.setPath(params.get("path"));
        fetchCourseToSettings(settings);
        if (!params.containsKey("path")) {
            throw new ProtocolException("path not supplied");
        }
        String currentPath = params.get("path");
        String courseName = settings.getCurrentCourse().or(new Course()).getName();
        if (courseName.isEmpty()) {
            String error = "Could not determine the course. Make sure you are under a directory with the name of the course";
            writeToOutputSocket(error);
            return;
        }
        String path = currentPath.substring(0, currentPath.indexOf(courseName));
        settings.setMainDirectory(path);
        ListenableFuture<List<Exercise>> downloadFuture;
        ListenableFuture<List<Exercise>> updateFuture = core.getNewAndUpdatedExercises(settings.getCurrentCourse().get(), settings);
        downloadFuture = Futures.transform(updateFuture, new DownloadUpdates(settings));
        downloadFuture.addListener(new UpdateDownloadingListener(tmcCli, downloadFuture, output, socket), threadPool);
    }

    private Optional<CliSettings> getDefaultSettings() throws ParseException, IllegalStateException {
        try {
            CliSettings settings = this.tmcCli.defaultSettings();
            return Optional.of(settings);
        } catch (IllegalStateException | IOException ex) {
            this.writeToOutputSocket(ex.getMessage());
            return Optional.absent();
        }
    }

    private void writeToOutputSocket(String message) {
        try {
            output.write((message + "\n").getBytes());
            socket.close();
        }
        catch (IOException ex) {
            System.err.println("Failed to print error message: ");
            System.err.println(ex.getMessage());
        }
    }

    /**
     * If no login is done yet, user will be asked to login.
     */
    public boolean loginIsDone(TmcSettings settings) {
        if (!settings.userDataExists()) {
            writeToOutputSocket("Please authorize first.");
            return false;
        }
        return true;
    }

    private boolean credentialsAreMissing(Map<String, String> params) {
        String username = params.get("username");
        String password = params.get("password");
        return username == null || username.isEmpty() || password == null || password.isEmpty();
    }

    /**
     * Convert ValidationResult to Validations. When deprecation of ValidationResult in tmc-langs
     * and tmc-core is ready, remove this.
     *
     * @param runCheckstyle
     * @return
     */
    private ListenableFuture<Validations> convertToValidations(ListenableFuture<ValidationResult> runCheckstyle) {
        return Futures.transform(runCheckstyle, new Converter());
    }

    /**
     * This class is for converting the deprecated ValidationResult to Validations. This shoul be
     * removed when tmc-core is updated to use Validations.
     */
    private class Converter implements AsyncFunction<ValidationResult, Validations> {

        @Override
        public ListenableFuture<Validations> apply(ValidationResult result) throws Exception {
            Validations v = new Validations();
            v.setStrategy(result.getStrategy().name());
            Map<File, List<fi.helsinki.cs.tmc.stylerunner.validation.ValidationError>> oldErrs
                    = result.getValidationErrors();
            Map<String, List<ValidationError>> newErrs = new HashMap<>();
            for (File file : oldErrs.keySet()) {
                newErrs.put(file.getName(), convertValidationError(oldErrs.get(file)));
            }
            v.setValidationErrors(newErrs);
            return Futures.immediateFuture(v);
        }

        private List<ValidationError> convertValidationError(List<fi.helsinki.cs.tmc.stylerunner.validation.ValidationError> oldErrs) {
            List<ValidationError> ret = new ArrayList<>();
            for (fi.helsinki.cs.tmc.stylerunner.validation.ValidationError err : oldErrs) {
                ValidationError newErr = new ValidationError();
                newErr.setColumn(err.getColumn());
                newErr.setLine(err.getLine());
                newErr.setMessage(err.getMessage());
                newErr.setSourceName(err.getSourceName());
                ret.add(newErr);
            }
            return ret;
        }

    }

    private class DownloadCourse implements AsyncFunction<Course, List<Exercise>> {

        CliSettings settings;

        public DownloadCourse(CliSettings settings) {
            this.settings = settings;
        }

        @Override
        public ListenableFuture<List<Exercise>> apply(Course course) throws Exception {
            String id = "" + course.getId();
            return core.downloadExercises(settings.getTmcMainDirectory(), id, settings, observer);
        }

    }

    private class DownloadUpdates implements AsyncFunction<List<Exercise>, List<Exercise>> {

        CliSettings settings;

        public DownloadUpdates(CliSettings settings) {
            this.settings = settings;
        }

        @Override
        public ListenableFuture<List<Exercise>> apply(List<Exercise> updatedAndNewExercises) throws Exception {
            if (updatedAndNewExercises.isEmpty()) {
                return Futures.immediateFuture(updatedAndNewExercises); // skip the download
            }
            CoreUser.this.observer.progress("update information received, starting download\n");
            return core.downloadExercises(updatedAndNewExercises, settings, observer);
        }
    }
}
