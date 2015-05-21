package hy.tmc.cli.frontend_communication.Commands;

import hy.tmc.cli.frontend_communication.FrontendListener;
import hy.tmc.cli.logic.Logic;
import java.util.HashMap;
import java.util.Set;

public class CommandFactory {

    /**
     *
     * @param frontend that the commands will use
     * @param logic that the commands will use
     * @return A map of names to corresponding commands
     */
    public static HashMap<String, Command> createCommandMap(FrontendListener frontend, Logic logic) {
        HashMap<String, Command> commandsByName = new HashMap<>();
        commandsByName.put("auth", Authenticate(frontend, logic));
        commandsByName.put("help", Help(frontend, logic));
        commandsByName.put("login", Login(frontend, logic));
        commandsByName.put("ping", ReplyToPing(frontend, logic));
        commandsByName.put("listCourses", ListCourses(frontend, logic));
        commandsByName.put("listExercises", ListExercises(frontend, logic));
        commandsByName.put("downloadExercises", DownloadExercises(frontend, logic));
        return commandsByName;
    }

    /**
     * Create Help Command object
     *
     * @param front frontend that the command will use
     * @param back logic that the command will use
     * @return a Help object
     */
    public static Command Help(FrontendListener front, Logic back) {
        return new Help(front, back);
    }


    /**
     * Create ReplyToPing Command object
     *
     * @param front frontend that the command will use
     * @param back logic that the command will use
     * @return a ReplyToPing object
     */
    public static Command ReplyToPing(FrontendListener front, Logic back) {
        return new ReplyToPing(front, back);
    }

    /**
     * Create an Authenticate Command object
     *
     * @param front frontend that the command will use
     * @param back logic that the command will use
     * @return an Authenticate object
     */
    public static Command Authenticate(FrontendListener front, Logic back) {
        return new Authenticate(front, back);
    }

    /**
     * Same as Authenticate
     * @param front frontend to use
     * @param back backend to use
     * @return an Authenticate bject
     */
    public static Command Login(FrontendListener front, Logic back) {
        return new Authenticate(front, back); // NOTE: login == Authenticate
    }

    /**
     * Create a ListCourses Command object
     *
     * @param front frontend that the command will use
     * @param back logic that the command will use
     * @return a ListCourses object
     */
    public static Command ListCourses(FrontendListener front, Logic back) {
        return new ListCourses(front, back);
    }

    /**
     * Create a ListExercises Command object
     *
     * @param front frontend that the command will use
     * @param back logic that the command will use
     * @return a ListExercises object
     */
    public static Command ListExercises(FrontendListener front, Logic back) {
        return new ListExercises(front, back);
    }
    
    /**
     * Create a DownloadExercises Command object
     *
     * @param front frontend that the command will use
     * @param back logic that the command will use
     * @return a DownloadExercises object
     */
    public static Command DownloadExercises(FrontendListener front, Logic back) {
        return new DownloadExercises(front, back);
    }

    /**
     *
     * @return a set of all available command names
     */
    static Set<String> allCommandNames() {
        return createCommandMap(null, null).keySet();
    }

}
