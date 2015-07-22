package hy.tmc.cli.frontend.communication.commands;

import hy.tmc.cli.listeners.ListExercisesListener;
import hy.tmc.cli.listeners.TestsListener;
import hy.tmc.cli.listeners.PasteListener;
import hy.tmc.cli.listeners.SubmissionListener;
import hy.tmc.cli.listeners.LoginListener;
import hy.tmc.cli.listeners.ListCoursesListener;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CommandFactory {

    /**
     * Maps command strings to objects.
     *
     * @return A map of names to corresponding commands
     */
    public static Map<String, Command> createCommandMap() {
        HashMap<String, Command> commandsByName = new HashMap<>();

        commandsByName.put("auth", authenticate());
        commandsByName.put("help", help());
        commandsByName.put("login", login());
        commandsByName.put("listCourses", listCourses());
        commandsByName.put("listExercises", listExercises());
        commandsByName.put("setServer", chooseServer());
        commandsByName.put("submit", submit());
        commandsByName.put("runTests", runTests());
        commandsByName.put("paste", paste());
//        commandsByName.put("getMail", getMail());
        return commandsByName;
    }



    /**
     * Create Help Command object.
     *
     * @return a help object
     */
    public static Command help() {
        return new Help();
    }




    /**
     * Create RunTests command object.
     *
     * @return a help object
     */
    public static Command runTests() {
        return new TestsListener();
    }

    /**
     * Create an Authenticate Command object.
     *
     * @return an authenticate object
     */
    public static Command authenticate() {
        return new LoginListener();
    }

    /**
     * Same as authenticate.
     *
     * @return an authenticate object
     */
    public static Command login() {
        return new LoginListener(); // NOTE: login == authenticate
    }

    /**
     * Create a ListCourses Command object.
     *
     * @return a listCourses object
     */
    public static Command listCourses() {
        return new ListCoursesListener();
    }

    /**
     * Create a ListExercises Command object.
     *
     * @return a listExercises object
     */
    public static Command listExercises() {
        return new ListExercisesListener();
    }

    /**
<<<<<<< HEAD
=======
     * Create a Logout Command object.
     *
     * @return a logout object
     */
    public static Command logout() {
        return new Logout();
    }

    /**
>>>>>>> fb9b9438df95fc78bb7768c5fdbc67a0fca749aa
     * Create a chooseServer Command object.
     *
     * @return a chooseServer object
     */
    public static Command chooseServer() {
        return new ChooseServer();
    }

    /**
     * Create a Submit Command object.
     *
     * @return a Submit object
     */
    public static Command submit() {
        return new SubmissionListener();
    }

    /**
     * Create a Paste Command object.
     *
     * @return a Paste object
     */
    public static Command paste() {
        return new PasteListener();
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
