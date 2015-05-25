package hy.tmc.cli.frontend.communication.server;

public class ProtocolException extends Exception {

    /**
     * ProtocolException is thrown when system gets invalid protocol.
     */
    public ProtocolException() {
        super();
    }

    /**
     * ProtocolException can give message.
     *
     * @param message of error
     */
    public ProtocolException(String message) {
        super(message);
    }

    /**
     * Constructor with message.
     *
     * @param message of error
     * @param cause of error
     */
    public ProtocolException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructor for exception.
     *
     * @param cause of error
     */
    public ProtocolException(Throwable cause) {
        super(cause);
    }
}
