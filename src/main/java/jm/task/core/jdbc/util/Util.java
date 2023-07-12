package jm.task.core.jdbc.util;

import jm.task.core.jdbc.model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Util {

    private static final Properties PROPERTIES = new Properties();
    private static final String URL_KEY = "db.url";
    private static final String USERNAME_KEY = "db.username";
    private static final String PASSWORD_KEY = "db.password";
    private static Connection connection;
    private static SessionFactory sessionFactory;

    static {
        loadProperties();
    }

    private Util() {
    }

    private static void loadProperties() {
        try (InputStream stream = Util.class.getClassLoader().getResourceAsStream("application.properties")) {
            PROPERTIES.load(stream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static String get(String key) {
        return PROPERTIES.getProperty(key);
    }

    public static Connection getConnection() {
        try {
            connection = DriverManager.getConnection(Util.get(URL_KEY), Util.get(USERNAME_KEY), Util.get(PASSWORD_KEY));
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            System.err.println("Connection failed!");
        }
        return connection;
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory = new Configuration()
                .addAnnotatedClass(User.class)
                .buildSessionFactory();
    }
}
