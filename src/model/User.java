package model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class User {
    private Long id;
    private String firstname;
    private String lastname;
    private String phone;
    private String password;
    private String address;

    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
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

    public String getPassword() {
        return this.password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddress() {
        return this.address;
    }
    public void setAddress(String address) {
        this.address = address;
    }

    public static User resultSetToUser(ResultSet resultSet) throws SQLException {
        User user = new User();
        user.setId(resultSet.getLong("id"));
        user.setFirstname(resultSet.getString("firstname"));
        user.setLastname(resultSet.getString("lastname"));
        user.setPhone(resultSet.getString("phone"));
        user.setPassword(resultSet.getString("password"));
        user.setAddress(resultSet.getString("address"));
        return user;
    }

    @Override
    public String toString() {
        return "{\"id\": " + id +
                ", \"firstname\": " + "\"" + firstname + "\"" +
                ", \"lastname\": " + "\"" + lastname + "\"" +
                ", \"phone\": " + "\"" + phone + "\"" +
                ", \"password\": " + "\"" + password + "\"" +
                ", \"address\": " + "\"" + address + "\"" + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) && Objects.equals(firstname, user.firstname) && Objects.equals(lastname, user.lastname) && Objects.equals(phone, user.phone) && Objects.equals(password, user.password) && Objects.equals(address, user.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstname, lastname, phone, password, address);
    }
}
