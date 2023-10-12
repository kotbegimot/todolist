package com.example.todolist.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

@Getter
@Setter
@Entity
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "todos")
public class TodoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "todo_id")
    @EqualsAndHashCode.Exclude
    int id;

    @Column(name = "name")
    @NotBlank(message = "Name must not be null or empty")
    String name;

    @Column(name = "description")
    @NotBlank(message = "Description must not be null or empty")
    String description;

    @OneToMany(mappedBy = "todoEntity",
            orphanRemoval = true,
            fetch = FetchType.EAGER)
    @Cascade(CascadeType.ALL)
    @Builder.Default
    List<TaskEntity> tasks = new ArrayList<>();

    public void addTask(TaskEntity task) {
        if (tasks == null) {
            tasks = new ArrayList<>();
        }
        task.setTodoEntity(this);
        tasks.add(task);
    }
}
