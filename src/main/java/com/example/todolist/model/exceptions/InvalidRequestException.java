package com.example.todolist.model.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class InvalidRequestException extends RuntimeException {
    private final List<String> errors;

    public InvalidRequestException(List<String> cause) {
        super("Invalid request: " + cause.toString());
        errors = cause;
    }

    public List<String> getErrorsMap() {
        return errors;
    }
}
