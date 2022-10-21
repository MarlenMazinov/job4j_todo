package ru.job4j.todo.service;

import lombok.AllArgsConstructor;
import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Service;
import ru.job4j.todo.model.Category;
import ru.job4j.todo.model.Priority;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.store.TaskStore;

import java.util.List;
import java.util.Optional;

@Service
@ThreadSafe
@AllArgsConstructor
public class TaskService {
    private final TaskStore store;

    public void create(Task task, List<Integer> categoryId, int priorityId) {
        store.create(task, categoryId, priorityId);
    }

    public boolean update(Task task, List<Integer> categoryId, int priorityId) {
        return store.update(task, categoryId, priorityId);
    }

    public boolean updateTaskState(int id) {
        return store.updateTaskState(id);
    }

    public boolean updateTaskPriority(String importance) {
        return store.updateTaskPriority(importance);
    }

    public void delete(int id) {
        store.delete(id);
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

    public List<Category> findAllCategories() {
        return store.findAllCategories();
    }

    public List<Priority> findAllPriorities() { return store.findAllPriorities(); }
}
