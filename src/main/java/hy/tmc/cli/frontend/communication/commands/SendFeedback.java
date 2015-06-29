package hy.tmc.cli.frontend.communication.commands;

import com.google.common.base.Optional;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import hy.tmc.cli.backend.communication.HttpResult;
import hy.tmc.cli.backend.communication.UrlCommunicator;
import hy.tmc.cli.frontend.communication.server.ProtocolException;
import java.io.IOException;
import java.net.URI;
import java.util.Map;
import java.util.Map.Entry;


public class SendFeedback extends Command<HttpResult> {

    private Map<String, String> answers;
    private String url;
    
    public SendFeedback(Map<String, String> answers, String url) {
        this.answers = answers;
        this.url = url;
    }
    
    @Override
    public void checkData() throws ProtocolException, IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Optional<String> parseData(Object data) throws IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public HttpResult call() throws Exception {
        JsonArray feedbackAnswers = new JsonArray();
        
        for (Entry<String, String> e : answers.entrySet()) {

            JsonObject jsonAnswer = new JsonObject();
            jsonAnswer.addProperty("question_id", e.getKey());
            jsonAnswer.addProperty("answer", e.getValue());
            feedbackAnswers.add(jsonAnswer);
        }
        // send
        JsonObject req = new JsonObject();
        req.add("answers", feedbackAnswers);

        return UrlCommunicator.makePostWithJson(req, url);
    }

    
}
