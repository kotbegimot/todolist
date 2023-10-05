package com.example.todolist.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import com.example.todolist.model.TaskModel;
import com.example.todolist.model.TodoModel;
import com.example.todolist.properties.MainProperties;
import com.example.todolist.service.TodosService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
class TodoControllerTest {
  private MockMvc mvc;
  private final TodosService service = mock(TodosService.class);
  private final MainProperties properties = mock(MainProperties.class);
  private List<TodoModel> todos;
  private TodoModel todoModel;

  @BeforeEach
  void setUp() {
    TodoController controller = new TodoController(service);
    mvc =
        MockMvcBuilders.standaloneSetup(controller)
            .setControllerAdvice(new RestResponseEntityExceptionHandler(properties))
            .build();

    todos = new ArrayList<>();
    TaskModel taskModel =
        TaskModel.builder().name("some task").description("task description").build();
    todoModel =
        TodoModel.builder()
            .id(1)
            .name("some todo")
            .description("todo description")
            .tasks(List.of(taskModel))
            .build();
    todos.add(todoModel);
    taskModel =
        TaskModel.builder().name("another task").description("another task description").build();
    todos.add(
        TodoModel.builder()
            .id(2)
            .name("another todo")
            .description("another todo description")
            .tasks(List.of(taskModel))
            .build());
  }

  @AfterEach
  void tearDown() {
    todos.clear();
    reset(service);
    reset(properties);
  }

  @Test
  @DisplayName("Should fetch all todos")
  @WithMockUser
  void getAllTodosTest() throws Exception {
    when(service.getTodos()).thenReturn(todos);

    mvc.perform(get("/api/v1/todos").accept(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/json"))
        .andExpect(jsonPath("$.todos.size()", is(todos.size())))
        .andExpect(jsonPath("$.todos[0].name", is("some todo")))
        .andExpect(jsonPath("$.todos[0].description", is("todo description")))
        .andExpect(jsonPath("$.todos[0].tasks[0].name", is("some task")))
        .andExpect(jsonPath("$.todos[1].name", is("another todo")))
        .andExpect(jsonPath("$.todos[1].description", is("another todo description")))
        .andExpect(jsonPath("$.todos[1].tasks[0].name", is("another task")));
    verify(service, times(1)).getTodos();
    verifyNoMoreInteractions(service);
  }

  @Test
  @DisplayName("Should fetch the todo subscription by id")
  @WithMockUser
  void getTodoByIdTest() throws Exception {
    int id = 1;
    when(service.getTodo(id)).thenReturn(todos.get(id - 1));

    mvc.perform(get("/api/v1/todos" + "/%d".formatted(id)).accept(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/json"))
        .andExpect(jsonPath("$", notNullValue()))
        .andExpect(jsonPath("$.name", is(todoModel.getName())))
        .andExpect(jsonPath("$.description", is(todoModel.getDescription())))
        .andExpect(jsonPath("$.tasks[0].name", is(todoModel.getTasks().get(0).getName())))
        .andExpect(
            jsonPath("$.tasks[0].description", is(todoModel.getTasks().get(0).getDescription())));
    verify(service, times(1)).getTodo(id);
    verifyNoMoreInteractions(service);
  }

  @Test
  @DisplayName("Should call todo creation")
  @WithMockUser
  void createTodoTest() throws Exception {
    TodoModel newTodoModel =
        TodoModel.builder()
            .id(0)
            .name(todoModel.getName())
            .description(todoModel.getDescription())
            .tasks(todoModel.getTasks())
            .build();
    when(service.createTodo(newTodoModel)).thenReturn(todoModel);

    mvc.perform(
            post("/api/v1/todos")
                .content(toJsonString(newTodoModel))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isCreated())
        .andExpect(content().contentType("application/json"))
        .andExpect(jsonPath("$", notNullValue()))
        .andExpect(jsonPath("$.id", is(todoModel.getId())))
        .andExpect(jsonPath("$.name", is(todoModel.getName())))
        .andExpect(jsonPath("$.description", is(todoModel.getDescription())))
        .andExpect(jsonPath("$.tasks[0].name", is(todoModel.getTasks().get(0).getName())))
        .andExpect(
            jsonPath("$.tasks[0].description", is(todoModel.getTasks().get(0).getDescription())));
    verify(service, times(1)).createTodo(newTodoModel);
    verifyNoMoreInteractions(service);
  }

  @Test
  @DisplayName("Should call todo creation")
  @WithMockUser
  void updateTodoTest() throws Exception {
    int id = 1;
    TodoModel updTodoModel =
        TodoModel.builder()
            .id(0)
            .name(todoModel.getName())
            .description(todoModel.getDescription())
            .tasks(todoModel.getTasks())
            .build();
    when(service.updateTodo(todoModel)).thenReturn(todoModel);

    mvc.perform(
            put("/api/v1/todos" + "/%d".formatted(id))
                .content(toJsonString(updTodoModel))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/json"))
        .andExpect(jsonPath("$", notNullValue()))
        .andExpect(jsonPath("$.id", is(todoModel.getId())))
        .andExpect(jsonPath("$.name", is(todoModel.getName())))
        .andExpect(jsonPath("$.description", is(todoModel.getDescription())))
        .andExpect(jsonPath("$.tasks[0].name", is(todoModel.getTasks().get(0).getName())))
        .andExpect(
            jsonPath("$.tasks[0].description", is(todoModel.getTasks().get(0).getDescription())));
    verify(service, times(1)).updateTodo(todoModel);
    verifyNoMoreInteractions(service);
  }

  @Test
  @DisplayName("Should call todo deleting")
  @WithMockUser
  void deleteTodoTest() throws Exception {
    int id = 1;
    doNothing().when(service).deleteTodo(id);

    mvc.perform(delete("/api/v1/todos" + "/%d".formatted(id)))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").doesNotExist());
    verify(service, times(1)).deleteTodo(id);
    verifyNoMoreInteractions(service);
  }

  public String toJsonString(final Object obj) throws RuntimeException {
    try {
      return new ObjectMapper().writeValueAsString(obj);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
