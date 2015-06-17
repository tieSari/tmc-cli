package hy.tmc.cli.frontend.communication.commands;

import hy.tmc.cli.frontend.FrontendListener;
import hy.tmc.cli.frontend.communication.server.ProtocolException;

public abstract class MailCheckingCommand extends Command {

    private final MailChecker mail;

    public MailCheckingCommand(FrontendListener front) {
        super(front);
        mail = new MailChecker(front);
    }

    @Override
    public void execute() throws ProtocolException {
        super.execute();
        checkMail();
    }

    /**
     * Executes the mail command with necessary params.
     * Gives the mail command either a courseID (preferably) or a path
     * for determining which courses reviews and updates should be fetched.
     * 
     * @throws ProtocolException if unable to find necessary params.
     */
    private void checkMail() throws ProtocolException {
        if (data.containsKey("courseID")) {
            mail.setParameter("courseID", data.get("courseID"));
        } else if (data.containsKey("path")) {
            mail.setParameter("path", data.get("path"));
        } else {
            throw new ProtocolException("must specify path");
        }
        mail.execute();
    }

}
