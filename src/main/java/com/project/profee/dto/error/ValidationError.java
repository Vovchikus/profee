package com.project.profee.dto.error;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ValidationError {

    private final List<FieldError> fieldErrors = new ArrayList<>();

    public void addFieldError(String path, String message) {
        FieldError error = new FieldError(path, message);
        fieldErrors.add(error);
    }

    public List<FieldError> getFieldErrors() {
        return Collections.unmodifiableList(fieldErrors);
    }
}
