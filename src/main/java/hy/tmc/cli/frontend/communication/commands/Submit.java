package hy.tmc.cli.frontend.communication.commands;

import com.google.common.base.Optional;
import hy.tmc.cli.backend.communication.SubmissionInterpreter;
import hy.tmc.cli.frontend.communication.server.ProtocolException;


import hy.tmc.cli.frontend.formatters.CommandLineSubmissionResultFormatter;
import hy.tmc.cli.frontend.formatters.VimSubmissionResultFormatter;
import hy.tmc.core.domain.Course;
import hy.tmc.core.domain.submission.SubmissionResult;


import java.io.IOException;

/**
 * Submit command for submitting exercises to TMC.
 */
public class Submit extends Command<SubmissionResult> {

    private SubmissionInterpreter interpreter;
    private Course course;
    private MailChecker mail;

    /**
     * Constructor for Submit command, creates the courseSubmitter.
     */
    public Submit() {
        mail = new MailChecker();
    }
    
     /**
     * Constructor for Submit command, creates the courseSubmitter.
     * @param path path which to submit
     */
    public Submit(String path) {
        mail = new MailChecker();
        this.setParameter("path", path);
    }


    private SubmissionInterpreter getInterpreter() {
        if (interpreter != null) {
            return interpreter;
        }
        if (data.containsKey("--vim")) {
            return new SubmissionInterpreter(new VimSubmissionResultFormatter());
        } else {
            return new SubmissionInterpreter(new CommandLineSubmissionResultFormatter());
        }
    }

    @Override
    public Optional<String> parseData(Object data) throws IOException {
        String mail = checkMail();
        return Optional.absent();
        //return Optional.of(mail + "\n" + interpreter.resultSummary(true));
    }

    /**
     ** HUOM EXTRAKTOI TÄMÄ OMAAN LUOKKAAN
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
