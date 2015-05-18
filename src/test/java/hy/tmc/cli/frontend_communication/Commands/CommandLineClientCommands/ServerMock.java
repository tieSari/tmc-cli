package hy.tmc.cli.frontend_communication.Commands.CommandLineClientCommands;

import hy.tmc.cli.frontend_communication.FrontendListener;

public class ServerMock implements FrontendListener{

    private StringBuilder printedLines;

    public ServerMock() {
        this.printedLines = new StringBuilder();
    }
    
    
    
    @Override
    public void start() {
        
    }

    @Override
    public void printLine(String line) {
        this.printedLines.append(line);
    }

    public String getPrintedLine() {
        return printedLines.toString();
    }
}
