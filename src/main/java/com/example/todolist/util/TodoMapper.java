package com.example.todolist.util;

import com.example.todolist.entity.TaskEntity;
import com.example.todolist.entity.TodoEntity;
import com.example.todolist.model.TodoModel;
import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TodoMapper {

  public static TodoModel toModel(@NonNull TodoEntity entity) {
    return TodoModel.builder()
        .id(entity.getId())
        .name(entity.getName())
        .description(entity.getDescription())
        .tasks(TaskMapper.toTaskModels(entity.getTasks()))
        .build();
  }

  public static List<TodoModel> toModels(@NonNull List<TodoEntity> entities) {
    return entities.stream().map(TodoMapper::toModel).toList();
  }

  public static TodoEntity toEntity(@NonNull TodoModel model) {
    TodoEntity entity =
        TodoEntity.builder()
            .id(model.getId())
            .name(model.getName())
            .description(model.getDescription())
            .build();
    for (TaskEntity taskEntity : TaskMapper.toTaskEntities(model.getTasks())) {
      entity.addTask(taskEntity);
    }
    return entity;
  }
}
