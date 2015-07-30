package hy.tmc.cli.listeners;

import com.google.common.base.Optional;
import com.google.common.util.concurrent.ListenableFuture;

import hy.tmc.cli.frontend.formatters.CheckstyleFormatter;
import hy.tmc.cli.frontend.formatters.DefaultCheckstyleFormatter;
import hy.tmc.core.domain.submission.ValidationError;
import hy.tmc.core.domain.submission.Validations;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.List;
import java.util.Map.Entry;


public class CheckstyleListener extends ResultListener<Validations> {

    private CheckstyleFormatter formatter;
    
    public CheckstyleListener(ListenableFuture<Validations> commandResult, DataOutputStream output, Socket socket) {
        super(commandResult, output, socket);
        formatter = new DefaultCheckstyleFormatter();
    }
    
    public CheckstyleListener(ListenableFuture<Validations> commandResult, DataOutputStream output, Socket socket, CheckstyleFormatter formatter) {
        super(commandResult, output, socket);
        this.formatter = formatter;
    }

    
    @Override
    protected Optional<String> parseData(Validations result) {
        if (result.getValidationErrors() == null || result.getValidationErrors().isEmpty()) {
            return Optional.of("");
        }
        StringBuilder builder = new StringBuilder();
        for (Entry<String, List<ValidationError>> entry : result.getValidationErrors().entrySet()) {
            builder.append(this.formatter.checkstyleErrors(entry.getKey(), entry.getValue()));
        }
        
        return Optional.of(builder.toString());
    }

    @Override
    protected void extraActions(Validations result) {
    }

}
