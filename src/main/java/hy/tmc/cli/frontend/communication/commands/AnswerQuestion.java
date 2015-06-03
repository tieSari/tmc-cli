package hy.tmc.cli.frontend.communication.commands;

import hy.tmc.cli.frontend.FrontendListener;
import hy.tmc.cli.frontend.communication.server.ProtocolException;
import hy.tmc.cli.frontend.communication.server.Server;

public class AnswerQuestion extends Command {
    public AnswerQuestion(FrontendListener front) {
        super(front);
    }

    @Override
    protected void functionality() {
        ((Server) frontend).feedbackAnswer(data.get("answer"));
    }

    @Override
    public void checkData() throws ProtocolException {
        if (!data.containsKey("answer")) {
            throw new ProtocolException("give question answer");
        }
    }
}
