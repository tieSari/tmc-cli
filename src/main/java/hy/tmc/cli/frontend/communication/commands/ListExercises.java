package hy.tmc.cli.frontend.communication.commands;

import com.google.common.base.Optional;
import hy.tmc.cli.backend.communication.ExerciseLister;
import hy.tmc.core.domain.Course;
import hy.tmc.core.domain.Exercise;
import java.io.IOException;

import java.util.List;

public class ListExercises extends Command<List<Exercise>> {

    private ExerciseLister lister;
    private Course current;
    private MailChecker mail;

    public ListExercises() {
        this(new ExerciseLister());
    }

    /**
     * For dependency injection for tests.
     *
     * @param lister mocked lister object.
     */
    public ListExercises(ExerciseLister lister) {
        //mail = new MailChecker();
        this.lister = lister;
    }

    public ListExercises(String path) {
        this(new ExerciseLister());
        this.setParameter("path", path);
    }

    @Override
    public Optional<String> parseData(Object data) throws IOException {
        String mail = checkMail();
        @SuppressWarnings("unchecked")
        List<Exercise> result = (List<Exercise>) data;
        return Optional.of(mail + "\n" + lister.buildExercisesInfo(result));
    }

    /**
     * HUOM EXTRAKTOI TÄMÄ OMAAN LUOKKAAN
     * Executes the mail command with necessary params.
     * Gives the mail command either a courseID (preferably) or a path
     * for determining which courses reviews and updates should be fetched.
     *
     * @throws ProtocolException if unable to find necessary params.
     */
    private String checkMail() throws IOException {
        if (data.containsKey("courseID")) {
            mail.setParameter("courseID", data.get("courseID"));
        } else if (data.containsKey("path")) {
            mail.setParameter("path", data.get("path"));
        } else {
            return "must specify path";
        }
        // TODO: read mail
        return "";
    }
}
