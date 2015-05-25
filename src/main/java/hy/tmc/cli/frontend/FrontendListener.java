package hy.tmc.cli.frontend;

public interface FrontendListener {
    /**
     * Starts the Frontend Listener.
     */
    void start();
    /**
     * prints Strings.
     * @param line is printed out
     */
    void printLine(String line);
}
