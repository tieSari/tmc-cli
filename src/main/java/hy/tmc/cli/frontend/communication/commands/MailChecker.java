package hy.tmc.cli.frontend.communication.commands;

import hy.tmc.cli.TmcCli;
import hy.tmc.cli.mail.MailFormatter;
import com.google.common.base.Optional;
import hy.tmc.cli.CliSettings;
import hy.tmc.cli.TmcCli;
import hy.tmc.cli.frontend.communication.server.ProtocolException;
import hy.tmc.cli.mail.Mailbox;
import hy.tmc.core.domain.Course;
import java.io.IOException;

public class MailChecker extends Command<String> {

    private Mailbox mailbox;
    private Course course;
    private CliSettings settings;

    public MailChecker(TmcCli cli, CliSettings settings) {
        super(cli);
    }

    public void checkData() throws ProtocolException, IOException {
        if (!settings.userDataExists()) {
            throw new ProtocolException("Must be logged in first");
        }
        checkMailbox();
        checkCourse();
    }

    private void checkMailbox() throws ProtocolException {
        Optional<Mailbox> mailbox = Mailbox.getMailbox();
        if (mailbox.isPresent()) {
            this.mailbox = mailbox.get();
        } else {
            throw new ProtocolException("No mailbox found. Are you logged in?");
        }
    }

    private void checkCourse() throws ProtocolException {
        Optional<Course> course = settings.getCurrentCourse();
        if (course.isPresent()) {
            this.course = course.get();
        } else {
            String errorMsg = "Unable to determine the course. Are you sure this is a tmc course subdirectory?";
            throw new ProtocolException(errorMsg);
        }
    }

    @Override
    public String call() throws ProtocolException, IOException {
        checkData();

        String mail = "";
        if (mailbox.reviewsWaiting()) {
            mail += MailFormatter.formatReviews(mailbox.getUnreadReviews());
        }

        if (mailbox.updatesWaiting()) {
            mail += "\n" + MailFormatter.formatUpdates(mailbox.getExerciseUpdates(course));
        }
        return mail;
    }
}
