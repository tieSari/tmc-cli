package hy.tmc.cli.listeners;

import com.google.common.base.Optional;
import com.google.common.util.concurrent.ListenableFuture;
import fi.helsinki.cs.tmc.stylerunner.validation.ValidationError;
import fi.helsinki.cs.tmc.stylerunner.validation.ValidationResult;
import hy.tmc.cli.frontend.formatters.CheckstyleFormatter;
import hy.tmc.cli.frontend.formatters.DefaultCheckstyleFormatter;
import java.io.DataOutputStream;
import java.io.File;
import java.net.Socket;
import java.util.List;
import java.util.Map.Entry;


public class CheckstyleListener extends ResultListener<ValidationResult> {

    private CheckstyleFormatter formatter;
    
    public CheckstyleListener(ListenableFuture<ValidationResult> commandResult, DataOutputStream output, Socket socket) {
        super(commandResult, output, socket);
        formatter = new DefaultCheckstyleFormatter();
    }
    
    public CheckstyleListener(ListenableFuture<ValidationResult> commandResult, DataOutputStream output, Socket socket, CheckstyleFormatter formatter) {
        super(commandResult, output, socket);
        this.formatter = formatter;
    }

    
    @Override
    protected Optional<String> parseData(ValidationResult result) {
        if (result.getValidationErrors() == null || result.getValidationErrors().isEmpty()) {
            return Optional.of("");
        }
        StringBuilder builder = new StringBuilder();
        for (Entry<File, List<ValidationError>> entry : result.getValidationErrors().entrySet()) {
            builder.append(this.formatter.checkstyleErrors(entry.getKey().getName(), entry.getValue()));
        }
        
        return Optional.of(builder.toString());
    }

    @Override
    protected void extraActions(ValidationResult result) {
    }

}
