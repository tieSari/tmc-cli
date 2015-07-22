package hy.tmc.cli.frontend.communication.commands;

import com.google.common.base.Optional;
import hy.tmc.cli.frontend.ProgressObserver;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public abstract class CommandResultParser<E> {

    protected Map<String, String> data;
    private String defaultErrorMessage = "Unexpected exception.";
    protected ProgressObserver observer;

    /**
     * Command can return any type of object. For example SubmissionResult etc.
     */
    public CommandResultParser() {
        data = new HashMap<>();
    }
    
    public CommandResultParser(ProgressObserver observer) {
        this();
        this.observer = observer;
    }

    public Map<String, String> getData() {
        return data;
    }

    /**
     * setParameter sets parameter data for command.
     *
     * @param key name of the datum
     * @param value value of the datum
     */
    public void setParameter(String key, String value) {
        data.put(key, value);
    }

    /**
     * This is only for TMC-cli-client.
     * Command should define, how to format data when result is ready. 
     * Other (G)UI's like TMC-Netbeans define
     * themselves, how to parse ready commandResult.
     *
     * @param data SubmissionResult or String for example
     * @return String to be printed to user
     */
    public abstract Optional<String> parseData(Object data) throws IOException;

    public void cleanData() {
        data.clear();
    }

    /**
     * This is only used in TMC-cli.
     * The observer is used for printing realtime progress information to user.
     * Observer knows the correct socket and is able to write there.
     */
    public void setObserver(ProgressObserver observer) {
        this.observer = observer;
    }
}