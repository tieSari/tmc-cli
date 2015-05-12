/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hy.tmc.cli.frontend_communication;

/**
 *
 * @author pihla
 */
public interface FrontendListener {
    public void start();
    public void printLine(String line);
}
