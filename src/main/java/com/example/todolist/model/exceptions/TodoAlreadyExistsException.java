package com.example.todolist.model.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class TodoAlreadyExistsException extends RuntimeException {
    public TodoAlreadyExistsException(String name) {
        super("Todo with this name already exists: " + name);
    }
}
