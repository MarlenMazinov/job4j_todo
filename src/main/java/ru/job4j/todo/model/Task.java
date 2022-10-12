package ru.job4j.todo.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "task")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private int id;
    @Column(unique = true)
    private String name;
    private String description;
    private LocalDateTime created;
    private boolean done;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
        this.done = false;
    }

    public Task() {

    }
}
