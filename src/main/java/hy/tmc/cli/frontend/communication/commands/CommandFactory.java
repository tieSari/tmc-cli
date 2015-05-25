package hy.tmc.cli.frontend.communication.commands;

import static hy.tmc.cli.frontend.communication.commands.CommandFactory.downloadExercises;

import hy.tmc.cli.frontend.FrontendListener;
import hy.tmc.cli.logic.Logic;

import java.util.HashMap;
import java.util.Set;

public class CommandFactory {

    /**
     * Maps command strings to objects.
     * @param frontend that the commands will use
     * @param logic that the commands will use
     * @return A map of names to corresponding commands
     */
    public static HashMap<String, Command> createCommandMap(
            FrontendListener frontend,
            Logic logic) {
        HashMap<String, Command> commandsByName = new HashMap<>();
        commandsByName.put("auth", authenticate(frontend, logic));
        commandsByName.put("help", help(frontend, logic));
        commandsByName.put("login", login(frontend, logic));
        commandsByName.put("ping", replyToPing(frontend, logic));
        commandsByName.put("listCourses", listCourses(frontend, logic));
        commandsByName.put("listExercises", listExercises(frontend, logic));
        commandsByName.put("downloadExercises", downloadExercises(frontend, logic));
        commandsByName.put("logout", logout(frontend, logic));
        return commandsByName;
    }

    /**
     * Create help Command object.
     *
     * @param front frontend that the command will use
     * @param back logic that the command will use
     * @return a help object
     */
    public static Command help(FrontendListener front, Logic back) {
        return new Help(front, back);
    }

    /**
     * Create replyToPing Command object.
     *
     * @param front frontend that the command will use
     * @param back logic that the command will use
     * @return a replyToPing object
     */
    public static Command replyToPing(FrontendListener front, Logic back) {
        return new ReplyToPing(front, back);
    }

    /**
     * Create an authenticate Command object.
     *
     * @param front frontend that the command will use
     * @param back logic that the command will use
     * @return an authenticate object
     */
    public static Command authenticate(FrontendListener front, Logic back) {
        return new Authenticate(front, back);
    }

    /**
     * Same as authenticate.
     *
     * @param front frontend to use
     * @param back backend to use
     * @return an authenticate bject
     */
    public static Command login(FrontendListener front, Logic back) {
        return new Authenticate(front, back); // NOTE: login == authenticate
    }

    /**
     * Create a listCourses Command object.
     *
     * @param front frontend that the command will use
     * @param back logic that the command will use
     * @return a listCourses object
     */
    public static Command listCourses(FrontendListener front, Logic back) {
        return new ListCourses(front, back);
    }

    /**
     * Create a listExercises Command object.
     *
     * @param front frontend that the command will use
     * @param back logic that the command will use
     * @return a listExercises object
     */
    public static Command listExercises(FrontendListener front, Logic back) {
        return new ListExercises(front, back);
    }

    /**
     * Create a downloadExercises Command object.
     *
     * @param front frontend that the command will use
     * @param back logic that the command will use
     * @return a downloadExercises object
     */
    public static Command downloadExercises(FrontendListener front, Logic back) {
        return new DownloadExercises(front, back);
    }

    /**
     * Create a logout Command object.
     *
     * @param front frontend that the command will use
     * @param back logic that the command will use
     * @return a logout object
     */
    public static Command logout(FrontendListener front, Logic back) {
        return new Logout(front, back);
    }

    /**
     *
     * @return a set of all available command names.
     */
    static Set<String> allCommandNames() {
        return createCommandMap(null, null).keySet();
    }

}
