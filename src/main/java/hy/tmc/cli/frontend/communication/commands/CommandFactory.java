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
    public static CommandResultParser help() {
        return new Help();
    }




    /**
     * Create RunTests command object.
     *
     * @return a help object
     */
<<<<<<< HEAD
    public static CommandResultParser runTests() {
        return new RunTests();
=======
    public static Command runTests() {
        return new TestsListener();
>>>>>>> 6f0a156e8a5a06410f1f1f312e949c5877ace448
    }

    /**
     * Create an Authenticate Command object.
     *
     * @return an authenticate object
     */
<<<<<<< HEAD
    public static CommandResultParser authenticate() {
        return new Authenticate();
=======
    public static Command authenticate() {
        return new LoginListener();
>>>>>>> 6f0a156e8a5a06410f1f1f312e949c5877ace448
    }

    /**
     * Same as authenticate.
     *
     * @return an authenticate object
     */
<<<<<<< HEAD
    public static CommandResultParser login() {
        return new Authenticate(); // NOTE: login == authenticate
=======
    public static Command login() {
        return new LoginListener(); // NOTE: login == authenticate
>>>>>>> 6f0a156e8a5a06410f1f1f312e949c5877ace448
    }

    /**
     * Create a ListCourses Command object.
     *
     * @return a listCourses object
     */
<<<<<<< HEAD
    public static CommandResultParser listCourses() {
        return new ListCourses();
=======
    public static Command listCourses() {
        return new ListCoursesListener();
>>>>>>> 6f0a156e8a5a06410f1f1f312e949c5877ace448
    }

    /**
     * Create a ListExercises Command object.
     *
     * @return a listExercises object
     */
<<<<<<< HEAD
    public static CommandResultParser listExercises() {
        return new ListExercises();
=======
    public static Command listExercises() {
        return new ListExercisesListener();
>>>>>>> 6f0a156e8a5a06410f1f1f312e949c5877ace448
    }

    /**
<<<<<<< HEAD
=======
     * Create a Logout Command object.
     *
     * @return a logout object
     */
    public static CommandResultParser logout() {
        return new Logout();
    }

    /**
>>>>>>> fb9b9438df95fc78bb7768c5fdbc67a0fca749aa
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
<<<<<<< HEAD
    public static CommandResultParser submit() {
        return new Submit();
=======
    public static Command submit() {
        return new SubmissionListener();
>>>>>>> 6f0a156e8a5a06410f1f1f312e949c5877ace448
    }

    /**
     * Create a Paste Command object.
     *
     * @return a Paste object
     */
<<<<<<< HEAD
    public static CommandResultParser paste() {
        return new Paste();
    }
    
    public static CommandResultParser getMail() {
        return new MailChecker();
=======
    public static Command paste() {
        return new PasteListener();
>>>>>>> 6f0a156e8a5a06410f1f1f312e949c5877ace448
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
