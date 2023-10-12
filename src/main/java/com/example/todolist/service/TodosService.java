package com.example.todolist.service;

import com.example.todolist.entity.TaskEntity;
import com.example.todolist.entity.TodoEntity;
import com.example.todolist.model.TaskModel;
import com.example.todolist.model.TodoModel;
import com.example.todolist.model.exceptions.NoSuchTaskFoundException;
import com.example.todolist.model.exceptions.NoSuchTodoFoundException;
import com.example.todolist.model.exceptions.TodoAlreadyExistsException;
import com.example.todolist.repository.TaskRepositoryJPA;
import com.example.todolist.repository.TodoRepositoryJPA;
import com.example.todolist.util.TaskMapper;
import com.example.todolist.util.TodoMapper;
import java.util.List;

import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TodosService {
  @NonNull TodoRepositoryJPA todoRepository;
  @NonNull TaskRepositoryJPA taskRepository;
  @NonNull ValidationService validation;

  public List<TodoModel> getTodos() {
    return TodoMapper.toModels(todoRepository.findAll());
  }

  public TodoModel getTodo(int id) {
    return TodoMapper.toModel(
        todoRepository.findById(id).orElseThrow(() -> new NoSuchTodoFoundException(id)));
  }

  @Transactional
  public TodoModel createTodo(TodoModel newTodo) {
    validation.validateTodo(newTodo);
    if (todoRepository.findByName(newTodo.getName()).isPresent()) {
      throw new TodoAlreadyExistsException(newTodo.getName());
    }
    newTodo.setId(0);
    return TodoMapper.toModel(todoRepository.save(TodoMapper.toEntity(newTodo)));
  }

  @Transactional
  public TodoModel updateTodo(@NonNull TodoModel newTodo) {
    todoRepository
        .findById(newTodo.getId())
        .orElseThrow(() -> new NoSuchTodoFoundException(newTodo.getId()));
    validation.validateTodo(newTodo);
    return TodoMapper.toModel(todoRepository.save(TodoMapper.toEntity(newTodo)));
  }

  @Transactional
  public void deleteTodo(int id) {
    todoRepository.findById(id).orElseThrow(() -> new NoSuchTodoFoundException(id));
    todoRepository.deleteById(id);
  }

  @Transactional
  public TaskModel createTask(@NonNull TaskModel newTask, int id) {
    TodoEntity entity =
        todoRepository.findById(id).orElseThrow(() -> new NoSuchTodoFoundException(id));
    TaskEntity taskEntity = TaskMapper.toTaskEntity(newTask);
    taskEntity.setTodoEntity(entity);
    return TaskMapper.toTaskModel(taskRepository.save(taskEntity));
  }

  @Transactional
  public void deleteTask(String taskName, int id) {
    TodoEntity todoEntity =
        todoRepository.findById(id).orElseThrow(() -> new NoSuchTodoFoundException(id));
    TaskEntity taskEntity = null;
    for (TaskEntity entity : todoEntity.getTasks()) {
      if (entity.getName().equals(taskName)) {
        taskEntity = entity;
        todoEntity.getTasks().remove(taskEntity);
        break;
      }
    }
    if (taskEntity != null) {
      taskRepository.deleteById(taskEntity.getId());
    } else {
      throw new NoSuchTaskFoundException(taskName);
    }
  }
}
