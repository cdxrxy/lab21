package model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class User {
    private int id;
    private String firstname;
    private String lastname;
    private String phone;
    private byte[] password;
    private String address;
    private String role;

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstname() {
        return this.firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return this.lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public byte[] getPassword() {
        return this.password;
    }

    public void setPassword(byte[] password) {
        this.password = password;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRole() {
        return this.role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public static User resultSetToUser(ResultSet resultSet) throws SQLException {
        User user = new User();
        user.setId(resultSet.getInt("id"));
        user.setFirstname(resultSet.getString("firstname"));
        user.setLastname(resultSet.getString("lastname"));
        user.setPhone(resultSet.getString("phone"));
        user.setPassword(resultSet.getBytes("password"));
        user.setAddress(resultSet.getString("address"));
        user.setRole(resultSet.getString("role"));
        return user;
    }

    @Override
    public String toString() {
        return "{\"id\": " + id +
                ", \"firstname\": " + "\"" + firstname + "\"" +
                ", \"lastname\": " + "\"" + lastname + "\"" +
                ", \"phone\": " + "\"" + phone + "\"" +
                ", \"address\": " + "\"" + address + "\"" +
                ", \"role\": " + "\"" + role + "\"" + "}";
    }
}
