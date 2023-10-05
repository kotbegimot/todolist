package com.example.todolist.controller;

import com.example.todolist.model.ErrorResponseModel;
import com.example.todolist.model.exceptions.InvalidRequestException;
import com.example.todolist.model.exceptions.NoSuchTodoFoundException;
import com.example.todolist.model.exceptions.TodoAlreadyExistsException;
import com.example.todolist.properties.MainProperties;
import jakarta.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.List;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@RequiredArgsConstructor
public class RestResponseEntityExceptionHandler {
  @NonNull private final MainProperties properties;

    /**
     * Custom exception, returns 404 if the todos doesn't exist.
     */
    @ExceptionHandler(NoSuchTodoFoundException.class)
    public ResponseEntity<ErrorResponseModel> handleNoSuchTodoFoundException(
            NoSuchTodoFoundException exception, HttpServletRequest request) {
        String timeStamp = new SimpleDateFormat().format(new java.util.Date());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ErrorResponseModel.builder()
                        .status(HttpStatus.NOT_FOUND.value())
                        .error(HttpStatus.NOT_FOUND.getReasonPhrase())
                        .messages(List.of((exception.getMessage())))
                        .path(request.getRequestURL().toString())
                        .timestamp(timeStamp)
                        .build());
    }

    /**
     * Custom exception, returns 400 if POST/PUT request body contains invalid values.
     */
    @ExceptionHandler(InvalidRequestException.class)
    public ResponseEntity<ErrorResponseModel> handleInvalidRequestException(
            InvalidRequestException exception, HttpServletRequest request) {
        String timeStamp = new SimpleDateFormat(properties.getExceptionDateFormat()).format(new java.util.Date());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponseModel.builder()
                        .status(HttpStatus.BAD_REQUEST.value())
                        .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                        .messages(exception.getErrorsMap())
                        .path(request.getRequestURL().toString())
                        .timestamp(timeStamp)
                        .build());
    }

    /**
     * Custom exception, returns 409 if the created todos already exists.
     */
    @ExceptionHandler(TodoAlreadyExistsException.class)
    public ResponseEntity<ErrorResponseModel> handleUserAlreadyExistsException(
            TodoAlreadyExistsException exception, HttpServletRequest request) {
        String timeStamp = new SimpleDateFormat(properties.getExceptionDateFormat()).format(new java.util.Date());
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(ErrorResponseModel.builder()
                        .status(HttpStatus.CONFLICT.value())
                        .error(HttpStatus.CONFLICT.getReasonPhrase())
                        .messages(List.of((exception.getMessage())))
                        .path(request.getRequestURL().toString())
                        .timestamp(timeStamp)
                        .build());
    }
}
