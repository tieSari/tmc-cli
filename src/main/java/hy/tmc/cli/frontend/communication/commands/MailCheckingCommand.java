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

    private void checkMail() throws ProtocolException {
        if (!data.containsKey("path")) {
            throw new ProtocolException("must specify path");
        }
        mail.setParameter("path", data.get("path"));
        mail.execute();
    }

}
