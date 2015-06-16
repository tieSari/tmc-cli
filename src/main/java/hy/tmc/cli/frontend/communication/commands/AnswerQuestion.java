package hy.tmc.cli.frontend.communication.commands;

import com.google.common.base.Optional;
import hy.tmc.cli.frontend.FrontendListener;
import hy.tmc.cli.frontend.communication.server.ProtocolException;
import hy.tmc.cli.frontend.communication.server.Server;

public class AnswerQuestion extends Command<String> {


    protected void functionality() {
       // Server server = (Server) frontend;
        if (data.get("kind").equals("text")) {
         //   server.textFeedbackAnswer(data.get("answer"));
        } else {
           // server.rangeFeedbackAnswer(data.get("answer"));
        }
    }

    @Override
    public void checkData() throws ProtocolException {
        if (!data.containsKey("answer")) {
            throw new ProtocolException("give question answer");
        }

        if (!data.containsKey("kind")) {
            throw new ProtocolException("type of answer must be specified");
        }
    }

    @Override
    public Optional<String> parseData(Object data) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String call() throws Exception {
        checkData();
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
