/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hy.tmc.cli.frontend_communication.Server;

/**
 *
 * @author pihla
 */
public class ProtocolException extends Exception {

    /**
     * ProtocolException is thrown when system gets invalid protocol 
     */
    public ProtocolException() {
        super();
    }

    /**
     * ProtocolException can give message
     * @param message
     */
    public ProtocolException(String message) {
        super(message);
    }

    /**
     *
     * @param message
     * @param cause
     */
    public ProtocolException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     *
     * @param cause
     */
    public ProtocolException(Throwable cause) {
        super(cause);
    }
}
