package com.example.todolist.controller;

import com.example.todolist.model.TaskModel;
import com.example.todolist.model.TodoModel;
import com.example.todolist.model.exceptions.NoSuchTaskFoundException;
import com.example.todolist.model.exceptions.NoSuchTodoFoundException;
import com.example.todolist.model.exceptions.TodoAlreadyExistsException;
import com.example.todolist.properties.MainProperties;
import com.example.todolist.service.TodosService;
import com.example.todolist.util.ControllerUtil;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
        TaskModel.builder().name("some task name").description("task description").build();
    todoModel =
        TodoModel.builder()
            .id(1)
            .name("some todo name")
            .description("todo description")
            .tasks(List.of(taskModel))
            .build();
    todos.add(todoModel);
    taskModel =
        TaskModel.builder().name("another task name").description("another task description").build();
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
        .andExpect(jsonPath("$.todos[0].name", is(todos.get(0).getName())))
        .andExpect(jsonPath("$.todos[0].description", is(todos.get(0).getDescription())))
        .andExpect(jsonPath("$.todos[0].tasks[0].name", is(todos.get(0).getTasks().get(0).getName())))
        .andExpect(jsonPath("$.todos[1].name", is(todos.get(1).getName())))
        .andExpect(jsonPath("$.todos[1].description", is(todos.get(1).getDescription())))
        .andExpect(jsonPath("$.todos[1].tasks[0].name", is(todos.get(1).getTasks().get(0).getName())));
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
  @DisplayName("Should return 404")
  @WithMockUser
  void getTodoByIdTodoNotFoundTest() throws Exception {
    int id = 1;
    when(properties.getExceptionDateFormat()).thenReturn("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
    doThrow(new NoSuchTodoFoundException(id)).when(service).getTodo(id);

    mvc.perform(get("/api/v1/todos" + "/%d".formatted(id)).accept(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isNotFound())
            .andExpect(content().contentType("application/json"))
            .andExpect(jsonPath("$", notNullValue()))
            .andExpect(jsonPath("$.status", is(HttpStatus.NOT_FOUND.value())))
            .andExpect(jsonPath("$.error", is(HttpStatus.NOT_FOUND.getReasonPhrase())))
            .andExpect(jsonPath("$.messages[0]", is("Todo ID is not found: " + id)))
            .andExpect(jsonPath("$.path", is("http://localhost/api/v1/todos/" + id)))
            .andExpect(jsonPath("$.timestamp", notNullValue()));
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
                .content(ControllerUtil.toJsonString(newTodoModel))
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
  @DisplayName("Should return 400")
  @WithMockUser
  void createTodoInvalidBodyTest() throws Exception {
    TodoModel newTodoModel = TodoModel.builder()
            .id(0)
            .name(ControllerUtil.createStringWithLength(9))
            .description(ControllerUtil.createStringWithLength(9))
            .tasks(new ArrayList<>())
            .build();

    when(properties.getExceptionDateFormat()).thenReturn("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
    mvc.perform(
                    post("/api/v1/todos")
                            .content(ControllerUtil.toJsonString(newTodoModel))
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$", notNullValue()))
            .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.value())))
            .andExpect(jsonPath("$.error", is(HttpStatus.BAD_REQUEST.getReasonPhrase())))
            .andExpect(jsonPath("$.messages", hasSize(2)))
            .andExpect(jsonPath("$.messages[0]", Matchers.endsWith("length must be of 10 - 255 characters")))
            .andExpect(jsonPath("$.messages[0]", Matchers.startsWith("Field error in object")))
            .andExpect(jsonPath("$.messages[1]", Matchers.endsWith("length must be of 10 - 255 characters")))
            .andExpect(jsonPath("$.messages[1]", Matchers.startsWith("Field error in object")))
            .andExpect(jsonPath("$.path", is("http://localhost/api/v1/todos")))
            .andExpect(jsonPath("$.timestamp", notNullValue()));
    verifyNoInteractions(service);
  }

  @Test
  @DisplayName("Should return 409")
  @WithMockUser
  void createTodoNameAlreadyExistsTest() throws Exception {
    TodoModel newTodoModel =
            TodoModel.builder()
                    .id(0)
                    .name(todoModel.getName())
                    .description(todoModel.getDescription())
                    .tasks(todoModel.getTasks())
                    .build();
    doThrow(new TodoAlreadyExistsException(newTodoModel.getName())).when(service).createTodo(newTodoModel);
    when(properties.getExceptionDateFormat()).thenReturn("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
    mvc.perform(
                    post("/api/v1/todos")
                            .content(ControllerUtil.toJsonString(newTodoModel))
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isConflict())
            .andExpect(jsonPath("$", notNullValue()))
            .andExpect(jsonPath("$.status", is(HttpStatus.CONFLICT.value())))
            .andExpect(jsonPath("$.error", is(HttpStatus.CONFLICT.getReasonPhrase())))
            .andExpect(jsonPath("$.messages", hasSize(1)))
            .andExpect(jsonPath("$.messages[0]",
                    is("Todo with this name already exists: " + newTodoModel.getName())))
            .andExpect(jsonPath("$.path", is("http://localhost/api/v1/todos")))
            .andExpect(jsonPath("$.timestamp", notNullValue()));
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
                .content(ControllerUtil.toJsonString(updTodoModel))
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

  @Test
  @DisplayName("Should call task creation")
  @WithMockUser
  void createTaskTest() throws Exception {
    int id = 1;
    TaskModel taskModel = TaskModel.builder()
            .name("some task name")
            .description("task description")
            .build();
    when(service.createTask(taskModel, id)).thenReturn(taskModel);

    mvc.perform(post("/api/v1/todos" + "/%d/tasks".formatted(id))
                    .content(ControllerUtil.toJsonString(taskModel))
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isCreated())
            .andExpect(content().contentType("application/json"))
            .andExpect(jsonPath("$", notNullValue()))
            .andExpect(jsonPath("$.name", is(taskModel.getName())))
            .andExpect(jsonPath("$.description", is(taskModel.getDescription())));
    verify(service, times(1)).createTask(taskModel, id);
    verifyNoMoreInteractions(service);
  }

  @Test
  @DisplayName("Should call task deleting")
  @WithMockUser
  void deleteTaskTest() throws Exception {
    int id = 1;
    String taskName = "Task name";
    doNothing().when(service).deleteTask(taskName, id);

    mvc.perform(delete("/api/v1/todos" + "/%d/tasks".formatted(id))
                    .queryParam("name", taskName)
                    .accept(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").doesNotExist());
    verify(service, times(1)).deleteTask(taskName, id);
    verifyNoMoreInteractions(service);
  }

  @Test
  @DisplayName("Should return 404")
  @WithMockUser
  void deleteTaskNotFoundTest() throws Exception {
    int id = 1;
    String taskName = "Task name";
    when(properties.getExceptionDateFormat()).thenReturn("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
    doThrow(new NoSuchTaskFoundException(taskName)).when(service).deleteTask(taskName, id);

    mvc.perform(delete("/api/v1/todos" + "/%d/tasks".formatted(id))
                    .queryParam("name", taskName)
                    .accept(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isNotFound())
            .andExpect(content().contentType("application/json"))
            .andExpect(jsonPath("$", notNullValue()))
            .andExpect(jsonPath("$.status", is(HttpStatus.NOT_FOUND.value())))
            .andExpect(jsonPath("$.error", is(HttpStatus.NOT_FOUND.getReasonPhrase())))
            .andExpect(jsonPath("$.messages", notNullValue()))
            .andExpect(jsonPath("$.path", is("http://localhost/api/v1/todos/" + id + "/tasks")))
            .andExpect(jsonPath("$.timestamp", notNullValue()));
  }

  @Test
  @DisplayName("Should return 500")
  @WithMockUser
  void deleteTaskRunTimeErrorTest() throws Exception {
    int id = 1;
    String taskName = "Task name";
    when(properties.getExceptionDateFormat()).thenReturn("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
    doThrow(new RuntimeException("Accessing field on null object")).when(service).deleteTask(taskName, id);

    mvc.perform(delete("/api/v1/todos" + "/%d/tasks".formatted(id))
                    .queryParam("name", taskName)
                    .accept(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isInternalServerError())
            .andExpect(content().contentType("application/json"))
            .andExpect(jsonPath("$", notNullValue()))
            .andExpect(jsonPath("$.status", is(HttpStatus.INTERNAL_SERVER_ERROR.value())))
            .andExpect(jsonPath("$.error", is(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())))
            .andExpect(jsonPath("$.messages[0]", is("Accessing field on null object")))
            .andExpect(jsonPath("$.path", is("http://localhost/api/v1/todos/" + id + "/tasks")))
            .andExpect(jsonPath("$.timestamp", notNullValue()));
  }
}
