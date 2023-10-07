package com.example.todolist.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Table(name = "tasks")
public class TaskEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "task_id")
  private int id;

  @Column(name = "name")
  @NotBlank(message = "Name must not be null or empty")
  private String name;

  @Column(name = "description")
  @NotBlank(message = "Description must not be null or empty")
  private String description;

  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @OnDelete(action = OnDeleteAction.NO_ACTION)
  @JoinColumn(name = "todo_id", nullable = false)
  @EqualsAndHashCode.Exclude
  private TodoEntity todoEntity;
}
