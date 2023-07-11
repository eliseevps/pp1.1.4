package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {
    public UserDaoJDBCImpl() {
        //Default constructor
    }

    public void createUsersTable() {
        Connection connection = Util.getConnection();
        String createUsersTableSql = """
                CREATE TABLE IF NOT EXISTS USERS (
                ID SERIAL PRIMARY KEY,
                NAME VARCHAR(45) NOT NULL,
                LASTNAME VARCHAR(45) NOT NULL,
                AGE INT NOT NULL);
                """;
        try (connection;
             Statement statement = connection.createStatement()) {
            statement.execute(createUsersTableSql);
            connection.commit();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            throw new RuntimeException(e);
        }
    }

    public void dropUsersTable() {
        Connection connection = Util.getConnection();
        String dropUsersTableSql = """
                DROP TABLE IF EXISTS USERS;
                """;
        try (connection;
             Statement statement = connection.createStatement()) {
            statement.execute(dropUsersTableSql);
            connection.commit();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            throw new RuntimeException(e);
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        Connection connection = Util.getConnection();
        String saveUserSql = """
                INSERT INTO USERS (NAME, LASTNAME, AGE) VALUES (?, ?, ?);
                """;
        try (connection;
             PreparedStatement statement = connection.prepareStatement(saveUserSql)) {
            statement.setString(1, name);
            statement.setString(2, lastName);
            statement.setByte(3, age);
            statement.executeUpdate();
            connection.commit();
            System.out.println("User с именем - " + name + " добавлен в базу данных");
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            throw new RuntimeException(e);
        }
    }

    public void removeUserById(long id) {
        Connection connection = Util.getConnection();
        String removeUserByIdSql = """
                DELETE FROM USERS WHERE ID = ?;
                """;
        try (connection;
             PreparedStatement statement = connection.prepareStatement(removeUserByIdSql)) {
            statement.setLong(1, id);
            statement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            throw new RuntimeException(e);
        }
    }

    public List<User> getAllUsers() {
        Connection connection = Util.getConnection();
        List<User> users = new ArrayList<>();
        String getAllUsersSql = """
                SELECT * FROM USERS;
                """;
        try (connection; Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(getAllUsersSql)) {
            while (resultSet.next()) {
                users.add(new User(resultSet.getLong("ID"),
                        resultSet.getString("NAME"),
                        resultSet.getString("LASTNAME"),
                        resultSet.getByte("AGE")));
            }
            connection.commit();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            throw new RuntimeException(e);
        }
        return users;
    }

    public void cleanUsersTable() {
        Connection connection = Util.getConnection();
        String cleanUsersTableSql = """
                TRUNCATE TABLE USERS;
                """;
        try (connection; Statement statement = connection.createStatement()) {
            statement.executeUpdate(cleanUsersTableSql);
            connection.commit();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            throw new RuntimeException(e);
        }
    }
}
