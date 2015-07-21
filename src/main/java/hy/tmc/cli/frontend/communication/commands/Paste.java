package hy.tmc.cli.frontend.communication.commands;

import com.google.common.base.Optional;

import hy.tmc.cli.frontend.communication.server.ProtocolException;
import hy.tmc.core.domain.Course;

import java.io.IOException;
import java.net.URI;

public class Paste extends Command<URI> {

    private Course course;
    private MailChecker mail;

    public Paste() {
        this.mail = new MailChecker();
    }

    public Paste(String path) {
        this.setParameter("path", path);
    }



    @Override
    public Optional<String> parseData(Object data) throws IOException {
        String mail = checkMail();
        URI returnURI = (URI) data;
        return Optional.of(mail + "\n"+"Paste submitted. Here it is: \n  " + returnURI);
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
        try {
            return mail.call();
        } catch (ProtocolException e) {
            return e.getMessage();
        }
    }
}
