package hy.tmc.cli.frontend.communication.commands;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CommandFactory {

    /**
     * Maps command strings to objects.
     *
     * @return A map of names to corresponding commands
     */
    public static Map<String, CommandResultParser> createCommandMap() {
        HashMap<String, CommandResultParser> commandsByName = new HashMap<>();

        commandsByName.put("auth", authenticate());
        commandsByName.put("help", help());
        commandsByName.put("login", login());
        commandsByName.put("listCourses", listCourses());
        commandsByName.put("listExercises", listExercises());
        commandsByName.put("logout", logout());
        commandsByName.put("setServer", chooseServer());
        commandsByName.put("submit", submit());
        commandsByName.put("runTests", runTests());
        commandsByName.put("paste", paste());
        commandsByName.put("getMail", getMail());
        return commandsByName;
    }



    /**
     * Create Help Command object.
     *
     * @return a help object
     */
    public static CommandResultParser help() {
        return new Help();
    }




    /**
     * Create RunTests command object.
     *
     * @return a help object
     */
    public static CommandResultParser runTests() {
        return new RunTests();
    }

    /**
     * Create an Authenticate Command object.
     *
     * @return an authenticate object
     */
    public static CommandResultParser authenticate() {
        return new Authenticate();
    }

    /**
     * Same as authenticate.
     *
     * @return an authenticate object
     */
    public static CommandResultParser login() {
        return new Authenticate(); // NOTE: login == authenticate
    }

    /**
     * Create a ListCourses Command object.
     *
     * @return a listCourses object
     */
    public static CommandResultParser listCourses() {
        return new ListCourses();
    }

    /**
     * Create a ListExercises Command object.
     *
     * @return a listExercises object
     */
    public static CommandResultParser listExercises() {
        return new ListExercises();
    }

    /**
     * Create a Logout Command object.
     *
     * @return a logout object
     */
    public static CommandResultParser logout() {
        return new Logout();
    }

    /**
     * Create a chooseServer Command object.
     *
     * @return a chooseServer object
     */
    public static CommandResultParser chooseServer() {
        return new ChooseServer();
    }

    /**
     * Create a Submit Command object.
     *
     * @return a Submit object
     */
    public static CommandResultParser submit() {
        return new Submit();
    }

    /**
     * Create a Paste Command object.
     *
     * @return a Paste object
     */
    public static CommandResultParser paste() {
        return new Paste();
    }
    
    public static CommandResultParser getMail() {
        return new MailChecker();
    }

    /**
     * Takes the command map and returns a set of command names.
     *
     * @return a set of all available command names.
     */
    public static Set<String> allCommandNames() {
        return createCommandMap().keySet();
    }
}
