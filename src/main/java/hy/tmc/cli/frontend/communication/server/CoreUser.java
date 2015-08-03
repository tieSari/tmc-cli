package hy.tmc.cli.frontend.communication.server;

import com.google.common.base.Optional;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import fi.helsinki.cs.tmc.langs.domain.RunResult;
import hy.tmc.cli.CliSettings;
import hy.tmc.cli.TmcCli;
import hy.tmc.cli.frontend.ColorFormatter;
import hy.tmc.cli.frontend.CommandLineColor;
import hy.tmc.cli.frontend.CommandLineProgressObserver;
import hy.tmc.cli.frontend.CourseFinder;
import hy.tmc.cli.frontend.formatters.CommandLineSubmissionResultFormatter;
import hy.tmc.cli.frontend.formatters.DefaultTestResultFormatter;
import hy.tmc.cli.frontend.formatters.SubmissionResultFormatter;
import hy.tmc.cli.frontend.formatters.TestResultFormatter;
import hy.tmc.cli.frontend.formatters.VimSubmissionResultFormatter;
import hy.tmc.cli.frontend.formatters.VimTestResultFormatter;
import hy.tmc.cli.listeners.*;
import hy.tmc.core.TmcCore;
import hy.tmc.core.communication.UrlHelper;
import hy.tmc.core.configuration.TmcSettings;
import hy.tmc.core.domain.Course;
import hy.tmc.core.domain.Exercise;
import hy.tmc.core.domain.submission.SubmissionResult;
import hy.tmc.core.exceptions.TmcCoreException;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.net.URI;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CoreUser {

    private TmcCore core;
    private DataOutputStream output;
    private Socket socket;
    private ListeningExecutorService threadPool;
    private TmcCli tmcCli;
    private CommandLineProgressObserver observer;

    public CoreUser(TmcCli tmcCli, DataOutputStream output, Socket socket, ListeningExecutorService pool) {
        this.core = tmcCli.getCore();
        this.threadPool = pool;
        this.tmcCli = tmcCli;
        this.socket = socket;
        this.output = output;
        this.observer = new CommandLineProgressObserver(output);
    }

    public void findAndExecute(String commandName, HashMap<String, String> params) throws ProtocolException, TmcCoreException, IOException, InterruptedException, ExecutionException {
        this.observer.progress("Starting command " + commandName + "\n");
        
        if (commandName.equals("login")) {
            System.err.println("Loginissa");
            login(params);
        } else if (commandName.equals("listCourses")) {
            listCourses(params);
        } else if (commandName.equals("listExercises")) {
            listExercises(params);
        } else if (commandName.equals("downloadExercises")) {
            downloadExercises(params);
        } else if (commandName.equals("logout")) {
            logout(params);
        } else if (commandName.equals("submit")) {
            submit(params);
        } else if (commandName.equals("runTests")) {
            runTests(params);
        } else if (commandName.equals("paste")) {
            paste(params);
        } else if (commandName.equals("getMail")) {
            getMail(params);
        } else {
            throw new ProtocolException("Command not found.");
        }
    }

    public void runTests(HashMap<String, String> params) throws ProtocolException, TmcCoreException {
        if (!params.containsKey("path") || params.get("path").isEmpty()) {
            throw new ProtocolException("File path to exercise required.");
        }
        // run tests need none of the defaults
        CliSettings settings = new CliSettings();
        settings.setMainDirectory(params.get("path"));
        ListenableFuture<RunResult> result = core.test(params.get("path"), settings);
        TestResultFormatter formatter;
        formatter = getTestResultFormatter(params);
        TestsListener listener = new TestsListener(result, output, socket, formatter);
        result.addListener(listener, threadPool);
    }

    private TestResultFormatter getTestResultFormatter(HashMap<String, String> params) {
        TestResultFormatter formatter;
        if(params.containsKey("--vim")){
            formatter = new VimTestResultFormatter();
        } else {
            formatter = new DefaultTestResultFormatter();
        }
        return formatter;
    }

    public void login(HashMap<String, String> params) throws ProtocolException, TmcCoreException {
        if (credentialsAreMissing(params)) {
            throw new ProtocolException("Username or/and password is missing!.");
        }
        try {
            CliSettings settings = this.tmcCli.defaultSettings();
            settings.setUserData(params.get("username"), params.get("password"));
            ListenableFuture<Boolean> result = core.verifyCredentials(settings);
            System.err.println("Result saatu. ");
            LoginListener listener = new LoginListener(result, output, socket, tmcCli, settings);
            result.addListener(listener, threadPool);
            
        } catch (IllegalStateException ex) {
            this.writeToOutputSocket(ex.getMessage());
        }
        catch (ParseException ex) {
            this.writeToOutputSocket(ex.getMessage());
        }

    }

    public void listCourses(HashMap<String, String> params) throws ProtocolException, TmcCoreException {
        CliSettings settings;
        try {
            settings = this.tmcCli.defaultSettings();
        } catch (IllegalStateException | ParseException ex) {
            this.writeToOutputSocket(ex.getMessage());
            return;
        }
        if (loginIsDone(settings)) {
            ListenableFuture<List<Course>> coursesFuture = core.listCourses(settings);
            ResultListener coursesListener = new ListCoursesListener(coursesFuture, output, socket);
            coursesFuture.addListener(coursesListener, threadPool);
        }
    }

    public void listExercises(HashMap<String, String> params) throws ProtocolException, TmcCoreException {
        CliSettings settings;
        try {
            settings = this.tmcCli.defaultSettings();
        } catch (IllegalStateException | ParseException ex) {
            this.writeToOutputSocket(ex.getMessage());
            return;
        }
        if (loginIsDone(settings)) {
            if (!params.containsKey("path")) {
                throw new ProtocolException("Path not recieved");
            }
            try {
                Optional<Course> currentCourse = new CourseFinder().getCurrentCourse(
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
            } catch (IOException | InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
    }

    public void downloadExercises(HashMap<String, String> params) throws ProtocolException, TmcCoreException, IOException {
        CliSettings settings;
        try {
            settings = this.tmcCli.defaultSettings();
        } catch (IllegalStateException | ParseException ex) {
            this.writeToOutputSocket(ex.getMessage());
            return;
        }
        if (loginIsDone(settings)) {
            if (params.get("path") == null || params.get("path").isEmpty()
                    || params.get("courseID") == null || params.get("courseID").isEmpty()) {
                throw new ProtocolException("Path and courseID required");
            }
            String coursePath = new UrlHelper(settings).getCourseUrl(
                    Integer.parseInt(params.get("courseID"))
            );

            ListenableFuture<List<Exercise>> exercisesFuture = core.downloadExercises(
                    params.get("path"), params.get("courseID"), settings, observer
            );
            ResultListener resultListener = new DownloadExercisesListener(exercisesFuture, output, socket);
            exercisesFuture.addListener(resultListener, threadPool);
        }
    }

    public void logout(HashMap<String, String> params) {
        this.tmcCli.logout();
        writeToOutputSocket("User data cleared!");
    }

    public void submit(HashMap<String, String> params) throws ProtocolException {
        CliSettings settings;
        try {
            settings = this.tmcCli.defaultSettings();
        } catch (IllegalStateException | ParseException ex) {
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
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private void sendSubmission(CliSettings settings, HashMap<String, String> params) throws TmcCoreException, ExecutionException, InterruptedException {
        fetchCourseToSettings(settings);
        ListenableFuture<SubmissionResult> result = core.submit(params.get("path"), settings);
        SubmissionResultFormatter formatter;
        formatter = getSubmissionFormatter(params);
        result.addListener(new SubmissionListener(result, output, socket, formatter), threadPool);
    }

    private SubmissionResultFormatter getSubmissionFormatter(HashMap<String, String> params) {
        SubmissionResultFormatter formatter;
        if(params.containsKey("--vim")){
            formatter = new VimSubmissionResultFormatter();
        } else {
            formatter = new CommandLineSubmissionResultFormatter();
        }
        return formatter;
    }

    private void fetchCourseToSettings(CliSettings settings)
            throws TmcCoreException, InterruptedException, ExecutionException {
        List<Course> courses = core.listCourses(settings).get(); // wait for completion
        String[] folders = settings.getPath().split("\\" + File.separatorChar);
        for (Course course : courses) {
            for (String folder : folders) {
                if (course.getName().equals(folder)) {
                    settings.setCurrentCourse(course);
                    return;
                }
            }
        }
    }

    public void paste(HashMap<String, String> params) throws ProtocolException, TmcCoreException, InterruptedException, ExecutionException {
        CliSettings settings;
        try {
            settings = this.tmcCli.defaultSettings();
        } catch (IllegalStateException | ParseException ex) {
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

    public void getMail(HashMap<String, String> params) throws ProtocolException {

    }

    private void writeToOutputSocket(String message) {
        try {
            output.write((message + "\n").getBytes());
            socket.close();
        } catch (IOException ex) {
            System.err.println("Failed to print error message: ");
            System.err.println(ex.getMessage());
        }
    }

    /**
     * If no login is done yet, user will be asked to login.
     */
    private boolean loginIsDone(TmcSettings settings) {
        if (!settings.userDataExists()) {
            writeToOutputSocket("Please authorize first.");
            return false;
        }
        return true;
    }

    private boolean credentialsAreMissing(HashMap<String, String> params) {
        String username = params.get("username");
        String password = params.get("password");
        return username == null || username.isEmpty() || password == null || password.isEmpty();
    }
}
