package com.example.todolist.service;

import com.example.todolist.model.TaskModel;
import com.example.todolist.model.TodoModel;
import com.example.todolist.properties.MainProperties;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ValidationServiceTest {
  private final MainProperties properties = mock(MainProperties.class);
  private ValidationService service;
  private TodoModel todoModel;
  private TaskModel taskModel;
  private String invalidNameError;

  @BeforeEach
  void setUp() {
    service = new ValidationService(properties);
    taskModel = TaskModel.builder().name("").description("").build();
    todoModel =
        TodoModel.builder().id(0).name("").description("").tasks(List.of(taskModel)).build();
    invalidNameError = "Invalid name %s: string shouldn't be null or empty";
  }

  @AfterEach
  public void tearDown() {
    reset(properties);
  }

  @Test
  @DisplayName("Name validation passes and doesn't throw InvalidRequestException")
  void validateTodoSuccessTest() {
    todoModel.setName("todoName");
    taskModel.setName("taskName");
    when(properties.getErrorInvalidName()).thenReturn(invalidNameError);

    service.validateTodo(todoModel);

    verify(properties, times(0)).getErrorInvalidName();
    assertEquals(0, service.getErrorMessages().size());
  }

  @Test
  @DisplayName("Returns error messages list")
  void getErrorMessagesTest() {
    List<String> errors = service.getErrorMessages();

    verify(properties, times(0)).getErrorInvalidName();
    assertEquals(0, errors.size());
  }
}
