package com.example.todolist.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@EqualsAndHashCode
@Schema(title = "Todo", description = "Todo model information")
public class TodoModel {
    @Schema(title = "Todo id", example = "1",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private int id;
    @Schema(title = "Todo name", example = "Make lunch",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;
    @Schema(title = "Todo description", example = "Pasta with cevapcici",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String description;
    @Schema(title = "List of todo subtasks", example = "See the task description above",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private List<TaskModel> tasks;
}
