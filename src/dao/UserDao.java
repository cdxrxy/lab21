package dao;

import exception.EmptyUserSetException;
import exception.UserNotExistsException;
import model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDao {
    private final Connection connection;

    public UserDao(Connection connection) {
        this.connection = connection;
    }

    public int createUser(String firstname, String lastname, String phone, String password, String address)
            throws SQLException {
        PreparedStatement preparedStatement = connection
                .prepareStatement("INSERT INTO users (firstname, lastname, phone, password, address)" +
                        "VALUES (?, ?, ?, ?, ?)");

        preparedStatement.setString(1, firstname);
        preparedStatement.setString(2, lastname);
        preparedStatement.setString(3, phone);
        preparedStatement.setString(4, password);
        preparedStatement.setString(5, address);

        return preparedStatement.executeUpdate();
    }

    public List<User> getAllUsers() throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM users");
        List<User> users = new ArrayList<>();
        if(!resultSet.next()) {
            throw new EmptyUserSetException("There is no users");
        }
        while(resultSet.next()) {
            users.add(User.resultSetToUser(resultSet));
        }
        return users;
    }

    public User getUserById(Long id) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM users WHERE id = ?");
        preparedStatement.setLong(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        if(!resultSet.next()) {
            throw new UserNotExistsException("User doesn't exist");
        }
        return User.resultSetToUser(resultSet);
    }

    public int updateUserById(Long id, String address) throws SQLException {
        PreparedStatement preparedStatement = connection
                .prepareStatement("UPDATE users SET address = ? WHERE id = ?");

        preparedStatement.setString(1, address);
        preparedStatement.setLong(2, id);

        return preparedStatement.executeUpdate();
    }
}
