package com.example.todolist.service;

import com.example.todolist.model.TaskModel;
import com.example.todolist.model.TodoModel;
import com.example.todolist.model.exceptions.InvalidRequestException;
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
  @DisplayName("Name validation has to fail twice: on todo name and on task name")
  void validateTodoFailedTest() {
    when(properties.getErrorInvalidName()).thenReturn(invalidNameError);

    Assertions.assertThrows(InvalidRequestException.class, () -> service.validateTodo(todoModel));

    verify(properties, times(2)).getErrorInvalidName();
    assertEquals(2, service.getErrorMessages().size());
    assertEquals(invalidNameError.formatted("todo"), service.getErrorMessages().get(0));
    assertEquals(invalidNameError.formatted("task"), service.getErrorMessages().get(1));
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
