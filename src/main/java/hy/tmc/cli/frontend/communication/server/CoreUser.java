
package hy.tmc.cli.frontend.communication.server;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import fi.helsinki.cs.tmc.langs.domain.RunResult;
import hy.tmc.cli.CliSettings;
import hy.tmc.cli.configuration.ClientData;
import hy.tmc.cli.listeners.LoginListener;
import hy.tmc.cli.frontend.communication.commands.ChooseServer;
import hy.tmc.cli.frontend.communication.commands.CommandResultParser;
import hy.tmc.cli.frontend.communication.commands.Help;
import hy.tmc.cli.listeners.ListCoursesListener;
import hy.tmc.cli.listeners.ListExercisesListener;
import hy.tmc.cli.frontend.communication.commands.Logout;
import hy.tmc.cli.frontend.communication.commands.MailChecker;
import hy.tmc.cli.listeners.PasteListener;
import hy.tmc.cli.listeners.TestsListener;
import hy.tmc.cli.listeners.SubmissionListener;
import hy.tmc.core.TmcCore;
import hy.tmc.core.configuration.TmcSettings;
import hy.tmc.core.domain.Course;
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
    private ListeningExecutorService pool;
    
    public CoreUser(DataOutputStream output, Socket socket, ListeningExecutorService pool){
        this.core = new TmcCore();
        this.pool = pool;
    }
    
    public ListenableFuture<?> findAndExecute(String commandName, HashMap<String, String> params) throws ProtocolException, TmcCoreException, IOException{
        switch(commandName){
            case "help":
                return help(params);
            case "login":
                return authenticate(params);
            case "listCourses":
                return listCourses(params);
            case "listExercises":
                return listExercises(params);
            case "downloadExercises":
                return downloadExercises(params);
            case "logout":
                return logout(params);
            case "setServer":
                return chooseServer(params);
            case "submit":
                return submit(params);
            case "runTests":
                return runTests(params);
            case "paste":
                return paste(params);
            case "getMail":
                return getMail(params);    
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
    
    private boolean userDataNotExists(HashMap<String, String> params) throws ProtocolException{
        String username = params.get("username");
        if (username == null || username.isEmpty()) {
            return  true;
        }
        String password = params.get("password");
        if (password == null || password.isEmpty()) {
            return true;
        }
        return false;
    }

    /**
     * Execute authenticate
     *
     * @return authenticate listenable future
     */
    public void authenticate(HashMap<String, String> params) throws ProtocolException, TmcCoreException {
        if(userDataNotExists(params)){
            throw new ProtocolException("Username and password must be given!");
        }
        CliSettings settings = new CliSettings();
        settings.setUserData(params.get("username"), params.get("password"));
        ListenableFuture<Boolean> result = core.verifyCredentials(settings);
        LoginListener listener = new LoginListener(result, output, socket);
        result.addListener(listener, pool);
    }
    
    
    /**
     * Execute ListCourses
     *
     * @return a listCourses listenablefuture
     */
    public ListenableFuture<List<Course>> listCourses(HashMap<String, String> params) throws ProtocolException, TmcCoreException {
        if(userDataNotExists(params)){
            throw new ProtocolException("Username or password must be given!");
        }
        CliSettings settings = new CliSettings();
        settings.setUserData(params.get("username"), params.get("password"));
        ListenableFuture<List<Course>> result = core.listCourses(settings);
    }

    /**
     * Execute ListExercises
     *
     * @return a listexercises listenablefuture
     */
    public ListenableFuture<?> listExercises(HashMap<String, String> params) throws ProtocolException {
        if (!params.containsKey("path")) {
            throw new ProtocolException("Path not recieved");
        }
        if(userDataNotExists(params)){
            throw new ProtocolException("Username or password must be given!");
        }
        CliSettings settings = new CliSettings();
        settings.setPath(params.get("path"));
        settings.setUserData(params.get("username"), params.get("password"));
    }

    /**
     * Execute DownloadExercises command
     *
     * @return a downloadexercises listenablefuture
     */
    public ListenableFuture<?> downloadExercises(HashMap<String, String> params) throws ProtocolException, TmcCoreException, IOException {
        if (params.get("path") == null || params.get("path").isEmpty() || params.get("courseID") == null || params.get("courseID").isEmpty()) {
            throw new ProtocolException("Path and courseID required");
        }
        CliSettings settings = new CliSettings();
        settings.setPath(params.get("path"));
        settings.setCourseID(params.get("courseID"));
        core.downloadExercises(params.get("path"), params.get("courseID"), settings);
    }

    /**
     * Execute logout command
     *
     * @return a logout listenablefuture
     */
    public ListenableFuture<?> logout(HashMap<String, String> params) {
        
    }

    /**
     * Execute ChooseServer
     *
     * @return a chooseServer listenablefuture
     */
    public ListenableFuture<?> chooseServer(HashMap<String, String> params) throws ProtocolException {
        if (!params.containsKey("tmc-server")) {
            throw new ProtocolException("must specify new server");
        }
        
    }

    /**
     * Execute submit
     *
     * @return a Submit listenablefuture
     */
    public ListenableFuture<SubmissionResult> submit(HashMap<String, String> params) throws ProtocolException, TmcCoreException {
         if(userDataNotExists(params)){
            throw new ProtocolException("Username or password must be given!");
        }
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
         if(userDataNotExists(params)){
            throw new ProtocolException("Username or password must be given!");
        }
        if (!params.containsKey("path")) {
            throw new ProtocolException("path not supplied");
        }
        
        CliSettings settings = new CliSettings();
        settings.setUserData(params.get("username"), params.get("password"));
        settings.setPath(params.get("path"));
        ListenableFuture<URI> result = core.paste(params.get("path"), settings);
    }
    
    public ListenableFuture<?> getMail(HashMap<String, String> params) throws ProtocolException {
        if(userDataNotExists(params)){
            throw new ProtocolException("Username or password must be given!");
        }
    }
}
