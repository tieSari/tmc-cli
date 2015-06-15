package hy.tmc.cli.frontend.communication.commands;


import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CommandFactory {

    /**
     * Maps command strings to objects.
     *
     * @param that the commands will use
     * @return A map of names to corresponding commands
     */
    public static Map<String, Command> createCommandMap() {
        HashMap<String, Command> commandsByName = new HashMap<>();

        commandsByName.put("auth", authenticate());
        commandsByName.put("help", help());
        commandsByName.put("login", login());
        commandsByName.put("ping", replyToPing());
        commandsByName.put("listCourses", listCourses());
        commandsByName.put("listExercises", listExercises());
        commandsByName.put("downloadExercises", downloadExercises());
        commandsByName.put("logout", logout());
        commandsByName.put("setServer", chooseServer());
        commandsByName.put("submit", submit());
        commandsByName.put("runTests", runTests());
        commandsByName.put("paste", paste());
        commandsByName.put("stopProcess", stopProcess());
//        commandsByName.put("answerQuestion", answerQuestion());

        return commandsByName;
    }

    /**
     * Create Help Command object.
     *
     * @param that the command will use
     * @return a help object
     */
    public static Command help() {
        return new Help();
    }

    /**
     * Create ReplyToPing Command object.
     *
     * @param that the command will use
     * @return a replyToPing object
     */
    public static Command replyToPing() {
        return new ReplyToPing();
    }

    /**
     * Create RunTests command object.
     *
     * @param that the command will use
     * @return a help object
     */
    public static Command runTests() {
        return new RunTests();
    }

    /**
     * Create an Authenticate Command object.
     *
     * @param that the command will use
     * @return an authenticate object
     */
    public static Command authenticate() {
        return new Authenticate();
    }

    /**
     * Same as authenticate.
     *
     * @param to use
     * @return an authenticate bject
     */
    public static Command login() {
        return new Authenticate(); // NOTE: login == authenticate
    }

    /**
     * Create a ListCourses Command object.
     *
     * @param that the command will use
     * @return a listCourses object
     */
    public static Command listCourses() {
        return new ListCourses();
    }

    /**
     * Create a ListExercises Command object.
     *
     * @param that the command will use
     * @return a listExercises object
     */
    public static Command listExercises() {
        return new ListExercises();
    }

    /**
     * Create a DownloadExercises Command object.
     *
     * @param that the command will use
     * @return a downloadExercises object
     */
    public static Command downloadExercises() {
        return new DownloadExercises();
    }

    /**
     * Create a Logout Command object.
     *
     * @param that the command will use
     * @return a logout object
     */
    public static Command logout() {
        return new Logout();
    }

    /**
     * Create a chooseServer Command object.
     *
     * @param that the command will use
     * @return a chooseServer object
     */
    public static Command chooseServer() {
        return new ChooseServer();
    }

    /**
     * Create a Submit Command object.
     *
     * @param that the command will use
     * @return a Submit object
     */
    public static Command submit() {
        return new Submit();
    }

    /**
     * Stops the whole process and exits java virtual machine.
     *
     * @param that the command will use
     * @return StopProcess object
     */
    private static Command stopProcess() {
        return new StopProcess();
    }

    /**
     * Create a Paste Command object.
     *
     * @param that the command will use
     * @return a Paste object
     */
    public static Command paste() {
        return new Paste();
    }

//    public static Command answerQuestion() {
//        return new AnswerQuestion();
//    }

    /**
     * Takes the command map and returns a set of command names.
     *
     * @return a set of all available command names.
     */
    public static Set<String> allCommandNames() {
        return createCommandMap().keySet();
    }
}
