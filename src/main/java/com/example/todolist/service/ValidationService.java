package com.example.todolist.service;

import com.example.todolist.model.TodoModel;
import com.example.todolist.model.exceptions.InvalidRequestException;
import com.example.todolist.properties.MainProperties;
import java.util.ArrayList;
import java.util.List;

import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ValidationService {
  @NonNull MainProperties properties;
  List<String> errors = new ArrayList<>();

  /**
   * Validates todomodel fields
   *
   * @param todoModel - model for checking
   * @throws InvalidRequestException - contains list of errors found in the model
   */
  public void validateTodo(TodoModel todoModel) throws InvalidRequestException {
    errors.clear();
    if (!errors.isEmpty()) throw new InvalidRequestException(errors);
  }

  public List<String> getErrorMessages() {
    return errors;
  }
}
