package hy.tmc.cli.frontend.communication.server;

import com.google.common.base.Optional;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import fi.helsinki.cs.tmc.langs.domain.RunResult;
import hy.tmc.cli.CliSettings;
import hy.tmc.cli.TmcCli;
import hy.tmc.cli.frontend.CourseFinder;
import hy.tmc.cli.listeners.*;
import hy.tmc.core.TmcCore;
import hy.tmc.core.configuration.TmcSettings;
import hy.tmc.core.domain.Course;
import hy.tmc.core.domain.Exercise;
import hy.tmc.core.domain.submission.SubmissionResult;
import hy.tmc.core.exceptions.TmcCoreException;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class CoreUser {

    private TmcCore core;
    private DataOutputStream output;
    private Socket socket;
    private ListeningExecutorService threadPool;
    private TmcCli tmcCli;

    public CoreUser(TmcCli tmcCli, DataOutputStream output, Socket socket, ListeningExecutorService pool) {
        this.core = tmcCli.getCore();
        this.threadPool = pool;
        this.tmcCli = tmcCli;
        this.socket = socket;
        this.output = output;
    }

    public void findAndExecute(String commandName, HashMap<String, String> params) throws ProtocolException, TmcCoreException, IOException, InterruptedException, ExecutionException {
        System.out.println(commandName);
        if (commandName.equals("login")) {
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

    public ListenableFuture<RunResult> runTests(HashMap<String, String> params) throws ProtocolException, TmcCoreException {
        if (!params.containsKey("path") || params.get("path").isEmpty()) {
            throw new ProtocolException("File path to exercise required.");
        }
        CliSettings settings = this.tmcCli.defaultSettings();
        settings.setMainDirectory(params.get("path"));
        ListenableFuture<RunResult> result = core.test(params.get("path"), settings);
        return result;
    }

    public void login(HashMap<String, String> params) throws ProtocolException, TmcCoreException {
        if(credentialsAreMissing(params)) {
            throw new ProtocolException("Username or/and password is missing!.");
        }
        CliSettings settings = this.tmcCli.defaultSettings();
        settings.setUserData(params.get("username"), params.get("password"));
        ListenableFuture<Boolean> result = core.verifyCredentials(settings);
        LoginListener listener = new LoginListener(result, output, socket, tmcCli, settings);
        result.addListener(listener, threadPool);
    }

    public void listCourses(HashMap<String, String> params) throws ProtocolException, TmcCoreException {
        CliSettings settings = this.tmcCli.defaultSettings();
        if(loginIsDone(settings)) {
            ListenableFuture<List<Course>> coursesFuture = core.listCourses(settings);
            ResultListener coursesListener = new ListCoursesListener(coursesFuture, output, socket);
            coursesFuture.addListener(coursesListener, threadPool);
        }
    }

    public void listExercises(HashMap<String, String> params) throws ProtocolException, TmcCoreException {
        CliSettings settings = this.tmcCli.defaultSettings();
        if(loginIsDone(settings)) {
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
        CliSettings settings = this.tmcCli.defaultSettings();
        if(loginIsDone(settings)) {
            if (params.get("path") == null || params.get("path").isEmpty() || params.get("courseID") == null || params.get("courseID").isEmpty()) {
                throw new ProtocolException("Path and courseID required");
            }
            settings.setPath(params.get("path"));
            settings.setCourseID(params.get("courseID"));
            ListenableFuture<List<Exercise>> exercisesFuture = core.downloadExercises(params.get("path"), params.get("courseID"), settings);
            ResultListener resultListener = new DownloadExercisesListener(exercisesFuture, output, socket);
        }
    }

    public void logout(HashMap<String, String> params) {
        this.tmcCli.logout();
        writeToOutputSocket("User data cleared!");
    }

    public void submit(HashMap<String, String> params) throws ProtocolException {
        CliSettings settings = this.tmcCli.defaultSettings();
        if(loginIsDone(settings)) {
            if (!params.containsKey("path")) {
                throw new ProtocolException("path not supplied");
            }
            //CLEAR THIS MESS!!
            settings.setCourseID(params.get("courseID"));
            settings.setUserData(params.get("username"), params.get("password"));
            settings.setPath(params.get("path"));
            settings.setServerAddress(settings.getServerAddress());
            settings.setApiVersion("7");

            ListenableFuture<Course> currentCourse;
            try {
                sendSubmission(settings, params);
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private void sendSubmission(CliSettings settings, HashMap<String, String> params) throws TmcCoreException, ExecutionException, InterruptedException {
        ListenableFuture<Course> currentCourse;
        fetchCourseToSettings(settings);
        settings.setCourseID(params.get("courseID"));
        ListenableFuture<SubmissionResult> result = core.submit(params.get("path"), settings);
        result.addListener(new SubmissionListener(result, output, socket), threadPool);
    }

    private void fetchCourseToSettings(CliSettings settings) throws TmcCoreException, InterruptedException, ExecutionException {
        ListenableFuture<Course> currentCourse;
        String courseUrl = settings.getServerAddress() + "/courses/" + settings.getCourseID() + ".json?api_version=" + settings.apiVersion();
        currentCourse = core.getCourse(settings, courseUrl);
        Course course = currentCourse.get();
        settings.setCurrentCourse(currentCourse.get());
    }

    public void paste(HashMap<String, String> params) throws ProtocolException, TmcCoreException {
        CliSettings settings = this.tmcCli.defaultSettings();
        if (loginIsDone(settings)) {
            if (!params.containsKey("path")) {
                throw new ProtocolException("path not supplied");
            }
            settings.setCourseID(params.get("courseID"));
            settings.setUserData(params.get("username"), params.get("password"));
            settings.setPath(params.get("path"));
            ListenableFuture<URI> result = core.paste(params.get("path"), settings);
            result.addListener(new PasteListener(result, output, socket), threadPool);
        }
    }

    public void getMail(HashMap<String, String> params) throws ProtocolException {

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
    private boolean loginIsDone(TmcSettings settings)  {
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
