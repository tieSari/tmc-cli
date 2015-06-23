package hy.tmc.cli.frontend.communication.commands;

import static hy.tmc.cli.backend.MailFormatter.formatExercises;
import static hy.tmc.cli.backend.MailFormatter.formatReviews;

import com.google.common.base.Optional;

import hy.tmc.cli.backend.Mailbox;
import hy.tmc.cli.backend.communication.TmcJsonParser;
import hy.tmc.cli.configuration.ClientData;
import hy.tmc.cli.domain.Course;
import hy.tmc.cli.domain.submission.SubmissionResult;
import hy.tmc.cli.frontend.FrontendListener;
import hy.tmc.cli.frontend.communication.server.ProtocolException;

public class MailChecker extends Command<String> {

    private Optional<Mailbox> mailbox;
    private Optional<Course> course;

    public MailChecker() {
        mailbox = Mailbox.getMailbox();
    }

    @Override
    public void checkData() throws ProtocolException {
        if (!ClientData.userDataExists()) {
            throw new ProtocolException("Must be logged in first");
        }
        mailbox = Mailbox.getMailbox();
        if (!mailbox.isPresent()) {
            throw new ProtocolException("No mailbox found. Are you logged in?");
        }
        if (data.containsKey("courseID")) {
            course = TmcJsonParser.getCourse(Integer.parseInt(data.get("courseID")));
        } else if (data.containsKey("path")) {
            String path = data.get("path");
            course = ClientData.getCurrentCourse(path);
        } else {
            throw new ProtocolException("must specify path or courseID");
        }
        if (!course.isPresent()) {
            String errorMsg = "Unable to determine the course. Are you sure this is a tmc course subdirectory?";
            throw new ProtocolException(errorMsg);
        }
    }

    @Override
    public Optional<String> parseData(Object data) {
        return Optional.of((String) data);
    }

    @Override
    public String call() throws ProtocolException {
        checkData();
        String mail = "";
        if (mailbox.get().reviewsWaiting()) {
            mail += formatReviews(mailbox.get().getUnreadReviews());
        } else {
            mail += "No mail for you :(";
        }
        if (mailbox.get().updatesWaiting()) {
            mail += formatExercises(mailbox.get().getExerciseUpdates(course.get()));
        }
        return mail;
    }
}
