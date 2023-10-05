package com.example.todolist.model.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class NoSuchTodoFoundException extends RuntimeException {
    public NoSuchTodoFoundException(int id) {
        super("Todo ID is not found: " + id);
    }
}
