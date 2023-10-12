package com.example.todolist.model.exceptions;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class InvalidRequestException extends RuntimeException {
    List<String> errors;

    public InvalidRequestException(List<String> cause) {
        super("Invalid request: " + cause.toString());
        errors = cause;
    }

    public List<String> getErrorsList() {
        return errors;
    }
}
