package ru.job4j.todo.service;

import lombok.AllArgsConstructor;
import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Service;
import ru.job4j.todo.model.User;
import ru.job4j.todo.store.UserStore;

import java.util.Optional;

@Service
@ThreadSafe
@AllArgsConstructor
public class UserService {
    private final UserStore store;

    public User create(User user) {
        return store.create(user);
    }

    public boolean update(User user) {
        return store.update(user);
    }

    public void delete(User user) {
        store.delete(user);
    }

    public Optional<User> findByLoginAndPwd(String login, String password) {
        return store.findByLoginAndPwd(login, password);
    }

    public Optional<User> findById(int id) {
        return store.findById(id);
    }
}
