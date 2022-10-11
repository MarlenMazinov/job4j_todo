package ru.job4j.todo.service;

import lombok.AllArgsConstructor;
import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Service;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.store.TaskStore;

import java.util.List;
import java.util.Optional;

@Service
@ThreadSafe
@AllArgsConstructor
public class TaskService {
    private final TaskStore store;

    public void create(Task task) {
        store.create(task);
    }

    public boolean update(Task task) {
        return store.update(task);
    }

    public boolean updateTaskState(int id) {
        return store.updateTaskState(id);
    }

    public void delete(int id) {
        store.delete(id);
    }

    public Optional<Task> findByName(String name) {
        return store.findByName(name);
    }

    public Optional<Task> findById(int id) {
        return store.findById(id);
    }

    public List<Task> findAllOrderByCreated() {
        return store.findAllOrderByCreated();
    }

    public List<Task> findTasksByDone(boolean key) {
        return store.findTasksByDone(key);
    }
}
