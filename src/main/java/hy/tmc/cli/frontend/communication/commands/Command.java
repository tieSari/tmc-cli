package hy.tmc.cli.frontend.communication.commands;

import com.google.common.base.Optional;
import hy.tmc.cli.frontend.communication.server.ProtocolException;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

public abstract class Command<E> implements Callable<E> {

    private String defaultErrorMessage = "Unexpected exception.";

    Map<String, String> data;
    private Class returnType;

    /**
     * Command can return any type of object. For example SubmissionResult etc.
     */
    public Command() {
        data = new HashMap<String, String>();
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
     * Command must have checkData method which throws ProtocolException if it doesn't have all data
     * needed.
     *
     * @throws ProtocolException if the command lacks some necessary data
     */
    public abstract void checkData() throws ProtocolException;
    
    /**
     * Command should define, how to format data when result is ready. 
     * This is ONLY for TMC-cli-client. Other (G)UI's like TMC-Netbeans 
     * define themselves, how to parse ready commandResult.
     * 
     * @param data SubmissionResult or String for example
     * @return String to be printed to user
     */
    public abstract Optional<String> parseData(Object data);

    private void cleanData() {
        data.clear();
    }
}
