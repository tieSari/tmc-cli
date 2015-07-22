package hy.tmc.cli.frontend.communication.commands;

import static hy.tmc.cli.mail.MailFormatter.formatReviews;

import com.google.common.base.Optional;
import hy.tmc.cli.CliSettings;
import hy.tmc.cli.mail.Mailbox;
import hy.tmc.cli.configuration.ClientData;

import hy.tmc.cli.frontend.communication.server.ProtocolException;
import hy.tmc.cli.mail.Mailbox;
import hy.tmc.core.domain.Course;
import java.io.IOException;

public class MailChecker extends Command<String> {

    private Optional<Mailbox> mailbox;
    private Optional<Course> course;
    private CliSettings settings;

    public MailChecker(CliSettings settings) {
        mailbox = Mailbox.getMailbox();
    }

    public void checkData() throws ProtocolException, IOException {
        if (!settings.userDataExists()) {
            throw new ProtocolException("Must be logged in first");
        }
        mailbox = Mailbox.getMailbox();
        if (!mailbox.isPresent()) {
            throw new ProtocolException("No mailbox found. Are you logged in?");
        }
        if (data.containsKey("courseID")) {
            course = settings.getCurrentCourse();
        } else if (data.containsKey("path")) {
            String path = data.get("path");
            course = settings.getCurrentCourse();
        } else {
            throw new ProtocolException("must specify path or courseID");
        }
        if (!course.isPresent()) {
            String errorMsg = "Unable to determine the course. Are you sure this is a tmc course subdirectory?";
            throw new ProtocolException(errorMsg);
        }
    }

    public Optional<String> parseData(Object data) {
        return Optional.of((String) data);
    }

    public String call() throws ProtocolException, IOException {
        checkData();
        String mail = "";
        if (mailbox.get().reviewsWaiting()) {
            mail += formatReviews(mailbox.get().getUnreadReviews());
        }
        return mail;
    }
}
