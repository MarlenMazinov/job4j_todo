package ru.job4j.todo.store;

import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.User;

import java.util.Optional;

@Repository
@AllArgsConstructor
public class UserStore {
    private final SessionFactory sf;

    public User create(User user) {
        try (Session session = sf.openSession()) {
            try {
                session.beginTransaction();
                session.save(user);
                session.getTransaction().commit();
            } catch (Exception e) {
                session.getTransaction().rollback();
            }
        }
        Optional<User> fuser = findByLoginAndPwd(user.getLogin(), user.getPassword());
        fuser.ifPresent(value -> user.setId(value.getId()));
        return user;
    }

    public Optional<User> findByLoginAndPwd(String login, String password) {
        Optional<User> result = Optional.empty();
        try (Session session = sf.openSession()) {
            try {
                session.beginTransaction();
                Query query = session.createQuery(
                        "from User u where u.login = :flogin and u.password = :fpassword");
                query.setParameter("flogin", login);
                query.setParameter("fpassword", password);
                result = query.uniqueResultOptional();
                session.getTransaction().commit();
            } catch (Exception e) {
                session.getTransaction().rollback();
            }
        }
        return result;
    }
}
