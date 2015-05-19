/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hy.tmc.cli.frontend_communication;

public enum Result {

    SUCCESS,

    ERROR,

    RESULT_DATA;
    
    private String data;
    
    /**
     *
     * @return
     */
    public String getData(){
        return this.data;
    }
    
    /**
     *
     */
    public void setData(){
        
    }
}
