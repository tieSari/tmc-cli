/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hy.tmc.cli.frontend_communication.commands;

import hy.tmc.cli.frontend.FrontendListener;

/**
 *
 * @author samutamm
 */
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
