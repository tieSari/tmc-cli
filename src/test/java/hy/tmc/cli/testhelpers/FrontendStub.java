package hy.tmc.cli.testhelpers;


import hy.tmc.cli.frontend_communication.FrontendListener;

public class FrontendStub implements FrontendListener {
    
    String line;

    @Override
    public void start() {
        
    }

    @Override
    public void printLine(String line) {
        this.line = line;
    }
    
    public String getMostRecentLine(){
        return line;
    }
    
}
