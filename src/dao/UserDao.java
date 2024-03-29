package dao;

import exception.EmptyUserSetException;
import exception.UserAlreadyExistsException;
import exception.UserNotExistsException;
import model.User;
import util.HashUtil;
import util.Role;

import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDao {
    private final Connection connection;

    public UserDao(Connection connection) {
        this.connection = connection;
    }

    public void createUser(String firstname, String lastname, String phone, String password, String address)
            throws SQLException, NoSuchAlgorithmException {
        if(existsByPhone(phone)) {
            throw new UserAlreadyExistsException("User with this phone already exists");
        }

        byte[] hashPassword = HashUtil.hashPassword(password);

        PreparedStatement preparedStatement = connection
                .prepareStatement("INSERT INTO users (firstname, lastname, phone, password, address, role)" +
                        "VALUES (?, ?, ?, ?, ?, ?)");

        preparedStatement.setString(1, firstname);
        preparedStatement.setString(2, lastname);
        preparedStatement.setString(3, phone);
        preparedStatement.setBytes(4, hashPassword);
        preparedStatement.setString(5, address);
        preparedStatement.setString(6, Role.ROLE_USER.getRole());

        preparedStatement.executeUpdate();
    }

    public void createAdmin() throws SQLException, NoSuchAlgorithmException {
        byte[] hashPassword = HashUtil.hashPassword("admin");

        PreparedStatement preparedStatement = connection
                .prepareStatement("INSERT INTO users (firstname, lastname, phone, password, address, role)" +
                        "VALUES (?, ?, ?, ?, ?, ?)");

        preparedStatement.setString(1, "admin");
        preparedStatement.setString(2, "admin");
        preparedStatement.setString(3, "admin");
        preparedStatement.setBytes(4, hashPassword);
        preparedStatement.setString(5, "admin");
        preparedStatement.setString(6, Role.ROLE_ADMIN.getRole());

        preparedStatement.executeUpdate();
    }

    public List<User> getAllUsers() throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM users");
        List<User> users = new ArrayList<>();
        if(!resultSet.next()) {
            throw new EmptyUserSetException("There is no users");
        }

        users.add(User.resultSetToUser(resultSet));
        while(resultSet.next()) {
            users.add(User.resultSetToUser(resultSet));
        }

        return users;
    }

    public User getUserById(int id) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM users WHERE id = ?");
        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        if(!resultSet.next()) {
            throw new UserNotExistsException("User doesn't exist");
        }

        return User.resultSetToUser(resultSet);
    }

    public User getUserByPhone(String phone) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM users WHERE phone = ?");
        preparedStatement.setString(1, phone);
        ResultSet resultSet = preparedStatement.executeQuery();
        if(!resultSet.next()) {
            throw new UserNotExistsException("User doesn't exist");
        }

        return User.resultSetToUser(resultSet);
    }

    public void updateUserById(int id, String address) throws SQLException {
        if(!existsById(id)) {
            throw new UserNotExistsException("There is no user with such id");
        }

        PreparedStatement preparedStatement = connection
                .prepareStatement("UPDATE users SET address = ? WHERE id = ?");

        preparedStatement.setString(1, address);
        preparedStatement.setInt(2, id);

        preparedStatement.executeUpdate();
    }

    public boolean existsByPhone(String phone) throws SQLException{
        PreparedStatement existsByPhone = connection.
                prepareStatement("SELECT 1 FROM users WHERE phone = ?");
        existsByPhone.setString(1, phone);
        ResultSet resultSet = existsByPhone.executeQuery();

        return resultSet.next();
    }

    public boolean existsById(int id) throws SQLException{
        PreparedStatement existsById = connection.
                prepareStatement("SELECT 1 FROM users WHERE id = ?");
        existsById.setInt(1, id);
        ResultSet resultSet = existsById.executeQuery();

        return resultSet.next();
    }
}
