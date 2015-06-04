package hy.tmc.cli.testhelpers;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import hy.tmc.cli.frontend.FeedbackHandler;
import hy.tmc.cli.frontend.communication.server.Server;
import java.io.IOException;


public class ServerMock extends Server {

    String lastLine;
    String lastJson;
    
    public ServerMock(FeedbackHandler handler) throws IOException {
        super(handler);
    }
    
    @Override
    public void printLine(String line) {
        this.lastLine = line;
    }
    
    @Override
    protected void sendToTmcServer(JsonArray json) {
        JsonObject req = new JsonObject();
        req.add("answers", json);
        lastJson = req.toString();
    }

    public String getLastLine() {
        return lastLine;
    }

    public String getLastJson() {
        return lastJson;
    }
    
    
    
}
