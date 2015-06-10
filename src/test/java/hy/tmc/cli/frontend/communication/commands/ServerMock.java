package hy.tmc.cli.frontend.communication.commands;

import hy.tmc.cli.frontend.FrontendListener;

@Deprecated
public class ServerMock implements FrontendListener{

    private StringBuilder printedLines;

    public ServerMock() {
        this.printedLines = new StringBuilder();
    }
    
    @Override
    public void start() {
        
    }
    public String getPrintedLine() {
        return printedLines.toString();
    }
}
