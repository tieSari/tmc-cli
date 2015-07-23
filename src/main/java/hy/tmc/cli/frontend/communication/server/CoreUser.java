
package hy.tmc.cli.frontend.communication.server;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import fi.helsinki.cs.tmc.langs.domain.RunResult;
import hy.tmc.cli.CliSettings;
import hy.tmc.cli.TmcCli;
import hy.tmc.cli.listeners.*;
import hy.tmc.core.TmcCore;
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

public class CoreUser {
    
    private TmcCore core;
    private DataOutputStream output;
    private Socket socket;
    private ListeningExecutorService threadPool;
    private TmcCli tmcCli;
    
    public CoreUser(TmcCli tmcCli, DataOutputStream output, Socket socket, ListeningExecutorService pool) throws IOException {
        this.core = tmcCli.getCore();
        this.threadPool = pool;
        this.tmcCli = tmcCli;
    }
    
    public void findAndExecute(String commandName, HashMap<String, String> params) throws ProtocolException, TmcCoreException, IOException{
        switch(commandName){
            case "help":
                help(params);
            case "login":
                authenticate(params);
            case "listCourses":
                listCourses(params);
            case "listExercises":
                listExercises(params);
            case "downloadExercises":
                downloadExercises(params);
            case "logout":
                logout(params);
            case "setServer":
                chooseServer(params);
            case "submit":
                submit(params);
            case "runTests":
                runTests(params);
            case "paste":
                paste(params);
            case "getMail":
                getMail(params);
            default:
                throw new ProtocolException("Command not found.");
        }
    }
    
     /**
     * Execute help. 
     *
     * a help listenablefuture
     */
    public ListenableFuture<?> help(HashMap<String, String> params) {
        
    }

    /**
     * Execute RunTests
     *
     * @return a runtests listenablefuture
     */
    public ListenableFuture<RunResult> runTests(HashMap<String, String> params) throws ProtocolException, TmcCoreException {
        if (!params.containsKey("path") || params.get("path").isEmpty()) {
            throw new ProtocolException("File path to exercise required.");
        }
        CliSettings settings = new CliSettings();
        settings.setMainDirectory(params.get("path"));
        ListenableFuture<RunResult> result = core.test(params.get("path"), settings);
        return result;
    }
    
    private void validateUserData(HashMap<String, String> params) throws ProtocolException{
        String username = params.get("username");
        if (username == null || username.isEmpty()) {
            throw new ProtocolException("Username must be given!");
        }
        String password = params.get("password");
        if (password == null || password.isEmpty()) {
            throw new ProtocolException("Password must be given!");
        }
    }

    /**
     * Execute authenticate
     *
     * @return authenticate listenable future
     */
    public void authenticate(HashMap<String, String> params) throws ProtocolException, TmcCoreException {
        validateUserData(params);
        CliSettings settings = new CliSettings();
        settings.setUserData(params.get("username"), params.get("password"));
        ListenableFuture<Boolean> result = core.verifyCredentials(settings);
        LoginListener listener = new LoginListener(result, output, socket);
        result.addListener(listener, threadPool);
    }
    
    
    /**
     * Execute ListCourses
     *
     * @return a listCourses listenablefuture
     */
    public void listCourses(HashMap<String, String> params) throws ProtocolException, TmcCoreException {
        validateUserData(params);
        CliSettings settings = new CliSettings();
        settings.setUserData(params.get("username"), params.get("password"));
        ListenableFuture<List<Course>> coursesFuture = core.listCourses(settings);
        ResultListener coursesListener = new ListCoursesListener(coursesFuture, output, socket);
        coursesFuture.addListener(coursesListener, threadPool);
    }

    /**
     * Execute ListExercises
     *
     * @return a listexercises listenablefuture
     */
    public void listExercises(HashMap<String, String> params) throws ProtocolException, TmcCoreException {
        if (!params.containsKey("path")) {
            throw new ProtocolException("Path not recieved");
        }
        validateUserData(params);
        CliSettings settings = new CliSettings();
        settings.setPath(params.get("path"));
        settings.setUserData(params.get("username"), params.get("password"));
        ListenableFuture<Course> course = core.getCourse(settings, params.get("path"));
        ResultListener exercisesListener = new ListExercisesListener(course, output, socket);
        course.addListener(exercisesListener, threadPool);
    }

    /**
     * Execute DownloadExercises command
     *
     * @return a downloadexercises listenablefuture
     */
    public void downloadExercises(HashMap<String, String> params) throws ProtocolException, TmcCoreException, IOException {
        if (params.get("path") == null || params.get("path").isEmpty() || params.get("courseID") == null || params.get("courseID").isEmpty()) {
            throw new ProtocolException("Path and courseID required");
        }
        CliSettings settings = new CliSettings();
        settings.setPath(params.get("path"));
        settings.setCourseID(params.get("courseID"));
        ListenableFuture<List<Exercise>> exercisesFuture = core.downloadExercises(params.get("path"), params.get("courseID"), settings);
        ResultListener resultListener = new DownloadExercisesListener(exercisesFuture, output, socket);
    }

    /**
     * Execute logout command
     *
     * @return a logout listenablefuture
     */
    public void logout(HashMap<String, String> params) {
        this.tmcCli.logout();
        String message = "User data cleared!";
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
     * Execute ChooseServer
     *
     * @return a chooseServer listenablefuture
     */
    public ListenableFuture<?> chooseServer(HashMap<String, String> params) throws ProtocolException {

    }

    /**
     * Execute submit
     *
     * @return a Submit listenablefuture
     */
    public ListenableFuture<SubmissionResult> submit(HashMap<String, String> params) throws ProtocolException, TmcCoreException {
        validateUserData(params);
        if (!params.containsKey("path")) {
            throw new ProtocolException("path not supplied");
        }
        CliSettings settings = new CliSettings();
        settings.setUserData(params.get("username"), params.get("password"));
        settings.setPath(params.get("path"));
        ListenableFuture<SubmissionResult> result = core.submit(params.get("path"), settings);
    }
    
    /**
     * Execute paste
     *
     * @return a Paste listenablefuture
     */
    public ListenableFuture<URI> paste(HashMap<String, String> params) throws ProtocolException, TmcCoreException {
        validateUserData(params);
        if (!params.containsKey("path")) {
            throw new ProtocolException("path not supplied");
        }
        
        CliSettings settings = new CliSettings();
        settings.setUserData(params.get("username"), params.get("password"));
        settings.setPath(params.get("path"));
        ListenableFuture<URI> result = core.paste(params.get("path"), settings);
        
    }
    
    public ListenableFuture<?> getMail(HashMap<String, String> params) throws ProtocolException {
        return null;
    }
}
