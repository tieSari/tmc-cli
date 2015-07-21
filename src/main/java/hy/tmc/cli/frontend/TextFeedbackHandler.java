package hy.tmc.cli.frontend;

import hy.tmc.cli.frontend.communication.server.Server;


public class TextFeedbackHandler extends FeedbackHandlerAbstract {

    public TextFeedbackHandler(Server server) {
        super(server);
    }

    @Override
    protected String instructions(String kind) {
        return "text";
    }

}
