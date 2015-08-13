package hy.tmc.cli.frontend.formatters;

import fi.helsinki.cs.tmc.core.domain.submission.ValidationError;

import java.util.List;

import static hy.tmc.cli.frontend.ColorFormatter.coloredString;
import static hy.tmc.cli.frontend.CommandLineColor.YELLOW;

public class DefaultCheckstyleFormatter implements CheckstyleFormatter {

    @Override
    public String checkstyleErrors(String file, List<ValidationError> errors) {
        StringBuilder builder = new StringBuilder();
        builder.append("\nFile: ").append(file);
        for (ValidationError error : errors) {
            String errorLine = "\n  On line: " + error.getLine() + " Column: " + error.getColumn();
            builder.append(coloredString(errorLine, YELLOW));
            builder.append("\n    ").append(error.getMessage());
        }
        return builder.toString();
    }

}
