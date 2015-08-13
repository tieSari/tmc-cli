package hy.tmc.cli.testhelpers.builders;

import fi.helsinki.cs.tmc.core.domain.submission.ValidationError;
import fi.helsinki.cs.tmc.core.domain.submission.Validations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ValidationsBuilder {
    private HashMap<String, List<ValidationError>> map;
    private String strategy;

    public ValidationsBuilder() {
        map = new HashMap<>();
    }

    public ValidationsBuilder withStrategy(String strategy) {
        this.strategy = strategy;
        return this;
    }

    public ValidationsBuilder withValidationError(String file, int column, int line, String msg,
        String source) {
        ValidationError err = new ValidationError();
        List<ValidationError> errors = map.get(file);
        if (!map.containsKey(file)) {
            errors = new ArrayList<>();
        }
        err.setColumn(column);
        err.setLine(line);
        err.setMessage(msg);
        err.setSourceName(source);
        errors.add(err);
        map.put(file, errors);
        return this;
    }

    public Validations build() {
        Validations validations = new Validations();
        validations.setStrategy(strategy);
        validations.setValidationErrors(map);
        map = new HashMap<>();
        strategy = "";
        return validations;
    }
}
