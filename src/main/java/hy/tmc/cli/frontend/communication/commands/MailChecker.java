package hy.tmc.cli.frontend.communication.commands;

import static hy.tmc.cli.backend.MailFormatter.formatReviews;

import com.google.common.base.Optional;

import hy.tmc.cli.backend.Mailbox;
import hy.tmc.cli.backend.communication.TmcJsonParser;
import hy.tmc.cli.configuration.ClientData;
import hy.tmc.cli.domain.Course;
import hy.tmc.cli.frontend.communication.server.ProtocolException;
import java.io.IOException;

public class MailChecker extends Command<String> {

    private Optional<Mailbox> mailbox;
    private Optional<Course> course;

    public MailChecker() {
        mailbox = Mailbox.getMailbox();
    }

    @Override
    public void checkData() throws ProtocolException, IOException {
        checkUserData();
        setMailbox();
        if (data.containsKey("courseID")) {
            setCourse();
        } else if (data.containsKey("path")) {
            String path = data.get("path");
            course = ClientData.getCurrentCourse(path);
        } else {
            throw new ProtocolException("must specify path or courseID");
        }
        checkCourseToPresent();
    }

    private void setMailbox() throws ProtocolException {
        mailbox = Mailbox.getMailbox();
        if (!mailbox.isPresent()) {
            throw new ProtocolException("No mailbox found. Are you logged in?");
        }
    }
    
    private void checkUserData() throws ProtocolException{
        if (!ClientData.userDataExists()) {
            throw new ProtocolException("Must be logged in first");
        }
    }

    private void setCourse() throws IOException, ProtocolException {
        try {
            course = TmcJsonParser.getCourse(Integer.parseInt(data.get("courseID")));
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
    }
    
    private void checkCourseToPresent() throws ProtocolException {
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
    public String call() throws ProtocolException, IOException {
        checkData();
        String mail = "";
        if (mailbox.get().hasNewReviews()) {
            mail += formatReviews(mailbox.get().getUnreadReviews());
        }
        return mail;
    }
}
