package hy.tmc.cli.frontend.communication.commands;

import hy.tmc.cli.domain.submission.FeedbackQuestion;
import hy.tmc.cli.frontend.FrontendListener;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class CommandFactory {

    /**
     * Maps command strings to objects.
     *
     * @param frontend that the commands will use
     * @return A map of names to corresponding commands
     */
    public static HashMap<String, Command> createCommandMap(FrontendListener frontend) {
        HashMap<String, Command> commandsByName = new HashMap<>();
        commandsByName.put("auth", authenticate(frontend));
        commandsByName.put("help", help(frontend));
        commandsByName.put("login", login(frontend));
        commandsByName.put("ping", replyToPing(frontend));
        commandsByName.put("listCourses", listCourses(frontend));
        commandsByName.put("listExercises", listExercises(frontend));
        commandsByName.put("downloadExercises", downloadExercises(frontend));
        commandsByName.put("logout", logout(frontend));
        commandsByName.put("setServer", chooseServer(frontend));
        commandsByName.put("submit", submit(frontend));
        commandsByName.put("runTests", runTests(frontend));
        return commandsByName;
    }

    /**
     * Create Help Command object.
     *
     * @param front frontend that the command will use
     * @return a help object
     */
    public static Command help(FrontendListener front) {
        return new Help(front);
    }

    /**
     * Create ReplyToPing Command object.
     *
     * @param front frontend that the command will use
     * @return a replyToPing object
     */
    public static Command replyToPing(FrontendListener front) {
        return new ReplyToPing(front);
    }
    
    /**
     * Create RunTests command object.
     * @param front frontend that the command will use
     * @param back logic that the command will use
     * @return a help object
     */
    public static Command runTests(FrontendListener front){
        return new RunTests(front);
    }

    /**
     * Create an Authenticate Command object.
     *
     * @param front frontend that the command will use
     * @return an authenticate object
     */
    public static Command authenticate(FrontendListener front) {
        return new Authenticate(front);
    }

    /**
     * Same as authenticate.
     *
     * @param front frontend to use
     * @return an authenticate bject
     */
    public static Command login(FrontendListener front) {
        return new Authenticate(front); // NOTE: login == authenticate
    }

    /**
     * Create a ListCourses Command object.
     *
     * @param front frontend that the command will use
     * @return a listCourses object
     */
    public static Command listCourses(FrontendListener front) {
        return new ListCourses(front);
    }

    /**
     * Create a ListExercises Command object.
     *
     * @param front frontend that the command will use
     * @return a listExercises object
     */
    public static Command listExercises(FrontendListener front) {
        return new ListExercises(front);
    }

    /**
     * Create a DownloadExercises Command object.
     *
     * @param front frontend that the command will use
     * @return a downloadExercises object
     */
    public static Command downloadExercises(FrontendListener front) {
        return new DownloadExercises(front);
    }

    /**
     * Create a Logout Command object.
     *
     * @param front frontend that the command will use
     * @return a logout object
     */
    public static Command logout(FrontendListener front) {
        return new Logout(front);
    }

    /**
     * Create a chooseServer Command object.
     *
     * @param front frontend that the command will use
     * @return a chooseServer object
     */
    public static Command chooseServer(FrontendListener front) {
        return new ChooseServer(front);
    }
    
    /**
     * Create a Submit Command object.
     *
     * @param front frontend that the command will use
     * @param back logic that the command will use
     * @return a Submit object
     */
    
    public static Command submit(FrontendListener front) {
        return new Submit(front);
    }

    /**
     * Takes the command map and returns a set of command names.
     *
     * @return a set of all available command names.
     */
    static Set<String> allCommandNames() {
        FrontendListener stub = new FrontendListener() {
            @Override
            public void start() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void printLine(String line) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void feedback(List<FeedbackQuestion> feedbackQuestions, String feedbackUrl) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        };

        return createCommandMap(stub).keySet();
    }

}
