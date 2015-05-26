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
<<<<<<< HEAD:src/main/java/hy/tmc/cli/frontend/communication/server/ProtocolException.java
     * ProtocolException can give message.
     *
     * @param message of error
=======
     * ProtocolException can give a message.
     * @param message is a final string.
>>>>>>> 778cd82da093bea3cf04f2cb897d7739f7a723cc:src/main/java/hy/tmc/cli/frontend_communication/Server/ProtocolException.java
     */
    public ProtocolException(final String message) {
        super(message);
    }

    /**
<<<<<<< HEAD:src/main/java/hy/tmc/cli/frontend/communication/server/ProtocolException.java
     * Constructor with message.
     *
     * @param message of error
     * @param cause of error
=======
     * ProtocolException can have cause as parameter.
     * @param message final string message
     * @param cause cause why exception is thrown
>>>>>>> 778cd82da093bea3cf04f2cb897d7739f7a723cc:src/main/java/hy/tmc/cli/frontend_communication/Server/ProtocolException.java
     */
    public ProtocolException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
<<<<<<< HEAD:src/main/java/hy/tmc/cli/frontend/communication/server/ProtocolException.java
     * Constructor for exception.
     *
     * @param cause of error
=======
     * ProtocolException can only have a cause.
     * @param cause why exception is thrown
>>>>>>> 778cd82da093bea3cf04f2cb897d7739f7a723cc:src/main/java/hy/tmc/cli/frontend_communication/Server/ProtocolException.java
     */
    public ProtocolException(final Throwable cause) {
        super(cause);
    }
}
