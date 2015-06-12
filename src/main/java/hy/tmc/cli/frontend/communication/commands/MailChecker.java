package hy.tmc.cli.frontend.communication.commands;

import static hy.tmc.cli.backend.MailFormatter.format;

import com.google.common.base.Optional;

import hy.tmc.cli.backend.Mailbox;
import hy.tmc.cli.configuration.ClientData;
import hy.tmc.cli.domain.Course;
import hy.tmc.cli.frontend.FrontendListener;
import hy.tmc.cli.frontend.communication.server.ProtocolException;

public class MailChecker extends Command {

    private Mailbox mailbox;
    private Optional<Course> course;

    public MailChecker(FrontendListener front) {
        super(front);
        mailbox = Mailbox.getMailbox();
    }

    @Override
    protected void functionality() {
        if (mailbox.reviewsWaiting()) {
            frontend.printLine(format(mailbox.getUnreadReviews()));
        } else {
            //frontend.printLine("No mail for you :(");
        }
        if (mailbox.updatesWaiting()) {
            frontend.printLine(format(mailbox.getExerciseUpdates(course.get())));
        }
    }

    @Override
    public void checkData() throws ProtocolException {
        mailbox = Mailbox.getMailbox();
        if (mailbox == null) {
            throw new ProtocolException("No mailbox found. Are you logged in?");
        }
        if (!data.containsKey("path")) {
            throw new ProtocolException("must specfiy path");
        }
        course = ClientData.getCurrentCourse(data.get("path"));
        if (!course.isPresent()) {
            String errorMsg = "Unable to determine the course. Are you sure this is a tmc course subdirectory?";
            throw new ProtocolException(errorMsg);
        }
    }

}
