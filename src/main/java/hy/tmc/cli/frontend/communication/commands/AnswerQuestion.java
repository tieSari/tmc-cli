package hy.tmc.cli.frontend.communication.commands;

import com.google.common.base.Optional;
import hy.tmc.cli.frontend.FrontendListener;
import hy.tmc.cli.frontend.communication.server.ProtocolException;
import hy.tmc.cli.frontend.communication.server.Server;

public class AnswerQuestion extends Command<String> {


    protected void functionality() {
    }

    @Override
    public void checkData() throws ProtocolException {

    }

    @Override
    public Optional<String> parseData(Object data) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public String call() throws Exception {
        checkData();
        throw new UnsupportedOperationException("Not supported yet."); 
    }
}
