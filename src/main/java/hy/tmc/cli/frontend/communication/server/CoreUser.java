
package hy.tmc.cli.frontend.communication.server;

import com.google.common.util.concurrent.ListenableFuture;
import hy.tmc.cli.configuration.ClientData;
import hy.tmc.cli.frontend.communication.commands.Authenticate;
import hy.tmc.cli.frontend.communication.commands.ChooseServer;
import hy.tmc.cli.frontend.communication.commands.Command;
import hy.tmc.cli.frontend.communication.commands.Help;
import hy.tmc.cli.frontend.communication.commands.ListCourses;
import hy.tmc.cli.frontend.communication.commands.ListExercises;
import hy.tmc.cli.frontend.communication.commands.Logout;
import hy.tmc.cli.frontend.communication.commands.MailChecker;
import hy.tmc.cli.frontend.communication.commands.Paste;
import hy.tmc.cli.frontend.communication.commands.RunTests;
import hy.tmc.cli.frontend.communication.commands.Submit;
import hy.tmc.core.TmcCore;
import hy.tmc.core.configuration.TmcSettings;
import hy.tmc.core.exceptions.TmcCoreException;
import java.util.HashMap;

public class CoreUser {
    
    private TmcCore core;
    private TmcSettings settings;
    
    public CoreUser(TmcSettings settings){
        this.core = new TmcCore();
        this.settings = settings;
    }
    
    public ListenableFuture<?> findAndExecute(String commandName, HashMap<String, String> params) throws ProtocolException{
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
    public ListenableFuture<?> runTests(HashMap<String, String> params) throws ProtocolException {
        if (!params.containsKey("path") || params.get("path").isEmpty()) {
            throw new ProtocolException("File path to exercise required.");
        }
    }

    /**
     * Execute authenticate
     *
     * @return authenticate listenable future
     */
    public ListenableFuture<?> authenticate(HashMap<String, String> params) throws ProtocolException {
        String username = params.get("username");
        if (username == null || username.isEmpty()) {
            throw new ProtocolException("username must be set!");
        }
        String password = params.get("password");
        if (password == null || password.isEmpty()) {
            throw new ProtocolException("password must be set!");
        }
    }
    
    
    /**
     * Execute ListCourses
     *
     * @return a listCourses listenablefuture
     */
    public static ListenableFuture<?> listCourses(HashMap<String, String> params) throws ProtocolException {
        if(params.get("username").isEmpty() || params.get("password").isEmpty()){
            throw new ProtocolException("Username and password must be set!");
        }
    }

    /**
     * Execute ListExercises
     *
     * @return a listexercises listenablefuture
     */
    public static ListenableFuture<?> listExercises(HashMap<String, String> params) throws ProtocolException {
        if (!params.containsKey("path")) {
            throw new ProtocolException("Path not recieved");
        }
        if (params.get("username").isEmpty() || params.get("password").isEmpty()) {
            throw new ProtocolException("Please authorize first.");
        }
    }

    /**
     * Execute DownloadExercises command
     *
     * @return a downloadexercises listenablefuture
     */
    public ListenableFuture<?> downloadExercises(HashMap<String, String> params) throws ProtocolException {
        if (params.get("path") == null || params.get("path").isEmpty() || params.get("courseID") == null || params.get("courseID").isEmpty()) {
            throw new ProtocolException("Path and courseID required");
        }
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
    public ListenableFuture<?> submit(HashMap<String, String> params) throws ProtocolException {
        if (params.get("username").isEmpty() || params.get("password").isEmpty()) {
            throw new ProtocolException("User must be authorized first");
        }
        if (!params.containsKey("path")) {
            throw new ProtocolException("path not supplied");
        }
    }
    
    /**
     * Execute paste
     *
     * @return a Paste listenablefuture
     */
    public ListenableFuture<?> paste(HashMap<String, String> params) throws ProtocolException {
        if (params.get("username").isEmpty() || params.get("password").isEmpty()) {
            throw new ProtocolException("User must be authorized first");
        }
        if (!params.containsKey("path")) {
            throw new ProtocolException("path not supplied");
        }
    }
    
    public ListenableFuture<?> getMail(HashMap<String, String> params) throws ProtocolException {
        if (params.get("username").isEmpty() || params.get("password").isEmpty()) {
            throw new ProtocolException("User must be authorized first");
        }
    }
}
