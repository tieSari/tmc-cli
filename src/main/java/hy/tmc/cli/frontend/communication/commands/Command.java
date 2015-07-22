package hy.tmc.cli.frontend.communication.commands;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

public abstract class Command<E> implements Callable<E> {

    protected Map<String, String> data;
    private String defaultErrorMessage = "Unexpected exception.";

    /**
     * Command can return any type of object. For example SubmissionResult etc.
     */
    public Command() {
        data = new HashMap<>();
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

    public void cleanData() {
        data.clear();
    }
}
