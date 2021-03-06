package hy.tmc.cli.frontend.formatters;

import fi.helsinki.cs.tmc.core.domain.submission.ValidationError;

import java.util.List;

public interface CheckstyleFormatter {

    String checkstyleErrors(String file, List<ValidationError> errors);
}
