package com.example.todolist.service;

import com.example.todolist.model.TaskModel;
import com.example.todolist.model.TodoModel;
import com.example.todolist.model.exceptions.InvalidRequestException;
import com.example.todolist.properties.MainProperties;
import java.util.ArrayList;
import java.util.List;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ValidationService {
  @NonNull private final MainProperties properties;
  private final List<String> errors = new ArrayList<>();

  /**
   * Validates todomodel fields
   *
   * @param todoModel - model for checking
   * @throws InvalidRequestException - contains list of errors found in the model
   */
  public void validateTodo(TodoModel todoModel) throws InvalidRequestException {
    errors.clear();
    validateName(todoModel.getName(), "todo");
    for (TaskModel task : todoModel.getTasks()) {
      validateName(task.getName(), "task");
    }
    if (!errors.isEmpty()) throw new InvalidRequestException(errors);
  }

  public List<String> getErrorMessages() {
    return errors;
  }

  /**
   * Validates the field name isn't null and empty
   *
   * @param name - field name
   * @param field - name of the checked filed for an error description
   */
  private void validateName(String name, String field) {
    if (name == null || name.isEmpty()) {
      errors.add(properties.getErrorInvalidName().formatted(field));
    }
  }
}
