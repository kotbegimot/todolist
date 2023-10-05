package com.example.todolist.repository;

import com.example.todolist.entity.TaskEntity;
import com.example.todolist.entity.TodoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.Optional;

@CrossOrigin(origins = "http://localhost:4200")
public interface TaskRepositoryJPA extends JpaRepository<TaskEntity, Integer> {
}
