package com.example.todolist.model.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class NoSuchTaskFoundException extends RuntimeException {
    public NoSuchTaskFoundException(String name) {
        super("Task name is not found: " + name);
    }
}
