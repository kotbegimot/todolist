package com.example.todolist.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder()
@EqualsAndHashCode
@FieldDefaults(level = AccessLevel.PRIVATE)
@Schema(title = "Task", description = "Todo subtask model information")
public class TaskModel {
    @Schema(title = "Task name", example = "Make salad",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Name must not be null or empty")
    @Size(min = 10, max = 255, message = "Name length must be of 10 - 255 characters")
    String name;
    @Schema(title = "Task description", example = "Rucola and tomatoes",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @EqualsAndHashCode.Exclude
    String description;
}
