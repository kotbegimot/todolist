package com.example.todolist.service;

import com.example.todolist.entity.TodoEntity;
import com.example.todolist.model.TodoModel;
import com.example.todolist.model.exceptions.NoSuchTodoFoundException;
import com.example.todolist.repository.TodoRepositoryJPA;
import com.example.todolist.util.TodoMapper;
import java.util.List;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TodosService {
  @NonNull private final TodoRepositoryJPA repository;
  @NonNull private final ValidationService validation;

  public List<TodoModel> getTodos() {
    return TodoMapper.toModels(repository.findAll());
  }

  public TodoModel getTodo(int id) {
    return TodoMapper.toModel(
        repository.findById(id).orElseThrow(() -> new NoSuchTodoFoundException(id)));
  }

  @Transactional
  public TodoModel createTodo(TodoModel newTodo) {
    validation.validateTodo(newTodo);
    newTodo.setId(0);
    return TodoMapper.toModel(repository.save(TodoMapper.toEntity(newTodo)));
  }

  @Transactional
  public TodoModel updateTodo(TodoModel newTodo) {
    repository
        .findById(newTodo.getId())
        .orElseThrow(() -> new NoSuchTodoFoundException(newTodo.getId()));
    validation.validateTodo(newTodo);
    return TodoMapper.toModel(repository.save(TodoMapper.toEntity(newTodo)));
  }

  @Transactional
  public void deleteTodo(int id) {
    TodoEntity entity = repository.findById(id).orElseThrow(() -> new NoSuchTodoFoundException(id));
    repository.delete(entity);
  }
}
