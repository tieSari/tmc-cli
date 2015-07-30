package hy.tmc.cli.frontend.formatters;

import hy.tmc.core.domain.submission.ValidationError;
import java.util.List;


public class VimCheckstyleFormatter implements CheckstyleFormatter {

    @Override
    public String checkstyleErrors(String file, List<ValidationError> errors) {
        StringBuilder builder = new StringBuilder();
        builder.append("\nFile: ").append(file);
        for (ValidationError error : errors) {
            String errorLine = "\n  On line: " + error.getLine() + " Column: " + error.getColumn();
            builder.append(errorLine);
            builder.append("\n    ").append(error.getMessage());
        }
        return builder.toString();
    }

}
