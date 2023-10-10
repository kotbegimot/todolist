package com.example.todolist.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.todolist.entity.TaskEntity;
import com.example.todolist.entity.TodoEntity;
import com.example.todolist.model.TaskModel;
import com.example.todolist.model.TodoModel;
import com.example.todolist.model.exceptions.NoSuchTodoFoundException;
import com.example.todolist.repository.TaskRepositoryJPA;
import com.example.todolist.repository.TodoRepositoryJPA;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.*;

class TodosServiceTest {
  private final TodoRepositoryJPA todoRepository = mock(TodoRepositoryJPA.class);
  private final TaskRepositoryJPA taskRepository = mock(TaskRepositoryJPA.class);
  private final ValidationService validation = mock(ValidationService.class);
  private TodosService service;
  private TodoModel todoModel;
  private TodoEntity todoEntity;
  private List<TodoModel> modelList;
  private List<TodoEntity> entityList;

  @BeforeEach
  void setUp() {
    service = new TodosService(todoRepository, taskRepository, validation);
    TaskModel taskModel =
        TaskModel.builder().name("some task").description("task description").build();
    todoModel =
        TodoModel.builder()
            .id(0)
            .name("some todo")
            .description("todo description")
            .tasks(List.of(taskModel))
            .build();
    TaskEntity taskEntity =
        TaskEntity.builder()
            .id(0)
            .name(taskModel.getName())
            .description(taskModel.getDescription())
            .build();
    todoEntity =
        TodoEntity.builder()
            .id(todoModel.getId())
            .name(todoModel.getName())
            .description(todoModel.getDescription())
            .build();
    todoEntity.addTask(taskEntity);
    modelList = List.of(todoModel);
    entityList = List.of(todoEntity);
  }

  @AfterEach
  void tearDown() {
    reset(todoRepository);
    reset(validation);
  }

  @Test
  @DisplayName("Service should return list of todo objects")
  void getAllTodosTest() {
    when(todoRepository.findAll()).thenReturn(entityList);

    assertEquals(modelList, service.getTodos());
    verify(todoRepository, times(1)).findAll();
    verifyNoMoreInteractions(todoRepository);
    verifyNoInteractions(taskRepository);
  }

  @Test
  @DisplayName("Service should throw the NoSuchTodoFoundException exception")
  void getInvalidTodoByIdTest() {
    int id = 1;
    when(todoRepository.findById(id)).thenReturn(Optional.empty());

    Assertions.assertThrows(NoSuchTodoFoundException.class, () -> service.getTodo(id));

    verify(todoRepository, times(1)).findById(id);
    verifyNoMoreInteractions(todoRepository);
    verifyNoInteractions(taskRepository);
  }

  @Test
  @DisplayName("Service should return todo object")
  void getTodoByIdTest() {
    int id = 1;
    when(todoRepository.findById(id)).thenReturn(Optional.of(todoEntity));

    assertEquals(todoModel, service.getTodo(id));
    verify(todoRepository, times(1)).findById(id);
    verifyNoMoreInteractions(todoRepository);
    verifyNoInteractions(taskRepository);
  }

  @Test
  @DisplayName("Service should execute todo creation")
  void createTodoSuccessTest() {
    TodoEntity todoEntityOutput = todoEntity.toBuilder().build();
    todoEntityOutput.setId(1);

    doNothing().when(validation).validateTodo(todoModel);
    when(todoRepository.findByName(todoEntity.getName())).thenReturn(Optional.empty());
    when(todoRepository.save(todoEntity)).thenReturn(todoEntityOutput);

    TodoModel outputModel = service.createTodo(todoModel);

    verify(todoRepository, times(1)).findByName(todoEntity.getName());
    verify(todoRepository, times(1)).save(todoEntity);
    assertEquals(1, outputModel.getId());
    assertEquals(todoModel.getName(), outputModel.getName());
    assertEquals(todoModel.getDescription(), outputModel.getDescription());
    assertEquals(todoModel.getTasks().get(0).getName(), outputModel.getTasks().get(0).getName());
    assertEquals(
        todoModel.getTasks().get(0).getDescription(),
        outputModel.getTasks().get(0).getDescription());
    verifyNoMoreInteractions(todoRepository);
    verifyNoInteractions(taskRepository);
  }

  @Test
  @DisplayName("Service should throw the NoSuchTodoFoundException exception")
  void updateTodoFailureTest() {
    int id = 1;
    todoModel.setId(id);
    when(todoRepository.findById(id)).thenReturn(Optional.empty());

    Assertions.assertThrows(NoSuchTodoFoundException.class, () -> service.updateTodo(todoModel));

    verify(todoRepository, times(1)).findById(id);
    verify(todoRepository, times(0)).save(todoEntity);
    verify(validation, times(0)).validateTodo(todoModel);
    verifyNoMoreInteractions(todoRepository);
    verifyNoInteractions(taskRepository);
  }

  @Test
  @DisplayName("Service should execute todo update")
  void updateTodoSuccessTest() {
    int id = 1;
    todoModel.setId(id);
    todoEntity.setId(id);
    when(todoRepository.findById(id)).thenReturn(Optional.of(todoEntity));
    doNothing().when(validation).validateTodo(todoModel);
    when(todoRepository.save(todoEntity)).thenReturn(todoEntity);

    TodoModel outputModel = service.updateTodo(todoModel);

    verify(todoRepository, times(1)).findById(id);
    verify(todoRepository, times(1)).save(todoEntity);
    verify(validation, times(1)).validateTodo(todoModel);
    assertEquals(todoModel.getName(), outputModel.getName());
    assertEquals(todoModel.getDescription(), outputModel.getDescription());
    assertEquals(todoModel.getTasks().get(0).getName(), outputModel.getTasks().get(0).getName());
    assertEquals(
        todoModel.getTasks().get(0).getDescription(),
        outputModel.getTasks().get(0).getDescription());
    verifyNoMoreInteractions(todoRepository);
    verifyNoInteractions(taskRepository);
  }

  @Test
  @DisplayName("Service should throw the NoSuchTodoFoundException exception")
  void deleteTodoFailureTest() {
    int id = 1;
    when(todoRepository.findById(id)).thenReturn(Optional.empty());

    Assertions.assertThrows(NoSuchTodoFoundException.class, () -> service.deleteTodo(id));

    verify(todoRepository, times(1)).findById(id);
    verify(todoRepository, times(0)).delete(todoEntity);
    verifyNoMoreInteractions(todoRepository);
    verifyNoInteractions(taskRepository);
  }

  @Test
  @DisplayName("Service should execute todo deletion")
  void deleteTodoSuccessTest() {
    int id = 1;
    when(todoRepository.findById(id)).thenReturn(Optional.of(todoEntity));

    service.deleteTodo(id);

    verify(todoRepository, times(1)).findById(id);
    verify(todoRepository, times(1)).delete(todoEntity);
    verifyNoMoreInteractions(todoRepository);
    verifyNoInteractions(taskRepository);
  }
}
