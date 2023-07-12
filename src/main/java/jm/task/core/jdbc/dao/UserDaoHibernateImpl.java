package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.ArrayList;
import java.util.List;

public class UserDaoHibernateImpl implements UserDao {

    private static final SessionFactory SESSION_FACTORY = Util.getSessionFactory();
    public UserDaoHibernateImpl() {
        //Default constructor
    }


    @Override
    public void createUsersTable() {
        String createUsersTableSql = """
                CREATE TABLE IF NOT EXISTS USERS (
                ID SERIAL PRIMARY KEY,
                NAME VARCHAR(45) NOT NULL,
                LASTNAME VARCHAR(45) NOT NULL,
                AGE INT NOT NULL);
                """;
        try (Session session = SESSION_FACTORY.openSession()) {
            session.beginTransaction();
            session.createSQLQuery(createUsersTableSql).executeUpdate();
            session.getTransaction().commit();
        }
    }

    @Override
    public void dropUsersTable() {
        String dropUsersTableSql = """
                DROP TABLE IF EXISTS USERS;
                """;
        try (Session session = SESSION_FACTORY.openSession()) {
            session.beginTransaction();
            session.createSQLQuery(dropUsersTableSql).executeUpdate();
            session.getTransaction().commit();
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        Session session = SESSION_FACTORY.openSession();
        try (session) {
            session.beginTransaction();
            User user = new User(name, lastName, age);
            session.save(user);
            session.getTransaction().commit();
            System.out.println("User с именем - " + name + " добавлен в базу данных");
        } catch (Exception e) {
            session.getTransaction().rollback();
            e.printStackTrace();
        }
    }

    @Override
    public void removeUserById(long id) {
        Session session = SESSION_FACTORY.openSession();
        try (session) {
            session.beginTransaction();
            session.createQuery("delete User where id = id");
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            e.printStackTrace();
        }
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        Session session = SESSION_FACTORY.openSession();
        try (session) {
            session.beginTransaction();
            users = session.createQuery("from User").list();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public void cleanUsersTable() {
        String cleanUsersTableSql = """
                TRUNCATE USERS;
                """;
        try (Session session = SESSION_FACTORY.openSession()) {
            session.beginTransaction();
            session.createSQLQuery(cleanUsersTableSql).executeUpdate();
            session.getTransaction().commit();
        }
    }
}
