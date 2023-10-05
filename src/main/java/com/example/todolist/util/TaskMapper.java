package com.example.todolist.util;

import com.example.todolist.entity.TaskEntity;
import com.example.todolist.model.TaskModel;
import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TaskMapper {
  public static TaskModel toTaskModel(@NonNull TaskEntity entity) {
    return TaskModel.builder().name(entity.getName()).description(entity.getDescription()).build();
  }

  public static List<TaskModel> toTaskModels(@NonNull List<TaskEntity> entities) {
    return entities.stream().map(TaskMapper::toTaskModel).toList();
  }

  public static TaskEntity toTaskEntity(@NonNull TaskModel model) {
    return TaskEntity.builder()
        .id(0)
        .name(model.getName())
        .description(model.getDescription())
        .build();
  }

  public static List<TaskEntity> toTaskEntities(@NonNull List<TaskModel> models) {
    return models.stream().map(TaskMapper::toTaskEntity).toList();
  }
}
