package com.example.todolist.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
    @NotBlank(message = "Name must not be null or empty")
    @Size(min = 10, max = 255, message = "Name length must be of 10 - 255 characters")
    private String name;

    @Schema(title = "Todo description", example = "Pasta with cevapcici",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @NotBlank(message = "Description must not be null or empty")
    @Size(min = 10, max = 255, message = "Description length must be of 10 - 255 characters")
    private String description;

    @Schema(title = "List of todo subtasks", example = "See the task description above",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @Valid
    private List<TaskModel> tasks;
}
