package com.example.todolist.controller;

import com.example.todolist.model.ErrorResponseModel;
import com.example.todolist.model.exceptions.InvalidRequestException;
import com.example.todolist.model.exceptions.NoSuchTaskFoundException;
import com.example.todolist.model.exceptions.NoSuchTodoFoundException;
import com.example.todolist.model.exceptions.TodoAlreadyExistsException;
import com.example.todolist.properties.MainProperties;
import jakarta.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;
import jakarta.validation.ConstraintViolationException;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RestResponseEntityExceptionHandler {
    @NonNull MainProperties properties;

    /**
     * Custom exception, returns 404 if the todos doesn't exist.
     */
    @ExceptionHandler(NoSuchTodoFoundException.class)
    public ResponseEntity<ErrorResponseModel> handleNoSuchTodoFoundException(
            @NonNull NoSuchTodoFoundException exception, @NonNull HttpServletRequest request) {
        String timeStamp = new SimpleDateFormat().format(new java.util.Date());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(
                        ErrorResponseModel.builder()
                                .status(HttpStatus.NOT_FOUND.value())
                                .error(HttpStatus.NOT_FOUND.getReasonPhrase())
                                .messages(List.of((exception.getMessage())))
                                .path(request.getRequestURL().toString())
                                .timestamp(timeStamp)
                                .build());
    }

    /**
     * Custom exception, returns 404 if the todos doesn't exist.
     */
    @ExceptionHandler(NoSuchTaskFoundException.class)
    public ResponseEntity<ErrorResponseModel> handleNoSuchTaskFoundException(
            @NonNull NoSuchTaskFoundException exception, @NonNull HttpServletRequest request) {
        String timeStamp = new SimpleDateFormat().format(new java.util.Date());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(
                        ErrorResponseModel.builder()
                                .status(HttpStatus.NOT_FOUND.value())
                                .error(HttpStatus.NOT_FOUND.getReasonPhrase())
                                .messages(List.of((exception.getMessage())))
                                .path(request.getRequestURL().toString())
                                .timestamp(timeStamp)
                                .build());
    }

    /**
     * Custom exception is thrown by annotations, returns 400 if POST/PUT request body contains
     * invalid values.
     */
    @ExceptionHandler(InvalidRequestException.class)
    public ResponseEntity<ErrorResponseModel> handleInvalidRequestException(
            @NonNull InvalidRequestException exception, @NonNull HttpServletRequest request) {
        String timeStamp =
                new SimpleDateFormat(properties.getExceptionDateFormat()).format(new java.util.Date());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(
                        ErrorResponseModel.builder()
                                .status(HttpStatus.BAD_REQUEST.value())
                                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                                .messages(exception.getErrorsList())
                                .path(request.getRequestURL().toString())
                                .timestamp(timeStamp)
                                .build());
    }

    /**
     * Custom exception is thrown by validation service, returns 400 if POST/PUT request body contains
     * invalid values.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseModel> handleValidationExceptions(
            @NonNull MethodArgumentNotValidException ex, @NonNull HttpServletRequest request) {
        List<String> errors = new ArrayList<>();
        ex.getBindingResult()
                .getAllErrors()
                .forEach(
                        (error) -> {
                            FieldError err = ((FieldError) error);
                            String errorStr =
                                    "Field error in object '%s' on field '%s': rejected value [%s]; error: %s"
                                            .formatted(
                                                    err.getObjectName(),
                                                    err.getField(),
                                                    ObjectUtils.nullSafeToString((err).getRejectedValue()),
                                                    err.getDefaultMessage());
                            errors.add(errorStr);
                        });
        String timeStamp =
                new SimpleDateFormat(properties.getExceptionDateFormat()).format(new java.util.Date());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(
                        ErrorResponseModel.builder()
                                .status(HttpStatus.BAD_REQUEST.value())
                                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                                .messages(errors)
                                .path(request.getRequestURL().toString())
                                .timestamp(timeStamp)
                                .build());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    ResponseEntity<ErrorResponseModel> handleOnConstraintValidationException(
            @NonNull ConstraintViolationException ex, @NonNull HttpServletRequest request) {
        List<String> errors = new ArrayList<>();
        ex.getConstraintViolations()
                .forEach(
                        (violation) -> {
                            String errorStr =
                                    "Field error in object '%s': rejected value [%s]; error: %s"
                                            .formatted(
                                                    violation.getPropertyPath().toString(),
                                                    violation.getInvalidValue().toString(),
                                                    violation.getMessage());
                            errors.add(errorStr);
                        });
        String timeStamp =
                new SimpleDateFormat(properties.getExceptionDateFormat()).format(new java.util.Date());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(
                        ErrorResponseModel.builder()
                                .status(HttpStatus.BAD_REQUEST.value())
                                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                                .messages(errors)
                                .path(request.getRequestURL().toString())
                                .timestamp(timeStamp)
                                .build());

    }

    /**
     * Custom exception, returns 409 if the created todos already exists.
     */
    @ExceptionHandler(TodoAlreadyExistsException.class)
    public ResponseEntity<ErrorResponseModel> handleUserAlreadyExistsException(
            @NonNull TodoAlreadyExistsException exception, @NonNull HttpServletRequest request) {
        String timeStamp =
                new SimpleDateFormat(properties.getExceptionDateFormat()).format(new java.util.Date());
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(
                        ErrorResponseModel.builder()
                                .status(HttpStatus.CONFLICT.value())
                                .error(HttpStatus.CONFLICT.getReasonPhrase())
                                .messages(List.of((exception.getMessage())))
                                .path(request.getRequestURL().toString())
                                .timestamp(timeStamp)
                                .build());
    }

    /**
     * Custom exception, returns 500 if the unchecked exception is caught
     */
    @ExceptionHandler(RuntimeException.class)
    public final @NonNull ResponseEntity<ErrorResponseModel> handleRuntimeExceptions(
            @NonNull RuntimeException exception, @NonNull HttpServletRequest request) {
        String timeStamp =
                new SimpleDateFormat(properties.getExceptionDateFormat()).format(new java.util.Date());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(
                        ErrorResponseModel.builder()
                                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                                .error(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                                .messages(List.of((exception.getMessage())))
                                .path(request.getRequestURL().toString())
                                .timestamp(timeStamp)
                                .build());
    }
}
