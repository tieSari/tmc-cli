package hy.tmc.cli.testhelpers;

import hy.tmc.cli.frontend.FrontendListener;


@Deprecated
public class FrontendStub implements FrontendListener {
    
    String line;

    @Override
    public void start() {
        
    }
    public String getMostRecentLine() {
        return line;
    }
    
}
