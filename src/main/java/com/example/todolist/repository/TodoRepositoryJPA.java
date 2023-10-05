package com.example.todolist.repository;

import com.example.todolist.entity.TodoEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin(origins = "http://localhost:4200")
public interface TodoRepositoryJPA extends JpaRepository<TodoEntity, Integer> {
  Optional<TodoEntity> findByName(String name);
}
