/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hy.tmc.cli.stubs;

import hy.tmc.cli.frontend_communication.FrontendListener;

/**
 *
 * @author kristianw
 */
public class FrontendStub implements FrontendListener{

    
    
    @Override
    public void start() {
        
    }

    @Override
    public void printLine(String line) {
        System.out.println(line);
    }
    
}
