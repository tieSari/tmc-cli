package hy.tmc.cli.frontend.communication.commands;

import hy.tmc.cli.domain.submission.FeedbackQuestion;
import hy.tmc.cli.frontend.FrontendListener;
import hy.tmc.cli.logic.Logic;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class CommandFactory {

    /**
     * Maps command strings to objects.
     *
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
        commandsByName.put("setServer", chooseServer(frontend, logic));
        commandsByName.put("submit", submit(frontend, logic));
        commandsByName.put("runTests", runTests(frontend, logic));
        return commandsByName;
    }

    /**
     * Create Help Command object.
     *
     * @param front frontend that the command will use
     * @param back logic that the command will use
     * @return a help object
     */
    public static Command help(FrontendListener front, Logic back) {
        return new Help(front, back);
    }

    /**
     * Create ReplyToPing Command object.
     *
     * @param front frontend that the command will use
     * @param back logic that the command will use
     * @return a replyToPing object
     */
    public static Command replyToPing(FrontendListener front, Logic back) {
        return new ReplyToPing(front, back);
    }
    
    /**
     * Create RunTests command object.
     * @param front frontend that the command will use
     * @param back logic that the command will use
     * @return a help object
     */
    public static Command runTests(FrontendListener front, Logic back){
        return new RunTests(front, back);
    }

    /**
     * Create an Authenticate Command object.
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
     * Create a ListCourses Command object.
     *
     * @param front frontend that the command will use
     * @param back logic that the command will use
     * @return a listCourses object
     */
    public static Command listCourses(FrontendListener front, Logic back) {
        return new ListCourses(front, back);
    }

    /**
     * Create a ListExercises Command object.
     *
     * @param front frontend that the command will use
     * @param back logic that the command will use
     * @return a listExercises object
     */
    public static Command listExercises(FrontendListener front, Logic back) {
        return new ListExercises(front, back);
    }

    /**
     * Create a DownloadExercises Command object.
     *
     * @param front frontend that the command will use
     * @param back logic that the command will use
     * @return a downloadExercises object
     */
    public static Command downloadExercises(FrontendListener front, Logic back) {
        return new DownloadExercises(front, back);
    }

    /**
     * Create a Logout Command object.
     *
     * @param front frontend that the command will use
     * @param back logic that the command will use
     * @return a logout object
     */
    public static Command logout(FrontendListener front, Logic back) {
        return new Logout(front, back);
    }

    /**
     * Create a chooseServer Command object.
     *
     * @param front frontend that the command will use
     * @param back logic that the command will use
     * @return a chooseServer object
     */
    public static Command chooseServer(FrontendListener front, Logic back) {
        return new ChooseServer(front, back);
    }
    
    /**
     * Create a Submit Command object.
     *
     * @param front frontend that the command will use
     * @param back logic that the command will use
     * @return a Submit object
     */
    
    public static Command submit(FrontendListener front, Logic back) {
        return new Submit(front, back);
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

        return createCommandMap(stub, null).keySet();
    }

}
