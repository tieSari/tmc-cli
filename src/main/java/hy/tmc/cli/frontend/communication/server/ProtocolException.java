package hy.tmc.cli.frontend.communication.server;

/**
 * ProtocolException is thrown when something goes wrong with protocol rules.
 */
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
     * ProtocolException can give a message.
     * @param message is a final string.
     */
    public ProtocolException(final String message) {
        super(message);
    }

    /**
     * Constructor with message.
     * 
     * ProtocolException can have cause as parameter.
     * @param message final string message
     * @param cause cause why exception is thrown
     */
    public ProtocolException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructor for exception.
     *
     * ProtocolException can only have a cause.
     * @param cause why exception is thrown
     */
    public ProtocolException(final Throwable cause) {
        super(cause);
    }
}
