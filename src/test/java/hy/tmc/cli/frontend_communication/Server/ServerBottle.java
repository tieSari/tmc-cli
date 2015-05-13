/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hy.tmc.cli.frontend_communication.Server;

/**
 *
 * @author ilari
 */
public class ServerBottle implements Runnable {
    
    Server server;
    Thread t;
    
    public ServerBottle(Server server){
        this.server = server;
    }
    
    @Override
    public void run(){
        server.start();
    }
    
    public void start(){
        if (t != null) t.interrupt();
        t = new Thread(this);
        t.start();
    }
    
    public void stop(){
        t.interrupt();
    }
}
