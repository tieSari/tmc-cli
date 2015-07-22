
package hy.tmc.cli.frontend.communication.server;

import com.google.common.util.concurrent.ListenableFuture;
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
import hy.tmc.core.exceptions.TmcCoreException;
import java.io.IOException;
import java.util.HashMap;

public class CoreUser {
    
    private TmcCore core;
    private SocketListener commandParser;
    
    public CoreUser(SocketListener commandParser){
        this.core = new TmcCore();
        this.commandParser = commandParser;
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
    public ListenableFuture<?> runTests(HashMap<String, String> params) throws ProtocolException, TmcCoreException {
        if (!params.containsKey("path") || params.get("path").isEmpty()) {
            throw new ProtocolException("File path to exercise required.");
        }
        CliSettings settings = new CliSettings();
        settings.setMainDirectory(params.get("path"));
        ListenableFuture<?> result = core.test(params.get("path"), settings);
        commandParser.parseData(result);
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
    public ListenableFuture<?> authenticate(HashMap<String, String> params) throws ProtocolException, TmcCoreException {
        if(userDataNotExists(params)){
            throw new ProtocolException("Username and password must be given!");
        }
        CliSettings settings = new CliSettings();
        settings.setUserData(params.get("username"), params.get("password"));
        ListenableFuture<?> result = core.verifyCredentials(settings);
    }
    
    
    /**
     * Execute ListCourses
     *
     * @return a listCourses listenablefuture
     */
    public ListenableFuture<?> listCourses(HashMap<String, String> params) throws ProtocolException {
        if(userDataNotExists(params)){
            throw new ProtocolException("Username or password must be given!");
        }
        CliSettings settings = new CliSettings();
        settings.setUserData(params.get("username"), params.get("password"));
        ListenableFuture<?> result = core.listCourses(settings);
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
    public ListenableFuture<?> submit(HashMap<String, String> params) throws ProtocolException, TmcCoreException {
         if(userDataNotExists(params)){
            throw new ProtocolException("Username or password must be given!");
        }
        if (!params.containsKey("path")) {
            throw new ProtocolException("path not supplied");
        }
        CliSettings settings = new CliSettings();
        settings.setUserData(params.get("username"), params.get("password"));
        settings.setPath(params.get("path"));
        core.submit(params.get("path"), settings);
    }
    
    /**
     * Execute paste
     *
     * @return a Paste listenablefuture
     */
    public ListenableFuture<?> paste(HashMap<String, String> params) throws ProtocolException, TmcCoreException {
         if(userDataNotExists(params)){
            throw new ProtocolException("Username or password must be given!");
        }
        if (!params.containsKey("path")) {
            throw new ProtocolException("path not supplied");
        }
        
        CliSettings settings = new CliSettings();
        settings.setUserData(params.get("username"), params.get("password"));
        settings.setPath(params.get("path"));
        core.paste(params.get("path"), settings);
    }
    
    public ListenableFuture<?> getMail(HashMap<String, String> params) throws ProtocolException {
        if(userDataNotExists(params)){
            throw new ProtocolException("Username or password must be given!");
        }
    }
}
