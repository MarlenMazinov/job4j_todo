package ru.job4j.todo.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "task")
@Data
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(unique = true)
    private String name;
    private String description;
    private String created;
    private boolean done;

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
        this.done = false;
    }

    public Task() {

    }
}
