package com.example.todolist.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder()
@EqualsAndHashCode
@Schema(title = "Task", description = "Todo subtask model information")
public class TaskModel {
    @Schema(title = "Task name", example = "Make salad",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;
    @Schema(title = "Task description", example = "Rucola and tomatoes",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @EqualsAndHashCode.Exclude
    private String description;
}
