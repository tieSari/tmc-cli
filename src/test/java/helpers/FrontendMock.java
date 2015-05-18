/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package helpers;

import hy.tmc.cli.frontend_communication.FrontendListener;

/**
 *
 * @author ilari
 */
public class FrontendMock implements FrontendListener {
    
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
