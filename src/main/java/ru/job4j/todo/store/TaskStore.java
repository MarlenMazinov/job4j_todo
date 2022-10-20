package ru.job4j.todo.store;

import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Category;
import ru.job4j.todo.model.Priority;
import ru.job4j.todo.model.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class TaskStore {
    private final SessionFactory sf;

    public void create(Task task) {
        try (Session session = sf.openSession()) {
            try {
                session.beginTransaction();
                session.save(task);
                session.getTransaction().commit();
            } catch (Exception e) {
                session.getTransaction().rollback();
            }
        }
    }

    public boolean update(Task task) {
        boolean result = false;
        try (Session session = sf.openSession()) {
            try {
                session.beginTransaction();
                session.createQuery(
                                "update Task set name = :fname, description = :fdescription"
                                        + " where id = :fId")
                        .setParameter("fname", task.getName())
                        .setParameter("fdescription", task.getDescription())
                        .setParameter("fId", task.getId())
                        .executeUpdate();
                session.update(task);
                session.getTransaction().commit();
                result = true;
            } catch (Exception e) {
                session.getTransaction().rollback();
            }
        }
        return result;
    }

    public boolean updateTaskState(int id) {
        boolean result = false;
        try (Session session = sf.openSession()) {
            try {
                session.beginTransaction();
                session.createQuery(
                                "update Task set done = :fdone where id = :fId")
                        .setParameter("fdone", true)
                        .setParameter("fId", id)
                        .executeUpdate();
                session.getTransaction().commit();
                result = true;
            } catch (Exception e) {
                session.getTransaction().rollback();
            }
        }
        return result;
    }

    public boolean updateTaskPriority(String importance) {
        boolean result = false;
        try (Session session = sf.openSession()) {
            try {
                Priority priority;
                session.beginTransaction();
                Query query = session.createQuery(
                                "from Priority p where name = :fname").setParameter("fname", importance).
                        setMaxResults(1);
                priority = (Priority) query.uniqueResultOptional().get();
                session.createQuery(
                                "update Task set priority = :fpriority")
                        .setParameter("fpriority", priority)
                        .executeUpdate();
                session.getTransaction().commit();
                result = true;
            } catch (Exception e) {
                session.getTransaction().rollback();
            }
        }
        return result;
    }

    public void delete(int id) {
        try (Session session = sf.openSession()) {
            try {
                session.beginTransaction();
                session.createQuery(
                                "delete Task where id = :fId")
                        .setParameter("fId", id)
                        .executeUpdate();
                session.getTransaction().commit();
            } catch (Exception e) {
                session.getTransaction().rollback();
            }
        }
    }

    public Optional<Task> findByName(String name) {
        Optional<Task> result = Optional.empty();
        try (Session session = sf.openSession()) {
            try {
                session.beginTransaction();
                List<Task> list = session.createQuery("from Task t join fetch t.categories",
                        Task.class).getResultList();
                Query query = session.createQuery(
                        "from Task t join fetch t.priority where t.name = :fname and t in :flist",
                        Task.class);
                query.setParameter("fname", name);
                query.setParameter("flist", list);
                result = query.uniqueResultOptional();
                session.getTransaction().commit();
            } catch (Exception e) {
                session.getTransaction().rollback();
            }
        }
        return result;
    }

    public Optional<Task> findById(int id) {
        Optional<Task> result = Optional.empty();
        try (Session session = sf.openSession()) {
            try {
                session.beginTransaction();
                List<Task> list = session.createQuery("from Task t join fetch t.categories",
                        Task.class).getResultList();
                Query query = session.createQuery(
                        "from Task t join fetch t.priority where t.id = :fid and t in :flist",
                        Task.class);
                query.setParameter("fid", id);
                query.setParameter("flist", list);
                result = query.uniqueResultOptional();
                session.getTransaction().commit();
            } catch (Exception e) {
                session.getTransaction().rollback();
            }
        }
        return result;
    }

    public List<Task> findAllOrderByCreated() {
        List<Task> result = new ArrayList<>();
        try (Session session = sf.openSession()) {
            try {
                session.beginTransaction();
                List<Task> list = session.createQuery("from Task t join fetch t.categories",
                        Task.class).getResultList();
                result = session.createQuery(
                                "from Task t join fetch t.priority where t in :flist "
                                        + "order by t.created desc", Task.class)
                        .setParameter("flist", list).getResultList();
                session.getTransaction().commit();
            } catch (Exception e) {
                session.getTransaction().rollback();
            }
        }
        return result;
    }

    public List<Task> findTasksByDone(boolean key) {
        List<Task> result = new ArrayList<>();
        try (Session session = sf.openSession()) {
            try {
                session.beginTransaction();
                List<Task> list = session.createQuery("from Task t join fetch t.categories",
                        Task.class).getResultList();
                Query query = session.createQuery(
                        "from Task t join fetch t.priority where t.done = :fkey and t in :flist "
                                + "order by t.created desc", Task.class);
                query.setParameter("fkey", key);
                query.setParameter("flist", list);
                result = query.getResultList();
                session.getTransaction().commit();
            } catch (Exception e) {
                session.getTransaction().rollback();
            }
        }
        return result;
    }

    public List<Category> findAllCategories() {
        List<Category> result = new ArrayList<>();
        try (Session session = sf.openSession()) {
            try {
                session.beginTransaction();
                result = session.createQuery("from Category").list();
                session.getTransaction().commit();
            } catch (Exception e) {
                session.getTransaction().rollback();
            }
        }
        return result;
    }

    public Category findCategoryById(int id) {
        Category result = null;
        try (Session session = sf.openSession()) {
            try {
                session.beginTransaction();
                result = session.get(Category.class, id);
                session.getTransaction().commit();
            } catch (Exception e) {
                session.getTransaction().rollback();
            }
        }
        return result;
    }
}