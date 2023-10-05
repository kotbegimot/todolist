package com.example.todolist.controller;

import com.example.todolist.model.Catalogue;
import com.example.todolist.model.TaskModel;
import com.example.todolist.model.TodoModel;
import com.example.todolist.service.TodosService;
import com.example.todolist.util.TaskMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping(value = "/api/v1/todos")
@RequiredArgsConstructor
public class TodoController {
  @NonNull private final TodosService service;

  /**
   * GET request for getting a list of all the todos
   *
   * @return - list of todos objects in JSON format
   */
  @Operation(summary = "Return all todos", description = "Return all the persisted todos")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Todo list is successfully returned")
      })
  @GetMapping()
  public Catalogue getTodos() {
    return new Catalogue(service.getTodos());
  }

  /**
   * GET request that returns todos by id.
   *
   * @param id - id number
   * @return todos object
   */
  @Operation(
      summary = "Return todo by id",
      description = "Fetches todo and it's content by provided id")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Todo is found and returned"),
        @ApiResponse(responseCode = "404", description = "Todo with this id already exists")
      })
  @GetMapping("/{id}")
  public TodoModel getTodo(
      @PathVariable @Parameter(name = "id", description = "id of the task", example = "1") int id) {
    return service.getTodo(id);
  }

  /**
   * Creates new todos entity
   *
   * @param newTodo - new todos object
   */
  @Operation(
      summary = "Creates new todo",
      description = "Creates new todo and returns JSON containing created todo and it's ID")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "201", description = "Todo is successfully created"),
        @ApiResponse(responseCode = "400", description = "Todo body is invalid"),
        @ApiResponse(responseCode = "409", description = "Todo with this id already exists")
      })
  @PostMapping()
  @ResponseStatus(value = HttpStatus.CREATED)
  public TodoModel createTodo(@RequestBody TodoModel newTodo) {
    return service.createTodo(newTodo);
  }

  /**
   * Update existing todos
   *
   * @param id - id number
   * @param editTodo - todos object for updating
   * @return - updated todos object
   */
  @Operation(summary = "Updates todo", description = "Updates todo by ID")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Todo is successfully updated"),
        @ApiResponse(responseCode = "400", description = "Todo body is invalid"),
        @ApiResponse(responseCode = "404", description = "Todo with this id doesn't exist")
      })
  @PutMapping("/{id}")
  public TodoModel updateTodo(
      @PathVariable @Parameter(name = "id", description = "id of the task", example = "1") int id,
      @RequestBody TodoModel editTodo) {
    editTodo.setId(id);
    return service.updateTodo(editTodo);
  }

  /**
   * DELETE request that removes todos by id.
   *
   * @param id - id value
   */
  @Operation(summary = "Deletes todo", description = "Deletes todo by ID")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Todo is successfully deleted"),
        @ApiResponse(responseCode = "404", description = "Todo with this id doesn't exist")
      })
  @DeleteMapping("/{id}")
  @ResponseStatus(value = HttpStatus.OK)
  public void deleteTodo(
      @PathVariable @Parameter(name = "id", description = "id of the task", example = "1") int id) {
    service.deleteTodo(id);
  }

  /**
   * POST request that creates a new task for todos by its id. Creates a new task entity
   *
   * @param newTask - new task object
   * @param id - todos object id
   * @return - created task
   */
  @Operation(summary = "Creates task in todo", description = "Creates task in todo by ID")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Task is successfully created"),
        @ApiResponse(responseCode = "404", description = "Todo with this id doesn't exist")
      })
  @PostMapping("/{id}/tasks")
  @ResponseStatus(value = HttpStatus.CREATED)
  public TaskModel createTask(@RequestBody TaskModel newTask, @PathVariable int id) {
    return service.createTask(newTask, id);
  }

  @Operation(summary = "Deletes task from todo", description = "Deletes task from todo by ID")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Task is successfully deleted"),
        @ApiResponse(responseCode = "404", description = "Todo with this id doesn't exist"),
        @ApiResponse(responseCode = "404", description = "Task with this name doesn't exist")
      })
  @DeleteMapping("/{id}/tasks")
  @ResponseStatus(value = HttpStatus.OK)
  public void deleteTodo(
      @PathVariable @Parameter(name = "id", description = "id of the todo", example = "1") int id,
      @RequestParam(value = "name", required = true) String name) {
    service.deleteTask(name, id);
  }
}
