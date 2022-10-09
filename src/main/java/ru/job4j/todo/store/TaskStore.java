package ru.job4j.todo.store;

import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class TaskStore {
    private final SessionFactory sf;

    public Task create(Task task) {
        Session session = sf.openSession();
        try {
            session.beginTransaction();
            session.save(task);
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        }
        session.close();
        Optional<Task> fuser = findByName(task.getName());
        fuser.ifPresent(value -> task.setId(value.getId()));
        return task;
    }

    public boolean update(Task task) {
        Session session = sf.openSession();
        boolean result = false;
        try {
            session.beginTransaction();
            session.update(task);
            session.getTransaction().commit();
            result = true;
        } catch (Exception e) {
            session.getTransaction().rollback();
        }
        session.close();
        return result;
    }

    public void delete(Task task) {
        Session session = sf.openSession();
        try {
            session.beginTransaction();
            session.delete(task);
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        }
        session.close();
    }

    public Optional<Task> findByName(String name) {
        Session session = sf.openSession();
        Optional<Task> result = Optional.empty();
        try {
            session.beginTransaction();
            Query query = session.createQuery(
                    "from Task t where t.name = :fname");
            query.setParameter("fname", name);
            result = query.uniqueResultOptional();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        }
        session.close();
        return result;
    }

    public Optional<Task> findById(int id) {
        Session session = sf.openSession();
        Optional<Task> result = Optional.empty();
        try {
            session.beginTransaction();
            Query query = session.createQuery(
                    "from Task t where t.id = :fid");
            query.setParameter("fid", id);
            result = query.uniqueResultOptional();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        }
        session.close();
        return result;
    }

    public List<Task> findAllOrderByCreated() {
        Session session = sf.openSession();
        List<Task> result = new ArrayList<>();
        try {
            session.beginTransaction();
            result = session.createQuery(
                    "FROM Task t ORDER BY t.created DESC").list();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        }
        session.close();
        return result;
    }

    public List<Task> findTasksByDone(boolean key) {
        Session session = sf.openSession();
        List<Task> result = new ArrayList<>();
        try {
            session.beginTransaction();
            Query query = session.createQuery(
                    "from Task t where t.done = :fkey ORDER BY t.created DESC");
            query.setParameter("fkey", key);
            result = query.list();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        }
        session.close();
        return result;
    }
}